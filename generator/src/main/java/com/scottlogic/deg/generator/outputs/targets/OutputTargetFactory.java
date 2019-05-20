package com.scottlogic.deg.generator.outputs.targets;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.outputs.formats.OutputFormat;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

/** Represents a directory specified by a user as a target for violation data */
public class OutputTargetFactory {
    private final FileUtils fileUtils;
    private final Path directoryPath;
    private final boolean canOverwriteExistingFiles;
    private final OutputFormat formatOfViolationDatasets;

    @Inject
    public OutputTargetFactory(
        @Named("config:outputPath") Path directoryPath,
        OutputFormat formatOfViolationDatasets,
        @Named("config:canOverwriteOutputFiles") boolean canOverwriteExistingFiles,
        FileUtils fileUtils) {

        this.fileUtils = fileUtils;
        this.directoryPath = directoryPath;
        this.canOverwriteExistingFiles = canOverwriteExistingFiles;
        this.formatOfViolationDatasets = formatOfViolationDatasets;
    }

    public SingleDatasetOutputTarget create(String name) {
        String filename =
            formatOfViolationDatasets.getFileExtensionWithoutDot()
                .map(extension -> name + "." + extension)
                .orElse(name);

        return new FileOutputTarget(
            directoryPath.resolve(filename),
            formatOfViolationDatasets, canOverwriteExistingFiles, fileUtils
        );
    }
}
