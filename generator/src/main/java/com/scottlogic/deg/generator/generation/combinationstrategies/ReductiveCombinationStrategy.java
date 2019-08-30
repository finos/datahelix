/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.generation.combinationstrategies;

import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.generator.generation.databags.*;
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

            return FlatMappingSpliterator.flatMap(StreamSupport.stream(Spliterators.spliteratorUnknownSize(nextStream, Spliterator.ORDERED),false)
                .map(innerBag -> DataBag.merge(innerBag, accumulatingBag)),
                innerBag -> next(innerBag, bagSequences, bagSequenceIndex + 1));
        }
        else
            return Stream.of(accumulatingBag);
    }
}
