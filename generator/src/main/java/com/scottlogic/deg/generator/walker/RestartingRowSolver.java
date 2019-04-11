package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.RowSolver;
import com.scottlogic.deg.generator.generation.databags.Row;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Calls the inner generator once, then restarts it
 * Can then repeatedly call the inner generator
 * This is used to reset the random mode for reductive
 */
public class RestartingRowSolver implements RowSolver {
    private final RowSolver innerGenerator;

    public RestartingRowSolver(RowSolver innerGenerator) {
        this.innerGenerator = innerGenerator;
    }

    @Override
    public Stream<Row> generateRows(Profile profile, DecisionTree analysedProfile) {
        Optional<Row> firstGeneratedObject = getFirstGeneratedObjectFromIteration(profile, analysedProfile);
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

    private Optional<Row> getFirstGeneratedObjectFromIteration(Profile profile, DecisionTree analysedProfile){
        return innerGenerator.generateRows(profile, analysedProfile)
            .findFirst();
    }

}
