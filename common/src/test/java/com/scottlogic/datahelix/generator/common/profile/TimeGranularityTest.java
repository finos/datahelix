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

package com.scottlogic.datahelix.generator.common.profile;

import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.util.Defaults;
import com.scottlogic.datahelix.generator.common.RandomNumberGenerator;
import com.scottlogic.datahelix.generator.common.TestRandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


class TimeGranularityTest {

    @Test
    void isCorrectScale_returnsTrue_WithCorrectScale() {
        TimeGranularity granularity = new TimeGranularity(ChronoUnit.MINUTES);

        LocalTime time = LocalTime.of(6, 13);

        Assert.assertTrue(granularity.isCorrectScale(time));
    }

    @Test
    void isCorrectScale_returnsFalse_WithIncorrectScale() {
        TimeGranularity granularity = new TimeGranularity(ChronoUnit.MINUTES);

        LocalTime time = LocalTime.of(1, 39, 43);

        Assert.assertFalse(granularity.isCorrectScale(time));
    }

    @Test
    void merge_returnsMoreRestrictiveGranularity() {
        TimeGranularity granularity1 = new TimeGranularity(ChronoUnit.MINUTES);
        TimeGranularity granularity2 = new TimeGranularity(ChronoUnit.SECONDS);

        Assert.assertEquals(granularity1, granularity1.merge(granularity2));
    }

    @Test
    void getNext_returnsNextValidValue_whenPassedValueAtGranularity() {
        TimeGranularity granularity = new TimeGranularity(ChronoUnit.HOURS);
        LocalTime time = LocalTime.of(1, 0);
        LocalTime expected = LocalTime.of(2, 0);

        Assert.assertEquals(expected, granularity.getNext(time));
    }

    @Test
    void getNext_returnsNextValidValue_whenPassedValueBetweenGranularity() {
        TimeGranularity granularity = new TimeGranularity(ChronoUnit.MINUTES);
        LocalTime time = LocalTime.of(1, 14, 50);
        LocalTime expected = LocalTime.of(1, 15, 0);

        Assert.assertEquals(expected, granularity.getNext(time));
    }

    @Test
    void getNext_returnsMidnight_WhenPassedTimeNearMidnight() {
        TimeGranularity granularity = new TimeGranularity(ChronoUnit.SECONDS);
        LocalTime time = LocalTime.of(23, 59, 59, 234);
        LocalTime expected = Defaults.TIME_MAX;

        Assert.assertEquals(expected, granularity.getNext(time));
    }

    @Test
    void getNextMultiple_returnsAfterMidnight_WhenPassedTimeNearMidnight() {
        TimeGranularity granularity = new TimeGranularity(ChronoUnit.SECONDS);
        LocalTime time = LocalTime.of(23, 59, 59, 234);
        LocalTime expected = LocalTime.of(0, 0, 29);

        Assert.assertEquals(expected, granularity.getNext(time, 30));
    }

    @Test
    void trimToGranularity_truncatesTime() {
        TimeGranularity granularity = new TimeGranularity(ChronoUnit.MINUTES);

        LocalTime time = LocalTime.ofSecondOfDay(110);
        LocalTime expected = LocalTime.ofSecondOfDay(60);

        Assert.assertEquals(expected, granularity.trimToGranularity(time));
    }

    @Test
    void getPrevious_betweenGranularity_returnsTruncatedGranularity() {
        TimeGranularity granularity = new TimeGranularity(ChronoUnit.MINUTES);
        LocalTime time = LocalTime.of(12, 30, 15);
        LocalTime expected = LocalTime.of(12, 30, 0);

        Assert.assertEquals(expected, granularity.getPrevious(time));
    }

    @Test
    void getPrevious_onGranularity_returnsPreviousTime() {
        TimeGranularity granularity = new TimeGranularity(ChronoUnit.HOURS);
        LocalTime time = LocalTime.of(12, 0, 0);
        LocalTime expected = LocalTime.of(11, 0, 0);

        Assert.assertEquals(expected, granularity.getPrevious(time));
    }

    @Test
    void getRandom_generatesValidValue() {
        TimeGranularity granularity = new TimeGranularity(ChronoUnit.SECONDS);
        RandomNumberGenerator generator = new TestRandomNumberGenerator();

        LocalTime result = granularity.getRandom(LocalTime.MIN, LocalTime.MAX, generator);
        Assert.assertTrue(granularity.isCorrectScale(result));
    }

    @Test
    void creatingAnInvalidTimeGranularity_throwsAnError() {
        Assertions.assertThrows(ValidationException.class, () -> {
            new TimeGranularity(ChronoUnit.MONTHS);
        });
    }

    @Test
    void create_returnsTimeGranularityFromString() {
        TimeGranularity timeGranularity = TimeGranularity.create("millis");
        TimeGranularity expected = new TimeGranularity(ChronoUnit.MILLIS);

        Assert.assertEquals(expected, timeGranularity);
    }

}
