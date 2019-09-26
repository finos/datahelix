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
import com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsMerger;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static com.scottlogic.deg.generator.utils.Defaults.*;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

class DateTimeRestrictionsMergerTests {

    private LinearRestrictionsMerger merger = new LinearRestrictionsMerger();

    private static final OffsetDateTime REFERENCE_TIME = OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    @Test
    void merge_leftHasMinAndRightHasMax_returnsDateTimeRestrictionsWithMergedMinAndMax() {
        Limit<OffsetDateTime> minDateTimeLimit = new Limit<>(
            REFERENCE_TIME.minusDays(7), false
        );
        Limit <OffsetDateTime>maxDateTimeLimit = new Limit<>(
            REFERENCE_TIME, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions(minDateTimeLimit, DATETIME_MAX_LIMIT);
        DateTimeRestrictions right = new DateTimeRestrictions(DATETIME_MIN_LIMIT, maxDateTimeLimit);

        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get().getMin(), equalTo(minDateTimeLimit));
        Assert.assertThat(result.get().getMax(), equalTo(maxDateTimeLimit));
    }

    @Test
    void merge_leftHasMaxAndRightHasMin_returnsDateTimeRestrictionsWithMergedMinAndMax() {
        Limit <OffsetDateTime>minDateTimeLimit = new Limit<>(
            REFERENCE_TIME.minusDays(7), false
        );
        Limit <OffsetDateTime>maxDateTimeLimit = new Limit<>(
            REFERENCE_TIME, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions(minDateTimeLimit, DATETIME_MAX_LIMIT);
        DateTimeRestrictions right = new DateTimeRestrictions(DATETIME_MIN_LIMIT, maxDateTimeLimit);

        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get().getMin(), equalTo(minDateTimeLimit));
        Assert.assertThat(result.get().getMax(), equalTo(maxDateTimeLimit));
    }

    @Test
    void merge_leftHasMinGreaterThanRightMax_returnsNull() {
        Limit <OffsetDateTime>minDateTimeLimit = new Limit<>(
            OffsetDateTime.now(), false
        );
        Limit <OffsetDateTime>maxDateTimeLimit = new Limit<>(
            OffsetDateTime.now().minusDays(10), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions(minDateTimeLimit, DATETIME_MAX_LIMIT);
        DateTimeRestrictions right = new DateTimeRestrictions(DATETIME_MIN_LIMIT, maxDateTimeLimit);

        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(false));
    }

    @Test
    void merge_rightHasMinGreaterThanLeftMax_returnsNull() {
        Limit <OffsetDateTime>minDateTimeLimit = new Limit<>(
            OffsetDateTime.now(), false
        );
        Limit <OffsetDateTime>maxDateTimeLimit = new Limit<>(
            OffsetDateTime.now().minusDays(10), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions(DATETIME_MIN_LIMIT, maxDateTimeLimit);
        DateTimeRestrictions right = new DateTimeRestrictions(minDateTimeLimit, DATETIME_MAX_LIMIT);

        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(false));
    }

