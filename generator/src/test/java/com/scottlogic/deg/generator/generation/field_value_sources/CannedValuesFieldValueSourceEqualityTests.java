package com.scottlogic.deg.generator.generation.field_value_sources;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

class CannedValuesFieldValueSourceEqualityTests {
    @Test
    public void shouldBeEqualIfAllAndInterestingValuesMatch(){
        FieldValueSource a = CannedValuesFieldValueSource.of("a", "b", "c");
        FieldValueSource b = CannedValuesFieldValueSource.of("a", "b", "c");

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalIfAllAndInterestingValuesMatchButInDifferentOrder(){
        FieldValueSource a = CannedValuesFieldValueSource.of("a", "b", "c");
        FieldValueSource b = CannedValuesFieldValueSource.of("c", "b", "a");

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void shouldBeUnequalIfAllOrInterestingValuesDiffer(){
        FieldValueSource a = CannedValuesFieldValueSource.of("a", "b", "c");
        FieldValueSource b = CannedValuesFieldValueSource.of("a", "b");

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void emptyCollectionsShouldBeEqual(){
        FieldValueSource a = CannedValuesFieldValueSource.of();
        FieldValueSource b = CannedValuesFieldValueSource.of();

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }
}