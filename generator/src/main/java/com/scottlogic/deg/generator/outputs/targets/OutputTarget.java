package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.rows.Row;

import java.io.IOException;
import java.util.stream.Stream;

public interface OutputTarget {
    void outputDataset(Stream<Row> generatedObjects, ProfileFields profileFields) throws IOException;
}
