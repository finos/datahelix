package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;

import java.util.Optional;
import java.util.stream.Stream;

public class RandomReductiveDecisionTreeWalker implements DataGenerator {
    private final ReductiveDataGenerator underlyingWalker;

    @Inject
    RandomReductiveDecisionTreeWalker(ReductiveDataGenerator underlyingWalker) {
        this.underlyingWalker = underlyingWalker;
    }

    @Override
    public Stream<GeneratedObject> generateData(Profile profile, DecisionTree analysedProfile, GenerationConfig generationConfig) {
        Optional<GeneratedObject> firstGeneratedObject = getFirstRowSpecFromRandomisingIteration(profile, analysedProfile, generationConfig);
        //noinspection OptionalIsPresent
        if (!firstGeneratedObject.isPresent()) {
            return Stream.empty();
        }

        return Stream.concat(
            Stream.of(firstGeneratedObject.get()),
            Stream.generate(() ->
                getFirstRowSpecFromRandomisingIteration(profile, analysedProfile, generationConfig))
                .filter(Optional::isPresent)
                .map(Optional::get));
    }

    private Optional<GeneratedObject> getFirstRowSpecFromRandomisingIteration(Profile profile, DecisionTree analysedProfile, GenerationConfig generationConfig){
        return underlyingWalker.generateData(profile, analysedProfile, generationConfig)
            .findFirst();
    }

}
