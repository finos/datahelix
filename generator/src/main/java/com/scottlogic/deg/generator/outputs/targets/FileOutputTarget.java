package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;
import com.scottlogic.deg.generator.outputs.formats.OutputFormat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOutputTarget implements SingleDatasetOutputTarget {
    private final Path filePath;
    private final OutputFormat outputFormat;

    public FileOutputTarget(
        Path filePath,
        OutputFormat outputFormat) {

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
}
