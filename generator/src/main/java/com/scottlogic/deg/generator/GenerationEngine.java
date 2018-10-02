package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.AndConstraint;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeGenerator;
import com.scottlogic.deg.generator.constraints.ViolateConstraint;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.IDataSetOutputter;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerationEngine {
    private final IDecisionTreeGenerator profileAnalyser = new DecisionTreeGenerator();
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
    private final IDataGenerator dataGenerator = new DataGenerator(
        new RowSpecMerger(fieldSpecMerger),
        new ConstraintReducer(
            new FieldSpecFactory(),
            fieldSpecMerger));

    private final IDataSetOutputter outputter;

    public GenerationEngine(IDataSetOutputter outputter) {
        this.outputter = outputter;
    }

    public void generateDataSet(String profileFilePath, GenerationConfig config) {
        final Profile profile;

        try {
            profile = new ProfileReader().read(Paths.get(profileFilePath));
        } catch (Exception e) {
            System.err.println("Failed to read file!");
            System.err.println(e.toString());
            for (StackTraceElement ste : e.getStackTrace())
                System.err.println(ste.toString());
            return;
        }

        final TestCaseDataSet validCase = generate(profile, config, "");


        final TestCaseGenerationResult generationResult = new TestCaseGenerationResult(
            profile,
            Collections.singleton(validCase));

        try {
            this.outputter.output(generationResult);
        } catch (Exception e) {
            System.err.println("Failed to write generation result");
        }
    }

    public void generateTestCases(String profileFilePath, GenerationConfig config) {
        final Profile profile;

        try {
            profile = new ProfileReader().read(Paths.get(profileFilePath));
        } catch (Exception e) {
            System.err.println("Failed to read file!");
            System.err.println(e.toString());
            for (StackTraceElement ste : e.getStackTrace())
                System.err.println(ste.toString());
            return;
        }

        final TestCaseDataSet validCase = generate(profile, config, "");

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

                return generate(
                    violatingProfile,
                    config,
                    rule.description);
            })
            .collect(Collectors.toList());


        final TestCaseGenerationResult generationResult = new TestCaseGenerationResult(
            profile,
            Stream.concat(
                Stream.of(validCase),
                violatingCases.stream())
                .collect(Collectors.toList()));

        try {
            this.outputter.output(generationResult);
        } catch (Exception e) {
            System.err.println("Failed to write generation result");
            System.err.println(e.toString());
            for (StackTraceElement ste : e.getStackTrace())
                System.err.println(ste.toString());
        }
    }

    private TestCaseDataSet generate(Profile profile, GenerationConfig config, String violationDescription) {

        final DecisionTreeCollection analysedProfile = this.profileAnalyser.analyse(profile);

        final Iterable<TestCaseDataRow> validRows = this.dataGenerator.generateData(
            profile,
            analysedProfile.getMergedTree(),
            config);

        return new TestCaseDataSet(violationDescription, validRows);
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

