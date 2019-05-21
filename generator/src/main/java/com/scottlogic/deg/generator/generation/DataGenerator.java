package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.output.GeneratedObject;

import java.util.stream.Stream;

public interface DataGenerator {
    Stream<GeneratedObject> generateData(Profile profile);
}
