package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;
import com.scottlogic.deg.generator.utils.RestartableIterator;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ReductiveCombinationStrategy implements CombinationStrategy {
    @Override
    public Stream<GeneratedObject> permute(Stream<Stream<GeneratedObject>> dataBagSequences) {
        List<RestartableIterator<GeneratedObject>> bagsAsLists = dataBagSequences
            .map(dbs -> new RestartableIterator<>(dbs.iterator()))
            .collect(Collectors.toList());

        return next(GeneratedObject.empty, bagsAsLists, 0);
    }

    public Stream<GeneratedObject> next(GeneratedObject accumulatingBag, List<RestartableIterator<GeneratedObject>> bagSequences, int bagSequenceIndex) {
        if (bagSequenceIndex < bagSequences.size()) {
            RestartableIterator<GeneratedObject> nextStream = bagSequences.get(bagSequenceIndex);
            nextStream.restart();

            return FlatMappingSpliterator.flatMap(StreamSupport.stream(Spliterators.spliteratorUnknownSize(nextStream, Spliterator.ORDERED),false)
                .map(innerBag -> GeneratedObject.merge(innerBag, accumulatingBag)),
                innerBag -> next(innerBag, bagSequences, bagSequenceIndex + 1));
        }
        else
            return Stream.of(accumulatingBag);
    }
}
