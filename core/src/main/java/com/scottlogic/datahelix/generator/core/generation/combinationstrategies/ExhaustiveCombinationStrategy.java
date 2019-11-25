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

package com.scottlogic.datahelix.generator.core.generation.combinationstrategies;

import com.scottlogic.datahelix.generator.core.generation.databags.DataBag;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.scottlogic.datahelix.generator.common.util.FlatMappingSpliterator.flatMap;

public class ExhaustiveCombinationStrategy implements CombinationStrategy {
    @Override
    public Stream<DataBag> permute(Stream<Supplier<Stream<DataBag>>> dataBagSequences) {
        return flatten(dataBagSequences.iterator()).get();
    }

    public Supplier<Stream<DataBag>> flatten(Iterator<Supplier<Stream<DataBag>>> remainingBags) {
        Supplier<Stream<DataBag>> firstDataBagStream = remainingBags.next();

        if (!remainingBags.hasNext()){
            return firstDataBagStream;
        }

        Supplier<Stream<DataBag>> otherDataBags = flatten(remainingBags);

        return ()-> flatMap(
            firstDataBagStream.get(),
            currentBag ->
                otherDataBags.get()
                    .map(subBag ->
                        DataBag.merge(currentBag, subBag)));

    }
}
