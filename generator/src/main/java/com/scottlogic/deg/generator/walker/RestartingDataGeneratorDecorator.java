package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;

import java.util.Optional;
import java.util.stream.Stream;

public class RestartingDataGeneratorDecorator implements DataGenerator {
    private final DataGenerator underlyingWalker;

    public RestartingDataGeneratorDecorator(DataGenerator underlyingWalker) {
        this.underlyingWalker = underlyingWalker;
    }

    @Override
    public Stream<GeneratedObject> generateData(Profile profile, DecisionTree analysedProfile, GenerationConfig generationConfig) {
        Optional<GeneratedObject> firstGeneratedObject = getFirstGeneratedObjectFromIteration(profile, analysedProfile, generationConfig);
        //noinspection OptionalIsPresent
        if (!firstGeneratedObject.isPresent()) {
            return Stream.empty();
        }

        return Stream.concat(
            Stream.of(firstGeneratedObject.get()),
            Stream.generate(() ->
                getFirstGeneratedObjectFromIteration(profile, analysedProfile, generationConfig))
                .filter(Optional::isPresent)
                .map(Optional::get));
    }

    private Optional<GeneratedObject> getFirstGeneratedObjectFromIteration(Profile profile, DecisionTree analysedProfile, GenerationConfig generationConfig){
        return underlyingWalker.generateData(profile, analysedProfile, generationConfig)
            .findFirst();
    }

}
