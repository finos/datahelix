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

class NullAppendingFieldValueSourceEqualityTests {
    @Test
    public void shouldBeEqualIfUnderlyingSourcesAreTheSame(){
        NullAppendingValueSource a = new NullAppendingValueSource(source(1));
        NullAppendingValueSource b = new NullAppendingValueSource(source(1));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalIfUnderlyingSourcesAreTheSameButDifferentOrder(){
        NullAppendingValueSource a = new NullAppendingValueSource(source(1));
        NullAppendingValueSource b = new NullAppendingValueSource(source(2));

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
        public Stream<Object> generateInterestingValues() {
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