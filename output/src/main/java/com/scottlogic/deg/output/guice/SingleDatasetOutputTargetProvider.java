package com.scottlogic.deg.output.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.output.outputtarget.FileOutputTarget;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.output.outputtarget.StdoutOutputTarget;

import java.nio.file.Path;

public class SingleDatasetOutputTargetProvider implements Provider<SingleDatasetOutputTarget> {
    private final OutputConfigSource outputConfigSource;
    private final FileOutputTarget fileOutputTarget;
    private final StdoutOutputTarget stdoutOutputTarget;

    @Inject
    SingleDatasetOutputTargetProvider(
        OutputConfigSource outputConfigSource,
        FileOutputTarget fileOutputTarget,
        StdoutOutputTarget stdoutOutputTarget){
        this.outputConfigSource = outputConfigSource;
        this.fileOutputTarget = fileOutputTarget;
        this.stdoutOutputTarget = stdoutOutputTarget;
    }

    @Override
    public SingleDatasetOutputTarget get() {
        if (outputConfigSource.useStdOut()){
            return stdoutOutputTarget;
        }
        return fileOutputTarget;
    }
}
