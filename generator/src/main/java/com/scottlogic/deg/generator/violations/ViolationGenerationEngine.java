package com.scottlogic.deg.generator.violations;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.StandardGenerationEngine;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.ProfileViolator;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class ViolationGenerationEngine implements GenerationEngine {
    private final GenerationEngine generationEngine;
    private final ProfileViolator profileViolator;

    @Inject
    public ViolationGenerationEngine(ProfileViolator profileViolator,
                                     StandardGenerationEngine generationEngine){
        this.profileViolator = profileViolator;
        this.generationEngine = generationEngine;
    }

    @Override
    public void generateDataSet(Profile profile, GenerationConfig config, OutputTarget outputTarget) throws IOException {
        List<Profile> violatedProfiles = this.profileViolator.violate(profile);

        if (violatedProfiles.isEmpty()) {
            return;
        }

        DecimalFormat intFormatter = FileUtils.getDecimalFormat(violatedProfiles.size());

        int filename = 1;
        for (Profile violatedProfile : violatedProfiles) {
            generationEngine.generateDataSet(violatedProfile, config,
                getOutputTargetWithFilename(outputTarget, intFormatter.format(filename)));
            filename++;
        }
    }

    /**
     * If the output target is a FileOutputTarget sets the appropriate file name, otherwise simply returns the input
     * OutputTarget.
     * @param outputTarget Current output target.
     * @param filename Desired filename.
     * @return Either the file named
     */
    private OutputTarget getOutputTargetWithFilename(OutputTarget outputTarget, String filename) {
        if (outputTarget instanceof FileOutputTarget) {
            return ((FileOutputTarget)outputTarget).withFilename(filename);
        }
        else {
            return outputTarget;
        }
    }
}
