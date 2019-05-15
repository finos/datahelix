package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;
import com.scottlogic.deg.generator.outputs.formats.OutputFormat;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOutputTarget implements SingleDatasetOutputTarget {
    private final Path filePath;
    private final FileUtils fileUtils;
    private final boolean canOverwriteExistingFiles;
    private final OutputFormat outputFormat;

    public FileOutputTarget(
        Path filePath,
        OutputFormat outputFormat,
        boolean canOverwriteExistingFiles,
        FileUtils fileUtils) {

        this.fileUtils = fileUtils;
        this.canOverwriteExistingFiles = canOverwriteExistingFiles;
        this.outputFormat = outputFormat;

        Path directoryPath = filePath.getParent();
        if (directoryPath == null) {
            directoryPath = Paths.get(System.getProperty("user.dir"));
        }

        this.filePath = directoryPath.resolve(filePath.getFileName());
    }

    @Override
    public DataSetWriter openWriter(ProfileFields fields) throws IOException {
        final OutputStream stream = new FileOutputStream(
            this.filePath.toFile(),
            false);

        try {
            return outputFormat.createWriter(stream, fields);
        } catch (Exception e) {
            stream.close();

            throw e;
        }
    }

    @Override
    public void validate(Profile profile) throws OutputTargetValidationException, IOException {
        if (fileUtils.isDirectory(filePath)) {
            throw new OutputTargetValidationException(
                "target is a directory, please use a different output filename");
        } else if (!canOverwriteExistingFiles && fileUtils.exists(filePath)) {
            throw new OutputTargetValidationException(
                "file already exists, please use a different output filename or use the --replace option");
        } else if (!fileUtils.exists(filePath)) {
            Path parent = filePath.toAbsolutePath().getParent();
            if (!fileUtils.createDirectories(parent)) {
                throw new OutputTargetValidationException(
                    "parent directory of output file already exists but is not a directory, please use a different output filename");
            }
        }
    }
}
