package com.scottlogic.deg.output.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.scottlogic.deg.output.outputtarget.*;

import java.nio.file.Path;

public class SingleDatasetOutputTargetProvider implements Provider<SingleDatasetOutputTarget> {
    private final FileOutputTarget fileOutputTarget;
    private final TraceFileOutputTarget traceFileOutputTarget;
    private final StdoutOutputTarget stdoutOutputTarget;
    private final OutputConfigSource config;

    @Inject
    SingleDatasetOutputTargetProvider(
        FileOutputTarget fileOutputTarget,
        TraceFileOutputTarget traceFileOutputTarget,
        StdoutOutputTarget stdoutOutputTarget,
        OutputConfigSource outputConfigSource) {
        this.fileOutputTarget = fileOutputTarget;
        this.traceFileOutputTarget = traceFileOutputTarget;
        this.stdoutOutputTarget = stdoutOutputTarget;
        this.config = outputConfigSource;
    }

    @Override
    public SingleDatasetOutputTarget get() {
        if (config.useStdOut()){
            return stdoutOutputTarget;
        }

        if (config.isEnableTracing()) {
            return new SplittingOutputTarget(fileOutputTarget, traceFileOutputTarget);
        } else {
            return fileOutputTarget;
        }
    }
}
