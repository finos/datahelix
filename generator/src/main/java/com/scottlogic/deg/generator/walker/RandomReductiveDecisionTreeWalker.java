package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;

import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Supplier;
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
        Supplier<Stream<RowSpec>> rowGenerator = () -> underlyingWalker.walk(tree);
        Optional<RowSpec> firstRowSpec = rowGenerator.get().findFirst();
        if (!firstRowSpec.isPresent()) {
            return Stream.empty();
        }

        Stream<Stream<RowSpec>> rowSpecsPerIteration = Stream.concat(
            Stream.of(Stream.of(firstRowSpec.get())),
            Stream.generate(rowGenerator));

        return FlatMappingSpliterator.flatMap(
            rowSpecsPerIteration,
            (rowSpecsInIteration) -> rowSpecsInIteration.limit(1)
        );
    }
}
