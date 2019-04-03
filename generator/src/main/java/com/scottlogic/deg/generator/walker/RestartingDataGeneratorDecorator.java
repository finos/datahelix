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
    public Stream<GeneratedObject> generateData(Profile profile, DecisionTree analysedProfile) {
        Optional<GeneratedObject> firstGeneratedObject = getFirstGeneratedObjectFromIteration(profile, analysedProfile);
        //noinspection OptionalIsPresent
        if (!firstGeneratedObject.isPresent()) {
            return Stream.empty();
        }

        return Stream.concat(
            Stream.of(firstGeneratedObject.get()),
            Stream.generate(() ->
                getFirstGeneratedObjectFromIteration(profile, analysedProfile))
                .filter(Optional::isPresent)
                .map(Optional::get));
    }

    private Optional<GeneratedObject> getFirstGeneratedObjectFromIteration(Profile profile, DecisionTree analysedProfile){
        return underlyingWalker.generateData(profile, analysedProfile)
            .findFirst();
    }

}
