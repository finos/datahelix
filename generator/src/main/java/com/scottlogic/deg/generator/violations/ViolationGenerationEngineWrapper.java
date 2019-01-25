package com.scottlogic.deg.generator.violations;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ViolateConstraint;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ViolationGenerationEngineWrapper {
    private final GenerationEngine generationEngine;
    private final FileOutputTarget fileOutputTarget;
    private final Path outputPath;
    private final ManifestWriter manifestWriter;
    private final List<ViolationFilter> filters;

    @Inject
    public ViolationGenerationEngineWrapper(@Named("outputPath") Path outputPath, GenerationEngine generationEngine, FileOutputTarget fileOutputTarget, ManifestWriter manifestWriter, List<ViolationFilter> filters){
        this.outputPath = outputPath;
        this.generationEngine = generationEngine;
        this.fileOutputTarget = fileOutputTarget;
        this.manifestWriter = manifestWriter;
        this.filters = filters;
    }

    public void generateTestCases(Profile profile, GenerationConfig config) throws IOException {
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
        List<Constraint> violate = new ArrayList<>();
        List<Constraint> unviolated = new ArrayList<>();
        for (Constraint constraint:rule.constraints) {
            if (acceptConstraint(constraint)){
                violate.add(constraint);
            } else {
              unviolated.add(constraint);
            }
        }
        if (violate.isEmpty()) {
            return rule;
        }

        // The ViolateConstraint wraps the violated constraints.
        // It is used in the tree factory to create the violated tree
        ViolateConstraint violatedConstraint = new ViolateConstraint(
            violate.size() == 1
                ? violate.iterator().next()
                : new AndConstraint(violate));

        unviolated.add(violatedConstraint);
        return new Rule(rule.rule, unviolated);
    }

    private boolean acceptConstraint(Constraint constraint){
        for (ViolationFilter filter : filters) {
            if (!filter.accept(constraint)){
                return false;
            }
        }
        return true;
    }

    private static DecimalFormat getDecimalFormat(int numberOfDatasets)
    {
        int maxNumberOfDigits = (int)Math.ceil(Math.log10(numberOfDatasets));

        char[] zeroes = new char[maxNumberOfDigits];
        Arrays.fill(zeroes, '0');

        return new DecimalFormat(new String(zeroes));
    }
}
