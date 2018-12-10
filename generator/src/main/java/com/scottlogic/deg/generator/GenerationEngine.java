package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ViolateConstraint;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeGenerator;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.targets.IOutputTarget;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerationEngine {
    private final IDecisionTreeGenerator profileAnalyser = new DecisionTreeGenerator();
    private final IDataGenerator dataGenerator;

    private final IOutputTarget outputter;

    public GenerationEngine(IOutputTarget outputter, DataGenerator dataGenerator) {
        this.outputter = outputter;
        this.dataGenerator = dataGenerator;
    }

    void generateDataSet(Profile profile, GenerationConfig config) throws IOException {
        final Stream<GeneratedObject> generatedDataItems = generate(profile, config);

        this.outputter.outputDataset(generatedDataItems, profile.fields);
    }

    public void generateTestCases(Profile profile, GenerationConfig config) throws IOException {
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
        Constraint violateConstraint =
            rule.constraints.size() == 1
                ? new ViolateConstraint(
                rule.constraints.iterator().next())
                : new ViolateConstraint(
                new AndConstraint(
                    rule.constraints));

        return new Rule(rule.description, Collections.singleton(violateConstraint));
    }
}
