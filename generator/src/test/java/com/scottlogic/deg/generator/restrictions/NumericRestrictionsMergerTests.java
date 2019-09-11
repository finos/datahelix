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

package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.restrictions.linear.NumericLimit;
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
        NumericRestrictions left = new NumericRestrictions(null, null);

        MergeResult<NumericRestrictions> result = merger.merge(left, null);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, is(sameInstance(left)));
    }

    @Test
    public void merge_withOnlyRightNumericRestrictions_shouldReturnLeftRestrictions(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions right = new NumericRestrictions(null, null);

        MergeResult<NumericRestrictions> result = merger.merge(null, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, is(sameInstance(right)));
    }

    @Test
    public void merge_withNonContradictoryNumericRestrictions_shouldReturnMergedRestrictions(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new NumericLimit(BigDecimal.ZERO, true),
            new NumericLimit(BigDecimal.TEN, true));
        NumericRestrictions right = new NumericRestrictions(
            new NumericLimit(BigDecimal.ONE, false),
            new NumericLimit(BigDecimal.TEN, false));

        MergeResult<NumericRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, not(nullValue()));
        Assert.assertThat(result.restrictions.getMin().getValue(), is(equalTo(BigDecimal.ONE)));
        Assert.assertThat(result.restrictions.getMin().isInclusive(), is(false));
        Assert.assertThat(result.restrictions.getMax().getValue(), is(equalTo(BigDecimal.TEN)));
        Assert.assertThat(result.restrictions.getMax().isInclusive(), is(false));
    }

    @Test
    public void merge_withLessThanOrEqualAndGreaterThanOrEqualSameNumber_shouldReturnMergedRestrictions(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions greaterThanOrEqual = new NumericRestrictions(new NumericLimit(BigDecimal.TEN, true), null);
        NumericRestrictions lessThanOrEqual = new NumericRestrictions(null, new NumericLimit(BigDecimal.TEN, true));

        MergeResult<NumericRestrictions> result = merger.merge(greaterThanOrEqual, lessThanOrEqual);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, not(nullValue()));
        Assert.assertThat(result.restrictions.getMin().getValue(), is(equalTo(BigDecimal.TEN)));
        Assert.assertThat(result.restrictions.getMin().isInclusive(), is(true));
        Assert.assertThat(result.restrictions.getMax().getValue(), is(equalTo(BigDecimal.TEN)));
        Assert.assertThat(result.restrictions.getMax().isInclusive(), is(true));
    }

    @Test
    public void merge_withContradictoryNumericRestrictions_shouldReturnUnsuccessful(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new NumericLimit(BigDecimal.ZERO, true),
            new NumericLimit(BigDecimal.TEN, true));
        NumericRestrictions right = new NumericRestrictions(
            new NumericLimit(BigDecimal.TEN, false),
            new NumericLimit(BigDecimal.valueOf(20), false));

        MergeResult<NumericRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    public void merge_withScaleEqualToRange_shouldReturnSuccessful(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new NumericLimit(BigDecimal.ZERO, true),
            new NumericLimit(BigDecimal.ONE, true));
        NumericRestrictions right = new NumericRestrictions(null, null, 0);

        MergeResult<NumericRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
    }

    @Test
    public void merge_withScaleEqualToRangeExclusiveMax_shouldReturnSuccessful(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new NumericLimit(BigDecimal.ZERO, true),
            new NumericLimit(BigDecimal.ONE, false));

        NumericRestrictions right = new NumericRestrictions(null, null, 0);

        MergeResult<NumericRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
    }

    @Test
    public void merge_withScaleEqualToRangeExclusiveMin_shouldReturnSuccessful(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new NumericLimit(BigDecimal.ZERO, false),
            new NumericLimit(BigDecimal.ONE, true));
        NumericRestrictions right = new NumericRestrictions(null, null, 0);

        MergeResult<NumericRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
    }

    @Test
    public void merge_withScaleLargerThan_shouldReturnUnsuccessful(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new NumericLimit(BigDecimal.ZERO, false),
            new NumericLimit(BigDecimal.ONE, false));
        NumericRestrictions right = new NumericRestrictions(null, null, 0);

        MergeResult<NumericRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    public void merge_smallerScaleExlusiveLimit_shouldReturnSuccessful(){
        NumericRestrictionsMerger merger = new NumericRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new NumericLimit(BigDecimal.ZERO, false),
            new NumericLimit(BigDecimal.ONE, false));
        NumericRestrictions right = new NumericRestrictions(null, null, 1);

        MergeResult<NumericRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
    }
}