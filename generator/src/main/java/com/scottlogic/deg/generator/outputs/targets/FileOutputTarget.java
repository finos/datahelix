package com.scottlogic.deg.generator.outputs.targets;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.Row;
import com.scottlogic.deg.generator.outputs.datasetwriters.DataSetWriter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileOutputTarget implements OutputTarget{
    private final Path filePath;
    private final DataSetWriter dataSetWriter;

    @Inject
    public FileOutputTarget(@Named("outputPath") Path filePath, DataSetWriter dataSetWriter) {
        this.filePath = filePath;
        this.dataSetWriter = dataSetWriter;
    }

    @Override
    public void outputDataset(
        Stream<Row> generatedObjects,
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
                    this.dataSetWriter.writeRow(writer, row, profileFields);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public FileOutputTarget withFilename(String filename){
        return new FileOutputTarget(
            filePath.resolve(dataSetWriter.getFileName(filename)),
            dataSetWriter);
    }

    public Path getFilePath() {
        return filePath;
    }
}