    @Test
    void merge_rightAndLeftHaveNoMax_shouldNotReturnNull() {
        Limit <OffsetDateTime>minDateTimeLimit = new Limit<>(
            REFERENCE_TIME, false
        );

        DateTimeRestrictions left = new DateTimeRestrictions(minDateTimeLimit, DATETIME_MAX_LIMIT);
        DateTimeRestrictions right = new DateTimeRestrictions(minDateTimeLimit, DATETIME_MAX_LIMIT);

        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(true));
        Assert.assertThat(result.get().getMin(), equalTo(minDateTimeLimit));
        Assert.assertThat(result.get().getMax(), is(DATETIME_MAX_LIMIT));
    }

    @Test
    void merge_rightAndLeftHaveNoMin_shouldNotReturnNull() {
        Limit <OffsetDateTime>maxDateTimeLimit = new Limit<>(
            REFERENCE_TIME, false
        );

        DateTimeRestrictions left = new DateTimeRestrictions(DATETIME_MIN_LIMIT, maxDateTimeLimit);
        DateTimeRestrictions right = new DateTimeRestrictions(DATETIME_MIN_LIMIT, maxDateTimeLimit);

        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        Assert.assertNotEquals(result, nullValue());

        LinearRestrictions<OffsetDateTime> restrictions = result.get();
        Assert.assertNotEquals(restrictions, nullValue());


        Assert.assertNotEquals(restrictions.getMin(), nullValue());
        Assert.assertEquals(restrictions.getMax(), maxDateTimeLimit);

    }

    @Test
    void merge_minExclusiveSameAsMaxInclusive_shouldReturnAsUnsuccessful() {
        OffsetDateTime referenceTime = OffsetDateTime.now();

        Limit <OffsetDateTime>minDateTimeLimit = new Limit<>(
            referenceTime, false
        );
        Limit <OffsetDateTime>maxDateTimeLimit = new Limit<>(
            referenceTime, true
        );

        DateTimeRestrictions left = new DateTimeRestrictions(DATETIME_MIN_LIMIT, maxDateTimeLimit);
        DateTimeRestrictions right = new DateTimeRestrictions(minDateTimeLimit, DATETIME_MAX_LIMIT);

        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(false));
    }

    @Test
    void merge_minExclusiveSameAsMaxExclusive_shouldReturnAsUnsuccessful() {
        OffsetDateTime referenceTime = OffsetDateTime.now();

        Limit <OffsetDateTime>minDateTimeLimit = new Limit<>(
            referenceTime, false
        );
        Limit <OffsetDateTime>maxDateTimeLimit = new Limit<>(
            referenceTime, false
        );

        DateTimeRestrictions left = new DateTimeRestrictions(DATETIME_MIN_LIMIT, maxDateTimeLimit);
        DateTimeRestrictions right = new DateTimeRestrictions(minDateTimeLimit, DATETIME_MAX_LIMIT);


        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(false));
    }

    @Test
    void merge_minInclusiveSameAsMaxExclusive_shouldReturnAsUnsuccessful() {
        OffsetDateTime referenceTime = OffsetDateTime.now();

        Limit <OffsetDateTime>minDateTimeLimit = new Limit<>(
            referenceTime, true
        );
        Limit <OffsetDateTime>maxDateTimeLimit = new Limit<>(
            referenceTime, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions(DATETIME_MIN_LIMIT, maxDateTimeLimit);
        DateTimeRestrictions right = new DateTimeRestrictions(minDateTimeLimit, DATETIME_MAX_LIMIT);

        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.isPresent(), is(false));
    }

    @Test
    void merge_minOfDifferentGranularity_shouldReturnMostCoarse() {
        OffsetDateTime laterTime = REFERENCE_TIME.plusSeconds(1);

        Limit <OffsetDateTime>lowerDateTimeLimit = new Limit<>(
            REFERENCE_TIME, true
        );
        Limit <OffsetDateTime>upperDateTimeLimit = new Limit<>(
            laterTime, false
        );

        DateTimeRestrictions left = new DateTimeRestrictions(lowerDateTimeLimit, DATETIME_MAX_LIMIT, Timescale.HOURS);
        DateTimeRestrictions right = new DateTimeRestrictions(upperDateTimeLimit, DATETIME_MAX_LIMIT, Timescale.MILLIS);

        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);
        LinearRestrictions<OffsetDateTime> restrictions = result.get();

        Assert.assertThat(result, not(nullValue()));
        Assert.assertEquals(true, result.isPresent());

        Assert.assertThat(restrictions, not(nullValue()));

        Assert.assertEquals(restrictions.getMin(), new Limit<>(REFERENCE_TIME.plusHours(1), true));
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

        Limit <OffsetDateTime>lowerDateTimeLimit = new Limit<>(earlyTime, false);
        Limit <OffsetDateTime>upperDateTimeLimit = new Limit<>(laterTime, false);

        DateTimeRestrictions early = new DateTimeRestrictions(lowerDateTimeLimit, DATETIME_MAX_LIMIT, Timescale.HOURS);
        DateTimeRestrictions later = new DateTimeRestrictions(upperDateTimeLimit, DATETIME_MAX_LIMIT, Timescale.SECONDS);

        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(early, later);
        LinearRestrictions<OffsetDateTime> restrictions = result.get();

        // assert that we get the correct level of granularity
        Assert.assertNotNull(restrictions);

        // assert that we return an inclusive restriction for this edge case.
        Assert.assertEquals(restrictions.getMin(), new Limit<>(REFERENCE_TIME.plusHours(1), true));
    }

    @Test
    void merge_inclusiveOnLeftIsPassedIn_shouldReturnInclusive() {
        // ARRANGE
        DateTimeRestrictions left = new DateTimeRestrictions(
            new Limit<>(REFERENCE_TIME, true),
            DATETIME_MAX_LIMIT, Timescale.HOURS
        );
        DateTimeRestrictions right = new DateTimeRestrictions(
            new Limit<>(REFERENCE_TIME.plusSeconds(1), false),
            DATETIME_MAX_LIMIT, Timescale.SECONDS
        );

        // ACT
        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        // ASSERT
        DateTimeRestrictions expecteddt = new DateTimeRestrictions(
            new Limit<>(REFERENCE_TIME.plusHours(1), true),
            DATETIME_MAX_LIMIT,
            Timescale.HOURS);
        Optional<DateTimeRestrictions> expected = Optional.of(expecteddt);

        assertThat(result, sameBeanAs(expected));
    }

    @Test
    void merge_inclusiveOnRightIsPassedIn_shouldReturnInclusive() {
        // ARRANGE
        DateTimeRestrictions left = new DateTimeRestrictions(new Limit<>(REFERENCE_TIME, false),
            DATETIME_MAX_LIMIT,
            Timescale.HOURS
        );

        DateTimeRestrictions right = new DateTimeRestrictions(
            new Limit<>(REFERENCE_TIME.plusSeconds(1), true),
            DATETIME_MAX_LIMIT,
            Timescale.MILLIS);

        // ACT
        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        // ASSERT
        DateTimeRestrictions expecteddt = new DateTimeRestrictions(new Limit<>(
            REFERENCE_TIME.plusHours(1), true),
            DATETIME_MAX_LIMIT,
            Timescale.HOURS);
        Optional<DateTimeRestrictions> expected = Optional.of(expecteddt);

        assertThat(result, sameBeanAs(expected));
    }

    @Test
    void merge_notInclusivePassedIn_shouldReturnNotInclusive() {
        // ARRANGE
        DateTimeRestrictions left = new DateTimeRestrictions(
            new Limit<>(REFERENCE_TIME, false),
            DATETIME_MAX_LIMIT,
            Timescale.HOURS);

        DateTimeRestrictions right = new DateTimeRestrictions(
            new Limit<>(REFERENCE_TIME.plusHours(1), false),
            DATETIME_MAX_LIMIT,
            Timescale.MILLIS);

        // ACT
        Optional<LinearRestrictions<OffsetDateTime>> result = merger.merge(left, right);

        // ASSERT
        DateTimeRestrictions expecteddt = new DateTimeRestrictions(
            new Limit<>(REFERENCE_TIME.plusHours(1), false),
            DATETIME_MAX_LIMIT,
            Timescale.HOURS);
        Optional<DateTimeRestrictions> expected = Optional.of(expecteddt);

        assertThat(result, sameBeanAs(expected));
    }
}
