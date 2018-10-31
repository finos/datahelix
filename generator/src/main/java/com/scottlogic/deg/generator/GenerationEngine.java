package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.AndConstraint;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.ViolateConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.targets.IOutputTarget;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerationEngine {
    private final IDecisionTreeGenerator profileAnalyser = new DecisionTreeGenerator();
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
    private final IDataGenerator dataGenerator;

    private final IOutputTarget outputter;

    public GenerationEngine(IOutputTarget outputter, boolean optimiseTree) {
        this.outputter = outputter;
        dataGenerator = new DataGenerator(
                new RowSpecMerger(fieldSpecMerger),
                new ConstraintReducer(
                        new FieldSpecFactory(),
                        fieldSpecMerger),
                optimiseTree
                        ? new DecisionTreeOptimiser()
                        : new NoopDecisionTreeOptimiser());
    }

    public void generateDataSet(Path profileFilePath, GenerationConfig config) throws IOException, InvalidProfileException {
        final Profile profile = new ProfileReader().read(profileFilePath);

        final Stream<GeneratedObject> generatedDataItems = generate(profile, config);

        this.outputter.outputDataset(generatedDataItems, profile.fields);
    }

    public void generateTestCases(Path profileFilePath, GenerationConfig config) throws IOException, InvalidProfileException {
        final Profile profile = new ProfileReader().read(profileFilePath);

        final TestCaseDataSet validCase = new TestCaseDataSet("", generate(profile, config));

        System.out.println("Valid cases generated, starting violation generation...");

        final List<TestCaseDataSet> violatingCases = profile.rules.stream()
            .map(rule ->
            {
                Collection<Rule> violatedRule = profile.rules.stream()
                    .map(r -> r == rule
                        ? violateRule(rule)
                        : r)
                    .collect(Collectors.toList());

                Profile violatingProfile = new Profile(profile.fields, violatedRule);

                return new TestCaseDataSet(
                    rule.description,
                    generate(
                        violatingProfile,
                        config));
            })
            .collect(Collectors.toList());


        final TestCaseGenerationResult generationResult = new TestCaseGenerationResult(
            profile,
            Stream.concat(
                Stream.of(validCase),
                violatingCases.stream())
                .collect(Collectors.toList()));

        this.outputter.outputTestCases(generationResult);
    }

    private Stream<GeneratedObject> generate(Profile profile, GenerationConfig config) {

        final DecisionTreeCollection analysedProfile = this.profileAnalyser.analyse(profile);

        return this.dataGenerator.generateData(
            profile,
            analysedProfile.getMergedTree(),
            config);
    }

    private Rule violateRule(Rule rule) {
        IConstraint violateConstraint =
            rule.constraints.size() == 1
                ? new ViolateConstraint(
                rule.constraints.iterator().next())
                : new ViolateConstraint(
                new AndConstraint(
                    rule.constraints));

        return new Rule(rule.description, Collections.singleton(violateConstraint));
    }
}
