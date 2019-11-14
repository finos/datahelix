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

import com.scottlogic.deg.generator.builders.DataBagBuilder;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import org.hamcrest.collection.IsArrayContainingInAnyOrder;
import org.junit.Assert;

import java.util.function.Supplier;
import java.util.stream.Stream;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class CombinationStrategyTester {
    private CombinationStrategy strategy;
    private Stream<Supplier<Stream<DataBag>>> dataBags;

    CombinationStrategyTester(CombinationStrategy combinationStrategy) {
        strategy = combinationStrategy;
    }

    @SafeVarargs
    final void given(Stream<DataBag>... bagSequences) {
        dataBags = Stream.of(bagSequences).map(x->()->x);
    }
    @SafeVarargs
    final void given(Supplier<Stream<DataBag>>... bagSequences) {
        dataBags = Stream.of(bagSequences);
    }

    void expect(Stream<DataBag> bagSequence) {
        DataBag[] results = strategy.permute(dataBags).toArray(DataBag[]::new);
        DataBag[] bagArray = bagSequence.toArray(DataBag[]::new);

        Assert.assertThat(results, IsArrayContainingInAnyOrder.arrayContainingInAnyOrder(bagArray));
    }

    void expectEmpty() {
        Stream<DataBag> results = strategy.permute(dataBags);

        Assert.assertFalse(results.iterator().hasNext());
    }

    static DataBag bag(String... fieldNames) {
        DataBagBuilder builder = new DataBagBuilder();

        for (String fieldName : fieldNames) {
            builder.set(createField(fieldName), "whatever");
        }

        return builder.build();
    }
}
