package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.ViolateConstraint;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerationEngine {
    private final DecisionTreeFactory decisionTreeGenerator;
    private final DataGenerator dataGenerator;
    private final OutputTarget outputter;

    public GenerationEngine(
        OutputTarget outputter,
        DataGenerator dataGenerator,
        DecisionTreeFactory decisionTreeGenerator) {
        this.outputter = outputter;
        this.dataGenerator = dataGenerator;
        this.decisionTreeGenerator = decisionTreeGenerator;
    }

    void generateDataSet(Profile profile, GenerationConfig config) throws IOException {
        final Stream<GeneratedObject> generatedDataItems = generate(profile, config);

        this.outputter.outputDataset(generatedDataItems, profile.fields);
    }

    public void generateTestCases(Profile profile, GenerationConfig config) throws IOException {
        final Stream<TestCaseDataSet> violatingCases = profile.rules
            .stream()
            .flatMap(rule -> getViolationForRuleTestCaseDataSet(profile, rule).stream())
            .map(violatedProfile -> new TestCaseDataSet(
                violatedProfile.ruleBeingViolated.rule,
                violatedProfile.constraintBeingViolated,
                () -> generate(
                    violatedProfile,
                    config))
            );

        final TestCaseGenerationResult generationResult = new TestCaseGenerationResult(
            profile,
            violatingCases.collect(Collectors.toList()));

        this.outputter.outputTestCases(generationResult);
    }

    private Collection<ViolatedProfile> getViolationForRuleTestCaseDataSet(Profile profile, Rule rule) {
        return rule.constraints.stream()
            .map(constraintToViolate -> {
                Profile violatedProfile = new Profile(
                    profile.fields,
                    profile.rules.stream()
                        .map(r -> r == rule
                            ? violateRule(rule, constraintToViolate)
                            : r)
                        .collect(Collectors.toList()),
                    String.format(
                        "%s -- Violating: %s [%s]",
                        profile.description,
                        rule.rule.getDescription(),
                        constraintToViolate.toString())
                );
                return new ViolatedProfile(violatedProfile, rule, constraintToViolate);
            })
            .collect(Collectors.toList());
    }

    private Stream<GeneratedObject> generate(Profile profile, GenerationConfig config) {
        final DecisionTreeCollection analysedProfile = this.decisionTreeGenerator.analyse(profile);

        return this.dataGenerator.generateData(
            profile,
            analysedProfile.getMergedTree(),
            config);
    }

    private Rule violateRule(Rule rule, Constraint constraintToViolate) {
        return new Rule(
            rule.rule,
            rule.constraints
                .stream()
                .map(constraint -> constraint == constraintToViolate
                    ? new ViolateConstraint(constraint)
                    : constraint)
                .collect(Collectors.toList()));
    }

    class ViolatedProfile extends Profile {
        final Rule ruleBeingViolated;
        final Constraint constraintBeingViolated;

        ViolatedProfile(Profile profile, Rule ruleBeingViolated, Constraint constraintBeingViolated){
            super(profile.fields, profile.rules, profile.description);
            this.ruleBeingViolated = ruleBeingViolated;
            this.constraintBeingViolated = constraintBeingViolated;
        }
    }
}
