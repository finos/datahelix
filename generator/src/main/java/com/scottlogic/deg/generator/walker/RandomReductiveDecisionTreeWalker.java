package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RandomReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveDecisionTreeWalker underlyingWalker;

    @Inject
    public RandomReductiveDecisionTreeWalker(ReductiveDecisionTreeWalker underlyingWalker) {
        this.underlyingWalker = underlyingWalker;
    }

    @Override
    public Stream<RowSpec> walk(DecisionTree tree) {
        Iterator<RowSpec> iterator = new SourceRepeatingIterator<>(
            1,
            () -> underlyingWalker.walk(tree).iterator(),
            true);

        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                iterator,
                Spliterator.ORDERED
            ), false);
    }
}
