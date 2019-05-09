package com.scottlogic.deg.generator.outputs.targets;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.datasetwriters.DataSetWriter;
import com.scottlogic.deg.generator.outputs.datasetwriters.RowOutputFormatter;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileOutputTarget implements OutputTarget{
    private final Path filePath;
    private final DataSetWriter dataSetWriter;
    private final RowOutputFormatter formatter;

    @Inject
    public FileOutputTarget(@Named("outputPath") Path filePath, DataSetWriter dataSetWriter, RowOutputFormatter formatter) {
        this.filePath = filePath;
        this.dataSetWriter = dataSetWriter;
        this.formatter = formatter;
    }

    @Override
    public void outputDataset(
        Stream<GeneratedObject> generatedObjects,
        ProfileFields profileFields)
        throws IOException {

        Path directoryPath = this.filePath.getParent();
        if (directoryPath == null) {
            directoryPath = Paths.get(System.getProperty("user.dir"));
        }

        String fileNameWithoutExtension = this.filePath.getFileName().toString().replaceAll("\\.[^.]+$", "");
        String fileName = this.dataSetWriter.getFileName(fileNameWithoutExtension);

        try (Closeable writer = this.dataSetWriter.openWriter(directoryPath, fileName, profileFields)) {
            generatedObjects.forEach(row -> {
                try {
                    this.dataSetWriter.writeRow(writer, row, formatter);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    public FileOutputTarget withFilename(String filename){
        return new FileOutputTarget(
            filePath.resolve(dataSetWriter.getFileName(filename)),
            dataSetWriter,
            formatter);
    }

    public Path getFilePath() {
        return filePath;
    }
}
