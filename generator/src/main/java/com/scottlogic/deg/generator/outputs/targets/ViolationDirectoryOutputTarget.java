package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.generator.outputs.formats.OutputFormat;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.nio.file.Path;

/** Represents a directory specified by a user as a target for violation data */
public class ViolationDirectoryOutputTarget implements MultiDatasetOutputTarget {
    private final Path directoryPath;
    private final OutputFormat formatOfViolationDatasets;
    private final FileUtils fileUtils;

    public ViolationDirectoryOutputTarget(
        Path directoryPath,
        OutputFormat formatOfViolationDatasets,
        FileUtils fileUtils) {

        this.directoryPath = directoryPath;
        this.formatOfViolationDatasets = formatOfViolationDatasets;
        this.fileUtils = fileUtils;
    }

    @Override
    public SingleDatasetOutputTarget getSubTarget(String name) {
        String filename =
            formatOfViolationDatasets.getFileExtensionWithoutDot()
                .map(extension -> name + "." + extension)
                .orElse(name);

        return fileUtils.createFileTarget(
            directoryPath.resolve(filename),
            formatOfViolationDatasets);
    }
}
