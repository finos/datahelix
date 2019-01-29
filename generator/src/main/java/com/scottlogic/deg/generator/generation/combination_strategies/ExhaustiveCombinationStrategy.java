package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.generation.databags.DataBag;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ExhaustiveCombinationStrategy implements CombinationStrategy {

    @Override
    public Stream<DataBag> permute(Stream<Stream<DataBag>> dataBagSequences) {

        List<List<DataBag>> bagsAsLists = dataBagSequences
            .map(sequence ->
                StreamSupport.stream(sequence.spliterator(), true)
                    .collect(Collectors.toList()))
            .collect(Collectors.toList());

        return next(DataBag.empty, bagsAsLists, 0);
    }

    public Stream<DataBag> next(DataBag accumulatingBag, List<List<DataBag>> bagSequences, int bagSequenceIndex) {
        if (bagSequenceIndex < bagSequences.size()) {
            List<DataBag> nextStream = bagSequences.get(bagSequenceIndex);

            return FlatMappingSpliterator.flatMap(nextStream
                .stream()
                .map(innerBag -> DataBag.merge(innerBag, accumulatingBag)),
                innerBag -> next(innerBag, bagSequences, bagSequenceIndex + 1));
        }
        else
            return Stream.of(accumulatingBag);
    }
}
