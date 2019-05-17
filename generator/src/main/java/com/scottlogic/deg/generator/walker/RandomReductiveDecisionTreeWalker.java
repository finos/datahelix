package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;

import java.util.Optional;
import java.util.stream.Stream;

public class RandomReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveDecisionTreeWalker underlyingWalker;

    @Inject
    RandomReductiveDecisionTreeWalker(ReductiveDecisionTreeWalker underlyingWalker) {
        this.underlyingWalker = underlyingWalker;
    }

    @Override
    public Stream<RowSpec> walk(DecisionTree tree) {
        Optional<RowSpec> firstRowSpecOpt = getFirstRowSpecFromRandomisingIteration(tree);
        //noinspection OptionalIsPresent
        if (!firstRowSpecOpt.isPresent()) {
            return Stream.empty();
        }

        return Stream.concat(
            Stream.of(firstRowSpecOpt.get()),
            Stream.generate(() ->
                getFirstRowSpecFromRandomisingIteration(tree))
                    .filter(Optional::isPresent)
                    .map(Optional::get));
    }

    private Optional<RowSpec> getFirstRowSpecFromRandomisingIteration(DecisionTree tree){
        return underlyingWalker.walk(tree)
            .findFirst();
    }
}
