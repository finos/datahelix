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

import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsMerger;

import com.scottlogic.deg.generator.restrictions.linear.NumericRestrictions;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.scottlogic.deg.generator.restrictions.linear.NumericRestrictions.NUMERIC_MAX_LIMIT;
import static com.scottlogic.deg.generator.restrictions.linear.NumericRestrictions.NUMERIC_MIN_LIMIT;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;

class NumericRestrictionsMergerTests {
    @Test
    public void merge_withNoRestrictions_shouldReturnSuccessWithNoRestrictions(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();

        MergeResult<LinearRestrictions<BigDecimal>> result = merger.merge(null, null);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, is(nullValue()));
    }

    @Test
    public void merge_withOnlyLeftNumericRestrictions_shouldReturnLeftRestrictions(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT);

        MergeResult<LinearRestrictions<BigDecimal>> result = merger.merge(left, null);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, is(sameInstance(left)));
    }

    @Test
    public void merge_withOnlyRightNumericRestrictions_shouldReturnLeftRestrictions(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        NumericRestrictions right = new NumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT);

        MergeResult<LinearRestrictions<BigDecimal>> result = merger.merge(null, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, is(sameInstance(right)));
    }

    @Test
    public void merge_withNonContradictoryNumericRestrictions_shouldReturnMergedRestrictions(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new Limit<>(BigDecimal.ZERO, true),
            new Limit<>(BigDecimal.TEN, true));
        NumericRestrictions right = new NumericRestrictions(
            new Limit<>(BigDecimal.ONE, false),
            new Limit<>(BigDecimal.TEN, false));

        MergeResult<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

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
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        NumericRestrictions greaterThanOrEqual = new NumericRestrictions(new Limit<>(BigDecimal.TEN, true), NUMERIC_MAX_LIMIT);
        NumericRestrictions lessThanOrEqual = new NumericRestrictions(NUMERIC_MIN_LIMIT, new Limit<>(BigDecimal.TEN, true));

        MergeResult<LinearRestrictions<BigDecimal>> result = merger.merge(greaterThanOrEqual, lessThanOrEqual);

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
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new Limit<>(BigDecimal.ZERO, true),
            new Limit<>(BigDecimal.TEN, true));
        NumericRestrictions right = new NumericRestrictions(
            new Limit<>(BigDecimal.TEN, false),
            new Limit<>(BigDecimal.valueOf(20), false));

        MergeResult<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    public void merge_withScaleEqualToRange_shouldReturnSuccessful(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new Limit<>(BigDecimal.ZERO, true),
            new Limit<>(BigDecimal.ONE, true));
        NumericRestrictions right = new NumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT, 0);

        MergeResult<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
    }

    @Test
    public void merge_withScaleEqualToRangeExclusiveMax_shouldReturnSuccessful(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new Limit<>(BigDecimal.ZERO, true),
            new Limit<>(BigDecimal.ONE, false));

        NumericRestrictions right = new NumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT, 0);

        MergeResult<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
    }

    @Test
    public void merge_withScaleEqualToRangeExclusiveMin_shouldReturnSuccessful(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new Limit<>(BigDecimal.ZERO, false),
            new Limit<>(BigDecimal.ONE, true));
        NumericRestrictions right = new NumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT, 0);

        MergeResult<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
    }

    @Test
    public void merge_withScaleLargerThan_shouldReturnUnsuccessful(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new Limit<>(BigDecimal.ZERO, false),
            new Limit<>(BigDecimal.ONE, false));
        NumericRestrictions right = new NumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT, 0);

        MergeResult<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    public void merge_smallerScaleExlusiveLimit_shouldReturnSuccessful(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        NumericRestrictions left = new NumericRestrictions(
            new Limit<>(BigDecimal.ZERO, false),
            new Limit<>(BigDecimal.ONE, false));
        NumericRestrictions right = new NumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT, 1);

        MergeResult<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
    }
}