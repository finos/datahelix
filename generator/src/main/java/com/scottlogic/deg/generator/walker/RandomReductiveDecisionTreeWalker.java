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
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.databags.DataBag;

import java.util.Optional;
import java.util.stream.Stream;

public class RandomReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveDecisionTreeWalker underlyingWalker;

    @Inject
    RandomReductiveDecisionTreeWalker(ReductiveDecisionTreeWalker underlyingWalker) {
        this.underlyingWalker = underlyingWalker;
    }

    @Override
    public Stream<DataBag> walk(DecisionTree tree) {
        Optional<DataBag> firstRowSpecOpt = getFirstRowSpecFromRandomisingIteration(tree);
        //noinspection OptionalIsPresent
        if (!firstRowSpecOpt.isPresent()) {
            return Stream.empty();
        }

        return Stream.concat(
            Stream.of(firstRowSpecOpt.get()),
            Stream.generate(() ->
                getFirstRowSpecFromRandomisingIteration(tree))
                    .filter(Optional::isPresent)
                    .map(Optional::get));
    }

    private Optional<DataBag> getFirstRowSpecFromRandomisingIteration(DecisionTree tree) {
        try {
            return underlyingWalker.walk(tree)
                .findFirst();
        } catch (RetryLimitReachedException ex) {
            return Optional.empty();
        }

    }
}
