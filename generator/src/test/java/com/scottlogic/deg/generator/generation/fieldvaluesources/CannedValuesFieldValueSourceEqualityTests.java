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

package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

class CannedValuesFieldValueSourceEqualityTests {

    private FieldValueSource valueSourceOf(Object... elements) {
        Set<Object> set = Arrays.stream(elements).collect(Collectors.toSet());
        DistributedList<Object> whitelist = DistributedList.uniform(set);
        return new CannedValuesFieldValueSource(whitelist);
    }

    @Test
    public void shouldBeEqualIfAllAndInterestingValuesMatch(){
        FieldValueSource a = valueSourceOf("a", "b", "c");
        FieldValueSource b = valueSourceOf("a", "b", "c");

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalIfAllOrInterestingValuesDiffer(){
        FieldValueSource a = valueSourceOf("a", "b", "c");
        FieldValueSource b = valueSourceOf("a", "b");

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void emptyCollectionsShouldBeEqual(){
        FieldValueSource a = valueSourceOf();
        FieldValueSource b = valueSourceOf();

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }
}