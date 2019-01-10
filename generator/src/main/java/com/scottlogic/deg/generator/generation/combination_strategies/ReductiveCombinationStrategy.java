package com.scottlogic.deg.generator.generation.combination_strategies;

import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.utils.RestartableIterator;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ReductiveCombinationStrategy implements CombinationStrategy {
    @Override
    public Stream<DataBag> permute(Stream<Stream<DataBag>> dataBagSequences) {
        List<RestartableIterator<DataBag>> bagsAsLists = dataBagSequences
            .map(dbs -> new RestartableIterator<>(dbs.iterator()))
            .collect(Collectors.toList());

        return next(DataBag.empty, bagsAsLists, 0);
    }

    public Stream<DataBag> next(DataBag accumulatingBag, List<RestartableIterator<DataBag>> bagSequences, int bagSequenceIndex) {
        if (bagSequenceIndex < bagSequences.size()) {
            RestartableIterator<DataBag> nextStream = bagSequences.get(bagSequenceIndex);
            nextStream.restart();

            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(nextStream, Spliterator.ORDERED),false)
                .map(innerBag -> DataBag.merge(innerBag, accumulatingBag))
                .flatMap(innerBag -> next(innerBag, bagSequences, bagSequenceIndex + 1));
        }
        else
            return Stream.of(accumulatingBag);
    }
}
