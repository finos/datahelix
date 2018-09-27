package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.AndConstraint;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerationEngine {
    private final IDecisionTreeGenerator decisionTreeGenerator = new DecisionTreeGenerator();
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

    public void generateDataSet(String profileFilePath) {
        final Profile profile;

        try {
            profile = new ProfileReader().read(Paths.get(profileFilePath));
        }
        catch(Exception e) {
            System.err.println("Failed to read file!");
            System.err.println(e.toString());
            for (StackTraceElement ste : e.getStackTrace())
                System.err.println(ste.toString());
            return;
        }

        final TestCaseDataSet dataSet = generate(profile, profile.rules, null);


        final TestCaseGenerationResult generationResult = new TestCaseGenerationResult(
            profile,
            Collections.singleton(dataSet));

        try {
            this.outputter.output(generationResult);
        }
        catch (Exception e) {
            System.err.println("Failed to write generation result");
        }
    }

    public void generateTestCases(String profileFilePath) {
        final Profile profile;

        try {
            profile = new ProfileReader().read(Paths.get(profileFilePath));
        }
        catch(Exception e) {
            System.err.println("Failed to read file!");
            System.err.println(e.toString());
            for (StackTraceElement ste : e.getStackTrace())
                System.err.println(ste.toString());
            return;
        }

        final TestCaseDataSet validCase = generate(profile, profile.rules, null);

        System.out.println("Valid cases generated, starting violation generation...");

        final List<TestCaseDataSet> violatingCases = profile.rules.stream()
            .map(rule ->
                generate(
                    profile,
                    profile.rules.stream()
                        .map(r -> r == rule
                            ? violateRule(rule)
                            : r)
                        .collect(Collectors.toList()),
                    rule.description))
            .collect(Collectors.toList());


        final TestCaseGenerationResult generationResult = new TestCaseGenerationResult(
            profile,
            Stream.concat(
                Stream.of(validCase),
                violatingCases.stream())
                .collect(Collectors.toList()));

        try {
            this.outputter.output(generationResult);
        }
        catch (Exception e) {
            System.err.println("Failed to write generation result");
            System.err.println(e.toString());
            for (StackTraceElement ste : e.getStackTrace())
                System.err.println(ste.toString());
        }
    }

    private TestCaseDataSet generate(Profile profile, Collection<Rule> rules, String violationDescription) {
        final Map<Rule, DecisionTree> ruleToDecisionTree =
            rules.stream().collect(
                Collectors.toMap(
                    Function.identity(),
                    this.decisionTreeGenerator::generateTreeFor));

        final ProfileDecisionTreeCollection collection = new ProfileDecisionTreeCollection(
            ruleToDecisionTree);

        final Iterable<TestCaseDataRow> validRows = this.dataGenerator.generateData(
            profile.fields,
            collection);

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


