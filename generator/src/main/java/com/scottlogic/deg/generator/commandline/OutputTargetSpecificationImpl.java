package com.scottlogic.deg.generator.commandline;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.outputs.formats.OutputFormat;
import com.scottlogic.deg.generator.outputs.formats.trace.TraceOutputFormat;
import com.scottlogic.deg.generator.outputs.targets.*;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.nio.file.Path;

public class OutputTargetSpecificationImpl implements OutputTargetSpecification {
    private final boolean canOverwriteOutputFiles;
    private final boolean tracingIsEnabled;
    private final OutputFormat outputFormat;
    private final FileUtils fileUtils;
    private final Path filePath;

    @Inject
    OutputTargetSpecificationImpl(
        @Named("config:outputPath") Path filePath,
        OutputFormat outputFormat,
        @Named("config:canOverwriteOutputFiles") boolean canOverwriteOutputFiles,
        @Named("config:tracingIsEnabled") boolean tracingIsEnabled,
        FileUtils fileUtils) {

        this.filePath = filePath;
        this.outputFormat = outputFormat;
        this.canOverwriteOutputFiles = canOverwriteOutputFiles;
        this.tracingIsEnabled = tracingIsEnabled;
        this.fileUtils = fileUtils;
    }

    @Override
    public SingleDatasetOutputTarget asFilePath() {
        SingleDatasetOutputTarget mainOutputTarget = new FileOutputTarget(
            filePath,
            outputFormat,
            canOverwriteOutputFiles,
            fileUtils);

        if (tracingIsEnabled) {
            return new SplittingOutputTarget(
                mainOutputTarget,
                new FileOutputTarget(
                    FileUtils.addFilenameSuffix(
                        FileUtils.replaceExtension(
                            filePath,
                            "json"),
                        "-trace"),
                    new TraceOutputFormat(),
                    canOverwriteOutputFiles,
                    fileUtils
                ));
        } else {
            return mainOutputTarget;
        }
    }

    @Override
    public MultiDatasetOutputTarget asViolationDirectory() {
        return new ViolationDirectoryOutputTarget(
            filePath,
            outputFormat,
            canOverwriteOutputFiles,
            fileUtils);
    }
}
