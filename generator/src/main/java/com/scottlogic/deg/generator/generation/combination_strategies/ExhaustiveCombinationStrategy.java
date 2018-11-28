package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.generation.databags.DataBag;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ExhaustiveCombinationStrategy implements ICombinationStrategy {

    @Override
    public Stream<DataBag> permute(Stream<Stream<DataBag>> dataBagSequences) {

        List<Stream<DataBag>> bagsAsLists = dataBagSequences
            .collect(Collectors.toList());

        return next(DataBag.empty, bagsAsLists, 0);
    }

    public Stream<DataBag> next(DataBag accumulatingBag, List<Stream<DataBag>> bagSequences, int bagSequenceIndex) {
        if (bagSequenceIndex < bagSequences.size()) {
            Stream<DataBag> nextStream = bagSequences.get(bagSequenceIndex);

            return nextStream
                .map(innerBag -> DataBag.merge(innerBag, accumulatingBag))
                .flatMap(innerBag -> next(innerBag, bagSequences, bagSequenceIndex + 1));
        }
        else
            return Stream.of(accumulatingBag);
    }
}
