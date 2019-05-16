package com.scottlogic.deg.generator.violations;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.StandardGenerationEngine;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.profileviolation.ProfileViolator;
import com.scottlogic.deg.generator.outputs.targets.MultiDatasetOutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class ViolationGenerationEngine {
    private final StandardGenerationEngine generationEngine;
    private final ProfileViolator profileViolator;

    @Inject
    public ViolationGenerationEngine(
        ProfileViolator profileViolator,
        StandardGenerationEngine generationEngine) {

        this.profileViolator = profileViolator;
        this.generationEngine = generationEngine;
    }

    public void generateDataSet(
        Profile profile,
        MultiDatasetOutputTarget outputTarget)
        throws IOException {

        List<Profile> violatedProfiles = this.profileViolator.violate(profile);

        if (violatedProfiles.isEmpty()) {
            return;
        }

        DecimalFormat intFormatter = FileUtils.getDecimalFormat(violatedProfiles.size());

        int filename = 1;
        for (Profile violatedProfile : violatedProfiles) {
            generationEngine.generateDataSet(
                violatedProfile,
                outputTarget.getSubTarget(
                    intFormatter.format(filename))
            );

            filename++;
        }
    }
}
