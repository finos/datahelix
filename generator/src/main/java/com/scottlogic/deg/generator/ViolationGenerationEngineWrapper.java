package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ViolateConstraint;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.targets.DirectoryOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ViolationGenerationEngineWrapper {
    private final GenerationEngine generationEngine;
    private final Path folder;

    @Inject
    public ViolationGenerationEngineWrapper(GenerationEngine generationEngine, FileOutputTarget fileOutputTarget){
        this.generationEngine = generationEngine;
        this.folder = fileOutputTarget.getFolder();
    }

    public void generateTestCases(Profile profile, GenerationConfig config) throws IOException {
        final Stream<Profile> violatingCases = profile.rules
            .stream()
            .map(rule -> getViolationForRuleTestCaseDataSet(profile, config, rule));

        folder.resolve()


    }

    private Profile getViolationForRuleTestCaseDataSet(Profile profile, GenerationConfig config, Rule rule) {
        Collection<Rule> violatedRule = profile.rules.stream()
            .map(r -> r == rule
                ? violateRule(rule)
                : r)
            .collect(Collectors.toList());

        return new Profile(
            profile.fields,
            violatedRule,
            String.format("%s -- Violating: %s", profile.description, rule.rule.getDescription()));

    }


    private Rule violateRule(Rule rule) {
        Constraint constraintToViolate =
            rule.constraints.size() == 1
                ? rule.constraints.iterator().next()
                : new AndConstraint(rule.constraints);

        //This will in effect produce the following constraint: // VIOLATE(AND(X, Y, Z)) reduces to
        //   OR(
        //     AND(VIOLATE(X), Y, Z),
        //     AND(X, VIOLATE(Y), Z),
        //     AND(X, Y, VIOLATE(Z)))
        // See ProfileDecisionTreeFactory.convertConstraint(ViolateConstraint)
        ViolateConstraint violatedConstraint = new ViolateConstraint(constraintToViolate);
        return new Rule(rule.rule, Collections.singleton(violatedConstraint));
    }

}
