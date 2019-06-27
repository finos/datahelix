package com.scottlogic.deg.output.outputtarget;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.output.FileUtils;
import com.scottlogic.deg.output.OutputPath;
import com.scottlogic.deg.output.writer.OutputWriterFactory;

import java.nio.file.Path;

/** Represents a directory specified by a user as a target for violation data */
public class OutputTargetFactory {
    private final Path directoryPath;
    private final boolean canOverwriteExistingFiles;
    private final OutputWriterFactory formatOfViolationDatasets;
    private final FileUtils fileUtils;

    @Inject
    public OutputTargetFactory(
        OutputPath directoryPath,
        OutputWriterFactory formatOfViolationDatasets,
        @Named("config:canOverwriteOutputFiles") boolean canOverwriteExistingFiles,
        FileUtils fileUtils) {
        this.directoryPath = directoryPath.getPath();
        this.canOverwriteExistingFiles = canOverwriteExistingFiles;
        this.formatOfViolationDatasets = formatOfViolationDatasets;
        this.fileUtils = fileUtils;
    }

    public SingleDatasetOutputTarget create(String name) {
        String filename =
            formatOfViolationDatasets.getFileExtensionWithoutDot()
                .map(extension -> name + "." + extension)
                .orElse(name);

        return new FileOutputTarget(
            new OutputPath(directoryPath.resolve(filename)),
            formatOfViolationDatasets,
            canOverwriteExistingFiles,
            fileUtils);
    }
}
