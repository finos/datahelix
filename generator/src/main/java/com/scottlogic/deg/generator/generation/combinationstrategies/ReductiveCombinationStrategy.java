package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.generation.rows.Row;
import com.scottlogic.deg.generator.generation.rows.RowMerger;
import com.scottlogic.deg.generator.utils.RestartableIterator;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ReductiveCombinationStrategy implements CombinationStrategy {
    @Override
    public Stream<Row> permute(Stream<Stream<Row>> rowSequences) {
        List<RestartableIterator<Row>> bagsAsLists = rowSequences
            .map(dbs -> new RestartableIterator<>(dbs.iterator()))
            .collect(Collectors.toList());

        return next(Row.empty, bagsAsLists, 0);
    }

    public Stream<Row> next(Row accumulatingBag, List<RestartableIterator<Row>> bagSequences, int bagSequenceIndex) {
        if (bagSequenceIndex < bagSequences.size()) {
            RestartableIterator<Row> nextStream = bagSequences.get(bagSequenceIndex);
            nextStream.restart();

            return FlatMappingSpliterator.flatMap(StreamSupport.stream(Spliterators.spliteratorUnknownSize(nextStream, Spliterator.ORDERED),false)
                .map(innerBag -> RowMerger.merge(innerBag, accumulatingBag)),
                innerBag -> next(innerBag, bagSequences, bagSequenceIndex + 1));
        }
        else
            return Stream.of(accumulatingBag);
    }
}
