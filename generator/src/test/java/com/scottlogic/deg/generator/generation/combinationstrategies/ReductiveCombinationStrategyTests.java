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
import com.scottlogic.deg.generator.generation.databags.DataBagStream;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class ReductiveCombinationStrategyTests {
    @Test
    void permute_dataBagSequencesContainsTwoFieldsWithMultipleValues_returnsExpectedValues() {
        List<DataBag> firstFieldDataBags = new ArrayList<DataBag>() {{
            add(
                new DataBagBuilder().set(
                    new Field("First Field", false),
                    new DataBagValue(10)
                ).build()
            );
            add(
                new DataBagBuilder().set(
                    new Field("First Field", false),
                    new DataBagValue(20)
                ).build()
            );
        }};
        List<DataBag> secondFieldDataBags = new ArrayList<DataBag>() {{
            add(
                new DataBagBuilder().set(
                    new Field("Second Field", false),
                    new DataBagValue("A")
                ).build()
            );
            add(
                new DataBagBuilder().set(
                    new Field("Second Field", false),
                    new DataBagValue("B")
                ).build()
            );
        }};
        ReductiveCombinationStrategy combinationStrategy = new ReductiveCombinationStrategy();

        ArrayList<List<DataBag>> dataBagSequences = new ArrayList<List<DataBag>>() {{
            add(firstFieldDataBags);
            add(secondFieldDataBags);
        }};
        final List<DataBag> result = combinationStrategy.permute(dataBagSequences.stream().map(Collection::stream).map(i -> new DataBagStream(i, false)))
            .collect(Collectors.toList());

        List<DataBag> expectedDataBags = new ArrayList<DataBag>() {{
            add(
                new DataBagBuilder().set(
                    new Field("First Field", false),
                    new DataBagValue(10)
                ).set(
                    new Field("Second Field", false),
                    new DataBagValue("A")
                ).build()
            );
            add(
                new DataBagBuilder().set(
                    new Field("First Field", false),
                    new DataBagValue(10)
                ).set(
                    new Field("Second Field", false),
                    new DataBagValue("B")
                ).build()
            );
            add(
                new DataBagBuilder().set(
                    new Field("First Field", false),
                    new DataBagValue(20)
                ).set(
                    new Field("Second Field", false),
                    new DataBagValue("A")
                ).build()
            );
            add(
                new DataBagBuilder().set(
                    new Field("First Field", false),
                    new DataBagValue(20)
                ).set(
                    new Field("Second Field", false),
                    new DataBagValue("B")
                ).build()
            );
        }};
        Assert.assertEquals(expectedDataBags, result);
    }
}
