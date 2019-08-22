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

package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagStream;

import java.util.Optional;
import java.util.stream.Stream;

public class RandomReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveDecisionTreeWalker underlyingWalker;
    private final DataGeneratorMonitor monitor;

    @Inject
    RandomReductiveDecisionTreeWalker(ReductiveDecisionTreeWalker underlyingWalker, DataGeneratorMonitor monitor) {
        this.underlyingWalker = underlyingWalker;
        this.monitor = monitor;
    }

    @Override
    public DataBagStream walk(DecisionTree tree) {
        Optional<DataBag> firstRowSpecOpt = getFirstRowSpecFromRandomisingIteration(tree);
        //noinspection OptionalIsPresent
        if (!firstRowSpecOpt.isPresent()) {
            return new DataBagStream(Stream.empty(), false);
        }

        return new DataBagStream(Stream.concat(
            Stream.of(firstRowSpecOpt.get()),
            Stream.generate(() ->
                getFirstRowSpecFromRandomisingIteration(tree))
                    .filter(Optional::isPresent)
                    .map(Optional::get)), false);
    }

    private Optional<DataBag> getFirstRowSpecFromRandomisingIteration(DecisionTree tree) {
        try {
            return underlyingWalker.walk(tree).stream().findFirst();
        } catch (RetryLimitReachedException ex) {
            monitor.addLineToPrintAtEndOfGeneration("");
            monitor.addLineToPrintAtEndOfGeneration("The retry limit for generating data has been hit.");
            monitor.addLineToPrintAtEndOfGeneration("This may mean that a lot or all of the profile is contradictory.");
            monitor.addLineToPrintAtEndOfGeneration("Either fix the profile, or try running the same command again.");
            return Optional.empty();
        }

    }
}
