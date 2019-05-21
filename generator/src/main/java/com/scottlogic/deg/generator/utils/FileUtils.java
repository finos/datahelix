package com.scottlogic.deg.generator.utils;

import com.scottlogic.deg.generator.outputs.formats.OutputFormat;
import com.scottlogic.deg.generator.outputs.targets.SingleDatasetOutputTarget;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileUtils {
    boolean containsInvalidChars(File file);

    boolean isFileEmpty(File file);

    boolean exists(Path path);

    boolean isFile(Path path);
    boolean isDirectory(Path path);

    boolean isDirectoryEmpty(Path filepath, int fileCount);

    boolean createDirectories(Path dir) throws IOException;

    SingleDatasetOutputTarget createFileTarget(Path destinationPath, OutputFormat outputFormat);
}
