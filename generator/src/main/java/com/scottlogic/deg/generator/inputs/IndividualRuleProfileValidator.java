package com.scottlogic.deg.generator.inputs;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.violations.ViolatedProfile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines a profile violator which violates by violating each ruleInformation independently.
 * Within each violated ruleInformation we violate each constraint independently. This is consistent with the current
 * implementation of violation.
 */
public class IndividualRuleProfileValidator implements ProfileViolator {

    private final ManifestWriter manifestWriter;
    private final Path outputPath;
    private final RuleViolator ruleViolator;

    @Inject
    public IndividualRuleProfileValidator(ManifestWriter manifestWriter,
                                          @Named("outputPath") Path outputPath,
                                          RuleViolator ruleViolator) {
        this.manifestWriter = manifestWriter;
        this.outputPath = outputPath;
        this.ruleViolator = ruleViolator;
    }

    @Override
    public List<Profile> violate(Profile profile) {
        ArrayList<ViolatedProfile> violatedProfiles = new ArrayList<>();

        // Foreach rule in the profile generate a profile with this one rule violated
        for (Rule rule : profile.rules) {
            violatedProfiles.add(violateRuleOnProfile(profile, rule));
        }

        try {
            manifestWriter.writeManifest(violatedProfiles, outputPath);
        }
        catch (Exception e){
            System.out.println("Failed to write manifest for violated profiles, continuing...");
        }

        // The following will allow the conversion to a list of profiles from a list of violated profiles.
        return new ArrayList<>(violatedProfiles);
    }

    /**
     * Given a profile and the rule to violate on that profile produce a profile with this one rule violated.
     *
     * @param profile      Input un-violated profile
     * @param violatedRule Rule to violate
     * @return New profile with the specified rule violated.
     */
    private ViolatedProfile violateRuleOnProfile(Profile profile, Rule violatedRule) {
        Collection<Rule> newRules = profile.rules.stream()
            .map(r -> r == violatedRule
                ? ruleViolator.violateRule(violatedRule)
                : r)
            .collect(Collectors.toList());

        return new ViolatedProfile(
            violatedRule,
            profile.fields,
            newRules,
            String.format("%s -- Violating: %s", profile.description, violatedRule.ruleInformation.getDescription()));
    }
}
