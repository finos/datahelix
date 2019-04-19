package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Calls the inner generator once, then restarts it
 * Can then repeatedly call the inner generator
 * This is used to reset the random mode for reductive
 */
public class GeneratorRestarter {

    @Inject
    public GeneratorRestarter(){}

    public Stream<GeneratedObject> generateAndRestart(Profile profile, DecisionTree analysedProfile, BiFunction<Profile, DecisionTree, Stream<GeneratedObject>> generatorFunction) {
        Optional<GeneratedObject> firstGeneratedObject = getFirstGeneratedObjectFromIteration(profile, analysedProfile, generatorFunction);
        if (!firstGeneratedObject.isPresent()) {
            return Stream.empty();
        }

        return Stream.concat(
            Stream.of(firstGeneratedObject.get()),
            Stream.generate(() ->
                getFirstGeneratedObjectFromIteration(profile, analysedProfile, generatorFunction))
                .filter(Optional::isPresent)
                .map(Optional::get));
    }

    private Optional<GeneratedObject> getFirstGeneratedObjectFromIteration(Profile profile, DecisionTree analysedProfile, BiFunction<Profile, DecisionTree, Stream<GeneratedObject>> generatorFunction){
        return generatorFunction.apply(profile, analysedProfile)
            .findFirst();
    }

}
