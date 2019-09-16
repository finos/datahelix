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

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeLimit;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsMerger;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;

class DateTimeRestrictionsMergerTests {

    private LinearRestrictionsMerger merger = new LinearRestrictionsMerger();

    private static final OffsetDateTime REFERENCE_TIME = OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);


    @Test
    void merge_dateTimeRestrictionsAreBothNull_returnsNull() {
        MergeResult<LinearRestrictions<Object>> result = merger.merge(null, null);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, is(nullValue()));
    }

    @Test
    void merge_leftIsNullAndRightHasAValue_returnsRight() {
        DateTimeRestrictions right = new DateTimeRestrictions(null, null);

        MergeResult<LinearRestrictions<OffsetDateTime>> result = merger.merge(null, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, sameInstance(right));
    }

    @Test
    void merge_rightIsNullAndLeftHasAValue_returnsLeft() {
        DateTimeRestrictions left = new DateTimeRestrictions(null, null);

        MergeResult<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, null);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, sameInstance(left));
    }

    @Test
    void merge_leftHasMinAndRightHasMax_returnsDateTimeRestrictionsWithMergedMinAndMax() {
        DateTimeLimit minDateTimeLimit = new DateTimeLimit(
            REFERENCE_TIME.minusDays(7), false
        );
        DateTimeLimit maxDateTimeLimit = new DateTimeLimit(
            REFERENCE_TIME, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions(minDateTimeLimit, null);
        DateTimeRestrictions right = new DateTimeRestrictions(null, maxDateTimeLimit);

        MergeResult<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions.getMin(), equalTo(minDateTimeLimit));
        Assert.assertThat(result.restrictions.getMax(), equalTo(maxDateTimeLimit));
    }

    @Test
    void merge_leftHasMaxAndRightHasMin_returnsDateTimeRestrictionsWithMergedMinAndMax() {
        DateTimeLimit minDateTimeLimit = new DateTimeLimit(
            REFERENCE_TIME.minusDays(7), false
        );
        DateTimeLimit maxDateTimeLimit = new DateTimeLimit(
            REFERENCE_TIME, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions(minDateTimeLimit, null);
        DateTimeRestrictions right = new DateTimeRestrictions(null, maxDateTimeLimit);

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions.getMin(), equalTo(minDateTimeLimit));
        Assert.assertThat(result.restrictions.getMax(), equalTo(maxDateTimeLimit));
    }

    @Test
    void merge_leftHasMinGreaterThanRightMax_returnsNull() {
        DateTimeLimit minDateTimeLimit = new DateTimeLimit(
            OffsetDateTime.now(), false
        );
        DateTimeLimit maxDateTimeLimit = new DateTimeLimit(
            OffsetDateTime.now().minusDays(10), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions(minDateTimeLimit, null);
        DateTimeRestrictions right = new DateTimeRestrictions(null, maxDateTimeLimit);

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_rightHasMinGreaterThanLeftMax_returnsNull() {
        DateTimeLimit minDateTimeLimit = new DateTimeLimit(
            OffsetDateTime.now(), false
        );
        DateTimeLimit maxDateTimeLimit = new DateTimeLimit(
            OffsetDateTime.now().minusDays(10), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions(null, maxDateTimeLimit);
        DateTimeRestrictions right = new DateTimeRestrictions(minDateTimeLimit, null);

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_rightAndLeftHaveNoMax_shouldNotReturnNull() {
        DateTimeLimit minDateTimeLimit = new DateTimeLimit(
            REFERENCE_TIME, false
        );

        DateTimeRestrictions left = new DateTimeRestrictions(minDateTimeLimit, null);
        DateTimeRestrictions right = new DateTimeRestrictions(minDateTimeLimit, null);

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions.getMin(), equalTo(minDateTimeLimit));
        Assert.assertThat(result.restrictions.getMax(), is(nullValue()));
    }

    @Test
    void merge_rightAndLeftHaveNoMin_shouldNotReturnNull() {
        DateTimeLimit maxDateTimeLimit = new DateTimeLimit(
            REFERENCE_TIME, false
        );

        DateTimeRestrictions left = new DateTimeRestrictions(null, maxDateTimeLimit);
        DateTimeRestrictions right = new DateTimeRestrictions(null, maxDateTimeLimit);

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertNotEquals(result, nullValue());

        DateTimeRestrictions restrictions = result.restrictions;
        Assert.assertNotEquals(restrictions, nullValue());


        Assert.assertNotEquals(restrictions.getMin(), nullValue());
        Assert.assertEquals(restrictions.getMax(), maxDateTimeLimit);

    }

    @Test
    void merge_minExclusiveSameAsMaxInclusive_shouldReturnAsUnsuccessful() {
        OffsetDateTime referenceTime = OffsetDateTime.now();

        DateTimeLimit minDateTimeLimit = new DateTimeLimit(
            referenceTime, false
        );
        DateTimeLimit maxDateTimeLimit = new DateTimeLimit(
            referenceTime, true
        );

        DateTimeRestrictions left = new DateTimeRestrictions(null, maxDateTimeLimit);
        DateTimeRestrictions right = new DateTimeRestrictions(minDateTimeLimit, null);

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_minExclusiveSameAsMaxExclusive_shouldReturnAsUnsuccessful() {
        OffsetDateTime referenceTime = OffsetDateTime.now();

        DateTimeLimit minDateTimeLimit = new DateTimeLimit(
            referenceTime, false
        );
        DateTimeLimit maxDateTimeLimit = new DateTimeLimit(
            referenceTime, false
        );

        DateTimeRestrictions left = new DateTimeRestrictions(null, maxDateTimeLimit);
        DateTimeRestrictions right = new DateTimeRestrictions(minDateTimeLimit, null);


        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_minInclusiveSameAsMaxExclusive_shouldReturnAsUnsuccessful() {
        OffsetDateTime referenceTime = OffsetDateTime.now();

        DateTimeLimit minDateTimeLimit = new DateTimeLimit(
            referenceTime, true
        );
        DateTimeLimit maxDateTimeLimit = new DateTimeLimit(
            referenceTime, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions(null, maxDateTimeLimit);
        DateTimeRestrictions right = new DateTimeRestrictions(minDateTimeLimit, null);

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_minOfDifferentGranularity_shouldReturnMostCoarse() {
        OffsetDateTime laterTime = REFERENCE_TIME.plusSeconds(1);

        DateTimeLimit lowerDateTimeLimit = new DateTimeLimit(
            REFERENCE_TIME, true
        );
        DateTimeLimit upperDateTimeLimit = new DateTimeLimit(
            laterTime, false
        );

        DateTimeRestrictions left = new DateTimeRestrictions(lowerDateTimeLimit, null, Timescale.HOURS);
        DateTimeRestrictions right = new DateTimeRestrictions(upperDateTimeLimit, null, Timescale.MILLIS);

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);
        DateTimeRestrictions restrictions = result.restrictions;

        Assert.assertThat(result, not(nullValue()));
        Assert.assertEquals(true, result.successful);

        Assert.assertThat(restrictions, not(nullValue()));
        Assert.assertEquals(Timescale.HOURS, restrictions.getTimeScale());

        Assert.assertEquals(restrictions.getMin(), new DateTimeLimit(REFERENCE_TIME.plusHours(1), true));
    }

    @Test
    void merge_minOfDifferentGranularity_shouldReturnMostCoarseAndBeInclusiveWhenWeWouldOtherwiseLoseAValidResult() {
        // edge case example
        // constraint later than 00:00:00 (exclusive / inclusive = false, granularity = HOURS)
        // constraint later than 00:00:01 (exclusive / inclusive = false, granularity =  SECONDS)
        //
        //Should Return: equal to or later than 01:00:00 (inclusive, granularity = HOURS / inclusive true)
        // if inclusive were false, we would exclude 01:00:00. as 01:00:00 meets both original constraints, it should be included

        OffsetDateTime earlyTime = REFERENCE_TIME;
        OffsetDateTime laterTime = REFERENCE_TIME.plusSeconds(1);

        DateTimeLimit lowerDateTimeLimit = new DateTimeLimit(earlyTime, false);
        DateTimeLimit upperDateTimeLimit = new DateTimeLimit(laterTime, false);

        DateTimeRestrictions early = new DateTimeRestrictions(lowerDateTimeLimit, null, Timescale.HOURS);
        DateTimeRestrictions later = new DateTimeRestrictions(upperDateTimeLimit, null, Timescale.SECONDS);

        MergeResult<DateTimeRestrictions> result = merger.merge(early, later);
        DateTimeRestrictions restrictions = result.restrictions;

        // assert that we get the correct level of granularity
        Assert.assertNotNull(restrictions);
        Assert.assertEquals(Timescale.HOURS, restrictions.getTimeScale());

        // assert that we return an inclusive restriction for this edge case.
        Assert.assertEquals(restrictions.getMin(), new DateTimeLimit(REFERENCE_TIME.plusHours(1), true));
    }

    @Test
    void merge_inclusiveOnLeftIsPassedIn_shouldReturnInclusive() {
        // ARRANGE
        DateTimeRestrictions left = new DateTimeRestrictions(
            new DateTimeLimit(REFERENCE_TIME, true),
            null, Timescale.HOURS
        );
        DateTimeRestrictions right = new DateTimeRestrictions(
            new DateTimeLimit(REFERENCE_TIME.plusSeconds(1), false),
            null, Timescale.SECONDS
        );

        // ACT
        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        // ASSERT
        DateTimeRestrictions expecteddt = new DateTimeRestrictions(
            new DateTimeLimit(REFERENCE_TIME.plusHours(1), true),
            null,
            Timescale.HOURS);
        MergeResult<DateTimeRestrictions> expected = new MergeResult<>(expecteddt);

        assertThat(result, sameBeanAs(expected));
    }

    @Test
    void merge_inclusiveOnRightIsPassedIn_shouldReturnInclusive() {
        // ARRANGE
        DateTimeRestrictions left = new DateTimeRestrictions(new DateTimeLimit(REFERENCE_TIME, false),
            null,
            Timescale.HOURS
        );

        DateTimeRestrictions right = new DateTimeRestrictions(
            new DateTimeLimit(REFERENCE_TIME.plusSeconds(1), true),
            null,
            Timescale.MILLIS);

        // ACT
        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        // ASSERT
        DateTimeRestrictions expecteddt = new DateTimeRestrictions(new DateTimeLimit(
            REFERENCE_TIME.plusHours(1), true),
            null,
            Timescale.HOURS);
        MergeResult<DateTimeRestrictions> expected = new MergeResult<>(expecteddt);

        assertThat(result, sameBeanAs(expected));
    }

    @Test
    void merge_notInclusivePassedIn_shouldReturnNotInclusive() {
        // ARRANGE
        DateTimeRestrictions left = new DateTimeRestrictions(
            new DateTimeLimit(REFERENCE_TIME, false),
            null,
            Timescale.HOURS);

        DateTimeRestrictions right = new DateTimeRestrictions(
            new DateTimeLimit(REFERENCE_TIME.plusHours(1), false),
            null,
            Timescale.MILLIS);

        // ACT
        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        // ASSERT
        DateTimeRestrictions expecteddt = new DateTimeRestrictions(
            new DateTimeLimit(REFERENCE_TIME.plusHours(1), false),
            null,
            Timescale.HOURS);
        MergeResult<DateTimeRestrictions> expected = new MergeResult<>(expecteddt);

        assertThat(result, sameBeanAs(expected));
    }
}
