package com.scottlogic.deg.generator.visualisation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.inputs.validation.NoopProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;

public class VisualisationConfig {

    @Inject
    public VisualisationConfig(VisualisationConfigSource source) {
    }

    public ProfileValidator getProfileValidator() {
        return new NoopProfileValidator();
    }

}
