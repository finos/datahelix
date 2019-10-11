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

import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularity;
import com.scottlogic.deg.generator.restrictions.linear.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static com.scottlogic.deg.generator.utils.Defaults.NUMERIC_MAX_LIMIT;
import static com.scottlogic.deg.generator.utils.Defaults.NUMERIC_MIN_LIMIT;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

class NumericRestrictionsMergerTests {
    @Test
    public void merge_withNonContradictoryNumericRestrictions_shouldReturnMergedRestrictions(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        LinearRestrictions<BigDecimal> left = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(BigDecimal.ZERO, true),
            new Limit<>(BigDecimal.TEN, true));
        LinearRestrictions<BigDecimal> right = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(BigDecimal.ONE, false),
            new Limit<>(BigDecimal.TEN, false));

        Optional<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(nullValue()));
        Assert.assertThat(result.get().getMin(), is(greaterThan(BigDecimal.ONE)));
        Assert.assertThat(result.get().getMax(), is(lessThan(BigDecimal.TEN)));
    }

    @Test
    public void merge_withLessThanOrEqualAndGreaterThanOrEqualSameNumber_shouldReturnMergedRestrictions(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        LinearRestrictions<BigDecimal> greaterThanOrEqual = LinearRestrictionsFactory.createNumericRestrictions(new Limit<>(BigDecimal.TEN, true), NUMERIC_MAX_LIMIT);
        LinearRestrictions<BigDecimal> lessThanOrEqual = LinearRestrictionsFactory.createNumericRestrictions(NUMERIC_MIN_LIMIT, new Limit<>(BigDecimal.TEN, true));

        Optional<LinearRestrictions<BigDecimal>> result = merger.merge(greaterThanOrEqual, lessThanOrEqual);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get(), not(nullValue()));
        Assert.assertThat(result.get().getMin(), is(equalTo(BigDecimal.TEN)));
        Assert.assertThat(result.get().getMax(), is(equalTo(BigDecimal.TEN)));
    }

    @Test
    public void merge_withContradictoryNumericRestrictions_shouldReturnUnsuccessful(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        LinearRestrictions<BigDecimal> left = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(BigDecimal.ZERO, true),
            new Limit<>(BigDecimal.TEN, true));
        LinearRestrictions<BigDecimal> right = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(BigDecimal.TEN, false),
            new Limit<>(BigDecimal.valueOf(20), false));

        Optional<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(false));
    }

    @Test
    public void merge_withScaleEqualToRange_shouldReturnSuccessful(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        LinearRestrictions<BigDecimal> left = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(BigDecimal.ZERO, true),
            new Limit<>(BigDecimal.ONE, true));
        LinearRestrictions<BigDecimal> right = LinearRestrictionsFactory.createNumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT, new NumericGranularity(0));

        Optional<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(true));
    }

    @Test
    public void merge_withScaleEqualToRangeExclusiveMax_shouldReturnSuccessful(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        LinearRestrictions<BigDecimal> left = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(BigDecimal.ZERO, true),
            new Limit<>(BigDecimal.ONE, false));

        LinearRestrictions<BigDecimal> right = LinearRestrictionsFactory.createNumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT, new NumericGranularity(0));

        Optional<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(true));
    }

    @Test
    public void merge_withScaleEqualToRangeExclusiveMin_shouldReturnSuccessful(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        LinearRestrictions<BigDecimal> left = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(BigDecimal.ZERO, false),
            new Limit<>(BigDecimal.ONE, true));
        LinearRestrictions<BigDecimal> right = LinearRestrictionsFactory.createNumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT, new NumericGranularity(0));

        Optional<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(true));
    }

    @Test
    public void merge_withScaleLargerThan_shouldReturnUnsuccessful(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        LinearRestrictions<BigDecimal> left = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(BigDecimal.ZERO, false),
            new Limit<>(BigDecimal.ONE, false));
        LinearRestrictions<BigDecimal> right = LinearRestrictionsFactory.createNumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT, new NumericGranularity(0));

        Optional<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(false));
    }

    @Test
    public void merge_smallerScaleExlusiveLimit_shouldReturnSuccessful(){
        LinearRestrictionsMerger merger = new LinearRestrictionsMerger();
        LinearRestrictions<BigDecimal> left = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(BigDecimal.ZERO, false),
            new Limit<>(BigDecimal.ONE, false));
        LinearRestrictions<BigDecimal> right = LinearRestrictionsFactory.createNumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT, new NumericGranularity(1));

        Optional<LinearRestrictions<BigDecimal>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(true));
    }
}