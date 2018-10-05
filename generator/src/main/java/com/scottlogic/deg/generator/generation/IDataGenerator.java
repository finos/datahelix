package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

public interface IDataGenerator {
    Iterable<GeneratedObject> generateData(Profile profile,
                                           DecisionTree analysedProfile,
                                           GenerationConfig generationConfig);
}
