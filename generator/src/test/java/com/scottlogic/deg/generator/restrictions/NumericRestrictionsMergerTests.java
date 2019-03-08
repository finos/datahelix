package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;

class NumericRestrictionsMergerTests {
    @Test
    public void merge_withNoRestrictions_shouldReturnSuccessWithNoRestrictions(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();

        MergeResult<NumericRestrictions> result = merger.merge(null, null);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, is(nullValue()));
    }

    @Test
    public void merge_withOnlyLeftNumericRestrictions_shouldReturnLeftRestrictions(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions();

        MergeResult<NumericRestrictions> result = merger.merge(left, null);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, is(sameInstance(left)));
    }

    @Test
    public void merge_withOnlyRightNumericRestrictions_shouldReturnLeftRestrictions(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions right = new NumericRestrictions();

        MergeResult<NumericRestrictions> result = merger.merge(null, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, is(sameInstance(right)));
    }

    @Test
    public void merge_withNonContradictoryNumericRestrictions_shouldReturnMergedRestrictions(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions();
        NumericRestrictions right = new NumericRestrictions();
        left.min = new NumericLimit<>(BigDecimal.ZERO, true);
        left.max = new NumericLimit<>(BigDecimal.TEN, true);
        right.min = new NumericLimit<>(BigDecimal.ONE, false);
        right.max = new NumericLimit<>(BigDecimal.TEN, false);

        MergeResult<NumericRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, not(nullValue()));
        Assert.assertThat(result.restrictions.min.getLimit(), is(equalTo(BigDecimal.ONE)));
        Assert.assertThat(result.restrictions.min.isInclusive(), is(false));
        Assert.assertThat(result.restrictions.max.getLimit(), is(equalTo(BigDecimal.TEN)));
        Assert.assertThat(result.restrictions.max.isInclusive(), is(false));
    }

    @Test
    public void merge_withContradictoryNumericRestrictions_shouldReturnUnsuccessful(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions();
        NumericRestrictions right = new NumericRestrictions();
        left.min = new NumericLimit<>(BigDecimal.ZERO, true);
        left.max = new NumericLimit<>(BigDecimal.TEN, true);
        right.min = new NumericLimit<>(BigDecimal.TEN, false);
        right.max = new NumericLimit<>(BigDecimal.valueOf(20), false);

        MergeResult<NumericRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }
}