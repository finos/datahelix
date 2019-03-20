package com.scottlogic.deg.generator.outputs.targets;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.datasetwriters.DataSetWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public class VisualiseOutputTarget implements OutputTarget {
    private final Path filePath;

    @Inject
    public VisualiseOutputTarget(@Named("outputPath") Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) {
        throw new UnsupportedOperationException();
    }

    public Path getFilePath() {
        return filePath;
    }
}
