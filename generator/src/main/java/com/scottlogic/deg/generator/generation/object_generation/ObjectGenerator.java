package com.scottlogic.deg.generator.generation.object_generation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.util.stream.Stream;

public interface ObjectGenerator {
    Stream<GeneratedObject> generate(Profile profile, Stream<RowSpec> rowSpecs);
}
