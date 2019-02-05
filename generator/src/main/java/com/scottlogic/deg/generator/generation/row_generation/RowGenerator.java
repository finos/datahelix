package com.scottlogic.deg.generator.generation.row_generation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.util.stream.Stream;

public interface RowGenerator {
    Stream<GeneratedObject> generateObjectsFromRowSpecs(Profile profile, Stream<RowSpec> rowSpecs);
}
