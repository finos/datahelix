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

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

class CombiningFieldValueSourceEqualityTests {
    @Test
    public void shouldBeEqualIfUnderlyingSourcesAreTheSame(){
        CombiningFieldValueSource a = new CombiningFieldValueSource(Arrays.asList(source(1), source(2)));
        CombiningFieldValueSource b = new CombiningFieldValueSource(Arrays.asList(source(1), source(2)));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalIfUnderlyingSourcesAreTheSameButDifferentOrder(){
        CombiningFieldValueSource a = new CombiningFieldValueSource(Arrays.asList(source(1), source(2)));
        CombiningFieldValueSource b = new CombiningFieldValueSource(Arrays.asList(source(2), source(1)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalIfUnderlyingSourcesAreDifferent(){
        CombiningFieldValueSource a = new CombiningFieldValueSource(Arrays.asList(source(1), source(2)));
        CombiningFieldValueSource b = new CombiningFieldValueSource(Arrays.asList(source(2), source(3)));

        Assert.assertThat(a, not(equalTo(b)));
    }

    private FieldValueSource source(int hashCode){
        return new MockFieldValueSource(hashCode);
    }

    private class MockFieldValueSource implements FieldValueSource{
        private final int hashCode;

        MockFieldValueSource(int hashCode) {
            this.hashCode = hashCode;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            return obj.hashCode() == hashCode;
        }

        @Override
        public Iterable<Object> generateInterestingValues() {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public Stream<Object> generateAllValues() {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public Stream<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
            throw new UnsupportedOperationException("Not supported");
        }
    }
}