package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.generation.rows.Row;
import com.scottlogic.deg.generator.generation.rows.GeneratedObjectMerger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ExhaustiveCombinationStrategy implements CombinationStrategy {

    @Override
    public Stream<Row> permute(Stream<Stream<Row>> dataBagSequences) {

        List<List<Row>> bagsAsLists = dataBagSequences
            .map(sequence ->
                StreamSupport.stream(sequence.spliterator(), false)
                    .collect(Collectors.toList()))
            .collect(Collectors.toList());

        return next(Row.empty, bagsAsLists, 0);
    }

    public Stream<Row> next(Row accumulatingBag, List<List<Row>> bagSequences, int bagSequenceIndex) {
        if (bagSequenceIndex < bagSequences.size()) {
            List<Row> nextStream = bagSequences.get(bagSequenceIndex);

            return FlatMappingSpliterator.flatMap(nextStream
                .stream()
                .map(innerBag -> GeneratedObjectMerger.merge(innerBag, accumulatingBag)),
                innerBag -> next(innerBag, bagSequences, bagSequenceIndex + 1));
        }
        else
            return Stream.of(accumulatingBag);
    }
}
