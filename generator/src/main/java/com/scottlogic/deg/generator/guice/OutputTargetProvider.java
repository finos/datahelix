package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.outputs.targets.StreamOutputTarget;

public class OutputTargetProvider implements Provider<OutputTarget> {

    private final GenerationConfigSource configSource;
    private final FileOutputTarget fileOutputTarget;
    private final StreamOutputTarget streamOutputTarget;

    @Inject
    public OutputTargetProvider(GenerationConfigSource configSource,
                                FileOutputTarget fileOutputTarget,
                                StreamOutputTarget streamOutputTarget) {
        this.configSource = configSource;
        this.fileOutputTarget = fileOutputTarget;
        this.streamOutputTarget = streamOutputTarget;
    }

    @Override
    public OutputTarget get() {
        switch (configSource.getOutputDestination()) {
            case FILE:
                return fileOutputTarget;
            case STREAM:
                return streamOutputTarget;
        }
        throw new RuntimeException(String.format("Unknown output destination %s, options are STREAM or FILE", configSource.getOutputDestination()));
    }
}
