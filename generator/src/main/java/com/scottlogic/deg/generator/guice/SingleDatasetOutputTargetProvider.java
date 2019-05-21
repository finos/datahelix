package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.scottlogic.deg.output.writer.OutputWriterFactory;
import com.scottlogic.deg.output.writer.trace.TraceOutputWriterFactory;
import com.scottlogic.deg.generator.outputs.targets.*;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.nio.file.Path;

public class SingleDatasetOutputTargetProvider implements Provider<SingleDatasetOutputTarget> {
    private final boolean canOverwriteOutputFiles;
    private final boolean tracingIsEnabled;
    private final OutputWriterFactory outputWriterFactory;
    private final FileUtils fileUtils;
    private final Path filePath;

    @Inject
    SingleDatasetOutputTargetProvider(
        @Named("config:outputPath") Path filePath,
        OutputWriterFactory outputWriterFactory,
        @Named("config:canOverwriteOutputFiles") boolean canOverwriteOutputFiles,
        @Named("config:tracingIsEnabled") boolean tracingIsEnabled,
        FileUtils fileUtils) {

        this.filePath = filePath;
        this.outputWriterFactory = outputWriterFactory;
        this.canOverwriteOutputFiles = canOverwriteOutputFiles;
        this.tracingIsEnabled = tracingIsEnabled;
        this.fileUtils = fileUtils;
    }

    @Override
    public SingleDatasetOutputTarget get() {
        SingleDatasetOutputTarget mainOutputTarget = new FileOutputTarget(
            filePath,
            outputWriterFactory,
            canOverwriteOutputFiles,
            fileUtils);

        if (tracingIsEnabled) {
            SingleDatasetOutputTarget tracingOutputTarget = new FileOutputTarget(
                FileUtils.addFilenameSuffix(
                    FileUtils.replaceExtension(
                        filePath,
                        "json"),
                    "-trace"),
                new TraceOutputWriterFactory(),
                canOverwriteOutputFiles,
                fileUtils);

            return new SplittingOutputTarget(mainOutputTarget, tracingOutputTarget);
        } else {
            return mainOutputTarget;
        }
    }
}
