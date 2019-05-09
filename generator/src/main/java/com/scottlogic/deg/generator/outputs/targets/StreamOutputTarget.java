package com.scottlogic.deg.generator.outputs.targets;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.datasetwriters.RowOutputFormatter;

import java.util.stream.Stream;

public class StreamOutputTarget implements OutputTarget {

    private final RowOutputFormatter formatter;

    @Inject
    public StreamOutputTarget(RowOutputFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) {
        generatedObjects.forEach(row -> System.out.println(formatter.format(row)));
    }
}
