package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
        public boolean isFinite() {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public long getValueCount() {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public Iterable<Object> generateInterestingValues() {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public Iterable<Object> generateAllValues() {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
            throw new UnsupportedOperationException("Not supported");
        }
    }
}