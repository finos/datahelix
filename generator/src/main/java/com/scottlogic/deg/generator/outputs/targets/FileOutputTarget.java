package com.scottlogic.deg.generator.outputs.targets;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.dataset_writers.DataSetWriter;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileOutputTarget implements OutputTarget {
    private final Path filePath;
    private final DataSetWriter dataSetWriter;

    @Inject
    public FileOutputTarget(@Named("outputPath") Path filePath, DataSetWriter dataSetWriter) {
        this.filePath = filePath;
        this.dataSetWriter = dataSetWriter;
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
                    this.dataSetWriter.writeRow(writer, row);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public FileOutputTarget withFilename(String filename) {
        return new FileOutputTarget(
            filePath.resolve(dataSetWriter.getFileName(filename)),
            dataSetWriter);
    }

    public boolean exists() {
        return Files.exists(filePath);
    }

    public boolean isDirectory() {
        return Files.isDirectory(filePath);
    }

    @Override
    public boolean isDirectoryEmpty(int fileCount) {
        try {
            if (directoryContainsManifestJsonFile() || directoryContainsFilesWithExt("csv", fileCount)) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean directoryContainsManifestJsonFile() {
        return Files.exists(Paths.get(filePath.toString(), "manifest.json"));
    }

    private boolean directoryContainsFilesWithExt(String ext, int fileCount) throws IOException {
        for (int x = 0; x <= fileCount; x++) {
            final int fileNbr = x;
            if (Files.exists( Paths.get(filePath.toString(), fileNbr + "." + ext))) {
                return true;
            }
        }
        return false;
    }
}
