package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.StringConstraintsCollection;
import com.scottlogic.deg.generator.constraints.atomic.StringHasLengthConstraint;
import com.scottlogic.deg.generator.generation.StringGenerator;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

public class StringRestrictionsEqualityTests {
    private static final StringConstraintsCollection stringConstraints = new StringConstraintsCollection(
        new StringHasLengthConstraint(
            new Field("field"),
            1,
            Collections.emptySet()));

    @Test
    public void restrictionsShouldBeEqualIfGeneratorsAreEqual() {
        StringRestrictions a = restrictions(new MockStringGenerator(true));
        StringRestrictions b = restrictions(new MockStringGenerator(true));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void restrictionsShouldBeUnequalIfGeneratorsAreUnequal() {
        StringRestrictions a = restrictions(new MockStringGenerator(false));
        StringRestrictions b = restrictions(new MockStringGenerator(false));

        Assert.assertThat(a, not(equalTo(b)));
    }

    private static StringRestrictions restrictions(StringGenerator generator){
        StringRestrictions stringRestrictions = new StringRestrictions(stringConstraints);
        stringRestrictions.stringGenerator = generator;
        return stringRestrictions;
    }

    private class MockStringGenerator implements StringGenerator {

        private final boolean areEqual;

        public MockStringGenerator(boolean areEqual) {
            this.areEqual = areEqual;
        }

        @Override
        public int hashCode() {
            return 1234;
        }

        @Override
        public boolean equals(Object obj) {
            return areEqual;
        }

        @Override
        public StringGenerator intersect(StringGenerator stringGenerator) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public StringGenerator complement() {
            throw new UnsupportedOperationException("Not supported");
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
        public boolean match(String subject) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public Iterable<String> generateInterestingValues() {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public Iterable<String> generateAllValues() {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public Iterable<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
            throw new UnsupportedOperationException("Not supported");
        }
    }
}
