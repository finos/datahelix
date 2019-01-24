package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ViolateConstraint;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InvalidGenerationEngine implements GenerationEngine {
    private final GenerationEngine generationEngine;
    private final Path outputPath;
    private final ManifestWriter manifestWriter;

    @Inject
    public InvalidGenerationEngine(@Named("outputPath") Path outputPath, @Named("valid") GenerationEngine generationEngine, ManifestWriter manifestWriter){
        this.outputPath = outputPath;
        this.generationEngine = generationEngine;
        this.manifestWriter = manifestWriter;
    }

    public void generateDataSet(Profile profile, GenerationConfig config, FileOutputTarget fileOutputTarget) throws IOException {
        final List<ViolatedProfile> violatedProfiles = profile.rules
            .stream()
            .map(rule -> getViolationForRuleTestCaseDataSet(profile, config, rule))
            .collect(Collectors.toList());

        if (violatedProfiles.isEmpty()) { return; }

        int filename = 1;
        DecimalFormat intFormatter = getDecimalFormat(violatedProfiles.size());
        try {
            manifestWriter.writeManifest(violatedProfiles, outputPath, intFormatter, filename);
        }
        catch (Exception e){}

        for (ViolatedProfile violated: violatedProfiles) {
            generationEngine.generateDataSet(violated, config,
                fileOutputTarget.withFilename(intFormatter.format(filename)));
            filename++;
        }


    }

    private ViolatedProfile getViolationForRuleTestCaseDataSet(Profile profile, GenerationConfig config, Rule violatedRule) {
        Collection<Rule> newRules = profile.rules.stream()
            .map(r -> r == violatedRule
                ? violateRule(violatedRule)
                : r)
            .collect(Collectors.toList());

        return new ViolatedProfile(
            violatedRule,
            profile.fields,
            newRules,
            String.format("%s -- Violating: %s", profile.description, violatedRule.rule.getDescription()));

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

    private static DecimalFormat getDecimalFormat(int numberOfDatasets)
    {
        int maxNumberOfDigits = (int)Math.ceil(Math.log10(numberOfDatasets));

        char[] zeroes = new char[maxNumberOfDigits];
        Arrays.fill(zeroes, '0');

        return new DecimalFormat(new String(zeroes));
    }
}
