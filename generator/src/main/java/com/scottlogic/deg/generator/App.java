package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.AndConstraint;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResultWriter;
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

public class App {
    public static void main(String[] args) {
        final String profileFilePath = args[0];
        final String directoryFilePath = args[1];

        new GenerationEngine().generateTestCases(profileFilePath, directoryFilePath);
    }
}

class GenerationEngine {
    private final IDecisionTreeGenerator decisionTreeGenerator = new DecisionTreeGenerator();
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
    private final IDataGenerator dataGenerator = new DataGenerator(
            new RowSpecMerger(fieldSpecMerger),
            new ConstraintReducer(
                    new FieldSpecFactory(),
                    fieldSpecMerger));

    void generateTestCases(String profileFilePath, String directoryFilePath) {
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

        final List<TestCaseDataSet> violatingCases = profile.rules.stream()
            .map(rule ->
                generate(
                    profile,
                    profile.rules.stream()
                        .map(r -> r == rule
                            ? violateRule(rule)
                            : r)
                        .collect(Collectors.toList()),
                    "violatesfdsfsdfs"))
            .collect(Collectors.toList());


        final TestCaseGenerationResult generationResult = new TestCaseGenerationResult(
            profile,
            Stream.concat(
                    Stream.of(validCase),
                    violatingCases.stream())
                .collect(Collectors.toList()));

        try {
            TestCaseGenerationResultWriter writer = new TestCaseGenerationResultWriter();

            writer.writeToDirectory(
                generationResult,
                Paths.get(directoryFilePath).toAbsolutePath().normalize());
        }
        catch (Exception e) {
            System.err.println("Failed to write generation result");
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
