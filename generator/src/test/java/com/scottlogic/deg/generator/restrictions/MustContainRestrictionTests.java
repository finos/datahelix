package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

class MustContainRestrictionTests {
    @Test
    public void shouldBeEqualIfBothContainSameRequiredObjects(){
        MustContainRestriction a = restriction(fieldSpec(1), fieldSpec(2));
        MustContainRestriction b = restriction(fieldSpec(1), fieldSpec(2));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeEqualIfBothContainSameRequiredObjectsIfDifferentOrder(){
        MustContainRestriction a = restriction(fieldSpec(1), fieldSpec(2));
        MustContainRestriction b = restriction(fieldSpec(2), fieldSpec(1));

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalIfRequiredObjectsAreDifferent(){
        MustContainRestriction a = restriction(fieldSpec(1), fieldSpec(2));
        MustContainRestriction b = restriction(fieldSpec(1), fieldSpec(3));

        Assert.assertThat(a, not(equalTo(b)));
    }

    private static MustContainRestriction restriction(FieldSpec... fieldSpecs){
        return new MustContainRestriction(new HashSet<>(Arrays.asList(fieldSpecs)));
    }

    private FieldSpec fieldSpec(int hashCode){
        return FieldSpec.Empty.withSetRestrictions(
            SetRestrictions.fromWhitelist(Collections.singleton(hashCode)),
            FieldSpecSource.Empty);
    }
}