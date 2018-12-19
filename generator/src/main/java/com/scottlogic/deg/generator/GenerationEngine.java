package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ViolateConstraint;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerationEngine {
    private final OutputTarget outputter;
    private final DataGenerator dataGenerator;

    public GenerationEngine(OutputTarget outputter, DataGenerator dataGenerator) {
        this.outputter = outputter;
        this.dataGenerator = dataGenerator;
    }

    void generateDataSet(Profile profile, GenerationConfig config) throws IOException {
        final Stream<GeneratedObject> generatedDataItems = generate(profile, config);

        this.outputter.outputDataset(generatedDataItems, profile.fields);
    }

    public void generateTestCases(Profile profile, GenerationConfig config) throws IOException {
        final TestCaseDataSet validCase = new TestCaseDataSet(null, generate(profile, config));

        final Stream<TestCaseDataSet> violatingCases = profile.rules
            .stream()
            .map(rule -> getViolationForRuleTestCaseDataSet(profile, config, rule));

        final TestCaseGenerationResult generationResult = new TestCaseGenerationResult(
            profile,
            Stream.concat(Stream.of(validCase), violatingCases)
                .collect(Collectors.toList()));

        this.outputter.outputTestCases(generationResult);
    }

    private Stream<GeneratedObject> generate(Profile profile, GenerationConfig config) {
        return this.dataGenerator.generateData(
            profile,
            config);
    }

    private TestCaseDataSet getViolationForRuleTestCaseDataSet(Profile profile, GenerationConfig config, Rule rule) {
        Collection<Rule> violatedRule = profile.rules.stream()
            .map(r -> r == rule
                ? violateRule(rule)
                : r)
            .collect(Collectors.toList());

        Profile violatingProfile = new Profile(
            profile.fields,
            violatedRule,
            String.format("%s -- Violating: %s", profile.description, rule.rule.getDescription()));

        return new TestCaseDataSet(
            rule.rule,
            generate(
                violatingProfile,
                config));
    }

    private Rule violateRule(Rule rule) {
        Constraint constraintToViolate =
            rule.constraints.size() == 1
                ? rule.constraints.iterator().next()
                : new AndConstraint(rule.constraints);

        ViolateConstraint violatedConstraint = new ViolateConstraint(constraintToViolate);
        return new Rule(rule.rule, Collections.singleton(violatedConstraint));
    }
}
