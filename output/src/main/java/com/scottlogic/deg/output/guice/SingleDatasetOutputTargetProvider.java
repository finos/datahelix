package com.scottlogic.deg.output.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.output.outputtarget.FileOutputTarget;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;

public class SingleDatasetOutputTargetProvider implements Provider<SingleDatasetOutputTarget> {
    private final FileOutputTarget fileOutputTarget;

    @Inject
    SingleDatasetOutputTargetProvider(
        FileOutputTarget fileOutputTarget) {
        this.fileOutputTarget = fileOutputTarget;
    }

    @Override
    public SingleDatasetOutputTarget get() {
        return fileOutputTarget;
    }
}
