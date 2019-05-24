package com.scottlogic.deg.output.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.scottlogic.deg.output.OutputPath;
import com.scottlogic.deg.output.outputtarget.*;

import java.nio.file.Path;

public class SingleDatasetOutputTargetProvider implements Provider<SingleDatasetOutputTarget> {
    private final FileOutputTarget fileOutputTarget;
    private final TraceFileOutputTarget traceFileOutputTarget;
    private final StdoutOutputTarget stdoutOutputTarget;
    private final Path outputPath;
    private final OutputConfigSource outputConfigSource;

    @Inject
    SingleDatasetOutputTargetProvider(
        FileOutputTarget fileOutputTarget,
        TraceFileOutputTarget traceFileOutputTarget,
        StdoutOutputTarget stdoutOutputTarget,
        OutputPath outputPath,
        OutputConfigSource outputConfigSource) {
        this.fileOutputTarget = fileOutputTarget;
        this.traceFileOutputTarget = traceFileOutputTarget;
        this.stdoutOutputTarget = stdoutOutputTarget;
        this.outputPath = outputPath.get();
        this.outputConfigSource = outputConfigSource;
    }

    @Override
    public SingleDatasetOutputTarget get() {
        if (outputConfigSource.useStdOut()){
            return stdoutOutputTarget;
        }

        if (outputConfigSource.isEnableTracing()) {
            return new SplittingOutputTarget(fileOutputTarget, traceFileOutputTarget);
        } else {
            return fileOutputTarget;
        }
    }
}
