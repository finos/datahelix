package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ExhaustiveCombinationStrategy implements CombinationStrategy {

    @Override
    public Stream<GeneratedObject> permute(Stream<Stream<GeneratedObject>> dataBagSequences) {

        List<List<GeneratedObject>> bagsAsLists = dataBagSequences
            .map(sequence ->
                StreamSupport.stream(sequence.spliterator(), false)
                    .collect(Collectors.toList()))
            .collect(Collectors.toList());

        return next(GeneratedObject.empty, bagsAsLists, 0);
    }

    public Stream<GeneratedObject> next(GeneratedObject accumulatingBag, List<List<GeneratedObject>> bagSequences, int bagSequenceIndex) {
        if (bagSequenceIndex < bagSequences.size()) {
            List<GeneratedObject> nextStream = bagSequences.get(bagSequenceIndex);

            return FlatMappingSpliterator.flatMap(nextStream
                .stream()
                .map(innerBag -> GeneratedObject.merge(innerBag, accumulatingBag)),
                innerBag -> next(innerBag, bagSequences, bagSequenceIndex + 1));
        }
        else
            return Stream.of(accumulatingBag);
    }
}
