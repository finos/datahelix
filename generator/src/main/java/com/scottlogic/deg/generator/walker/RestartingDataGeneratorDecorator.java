package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Calls the inner generator once, then restarts it
 * Can then repeatedly call the inner generator
 * This is used to reset the random mode for reductive
 */
public class RestartingDataGeneratorDecorator implements DataGenerator {
    private final DataGenerator innerGenerator;

    public RestartingDataGeneratorDecorator(DataGenerator innerGenerator) {
        this.innerGenerator = innerGenerator;
    }

    @Override
    public Stream<GeneratedObject> generateData(Profile profile, DecisionTree analysedProfile) {
        Optional<GeneratedObject> firstGeneratedObject = getFirstGeneratedObjectFromIteration(profile, analysedProfile);
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
        return innerGenerator.generateData(profile, analysedProfile)
            .findFirst();
    }

}
