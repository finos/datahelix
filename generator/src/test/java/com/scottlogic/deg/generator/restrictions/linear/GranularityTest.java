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


package com.scottlogic.deg.generator.restrictions.linear;
import com.scottlogic.deg.common.profile.constraintdetail.DateTimeGranularity;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.YEARS;

class GranularityTest {

    @Test
    public void numericRestrictions_nextCorrectlyGetsNextValidValue(){
        NumericGranularity granularity = new NumericGranularity(0);
        BigDecimal num = new BigDecimal(5);

        BigDecimal result = granularity.getNext(num);

        BigDecimal expectedResult = new BigDecimal(6);
        assertThat(result, sameBeanAs(expectedResult));
    }

    @Test
    public void numericRestrictions_getPrevious(){
        NumericGranularity granularity = new NumericGranularity(0);
        BigDecimal num = new BigDecimal(5);

        BigDecimal result = granularity.getPrevious(num);

        BigDecimal expectedResult = new BigDecimal(4);
        assertThat(result, sameBeanAs(expectedResult));
    }

    @Test
    public void numericRestrictions_getPreviousBetweenGranularity(){
        NumericGranularity granularity = new NumericGranularity(0);
        BigDecimal num = new BigDecimal(5.5);

        BigDecimal result = granularity.getPrevious(num);

        BigDecimal expectedResult = new BigDecimal(5);
        assertThat(result, sameBeanAs(expectedResult));
    }

    @Test
    public void dateTimeRestrictions_getPrevious(){
        DateTimeGranularity granularity = new DateTimeGranularity(YEARS);
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        OffsetDateTime result = granularity.getPrevious(offsetDateTime);
        OffsetDateTime expectedResult = OffsetDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        assertThat(result, sameBeanAs(expectedResult));
    }

    @Test
    public void dateTimeRestrictionsMillis_getPrevious(){
        DateTimeGranularity granularity = new DateTimeGranularity(MILLIS);
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2018, 1, 1, 0, 0, 0, 1_000_000, ZoneOffset.UTC);

        OffsetDateTime result = granularity.getPrevious(offsetDateTime);
        OffsetDateTime expectedResult = OffsetDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        assertThat(result, sameBeanAs(expectedResult));
    }

    @Test
    public void dateTimeRestrictions_getPreviousBetweenGranularity(){
        DateTimeGranularity granularity = new DateTimeGranularity(YEARS);
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2018, 7, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        OffsetDateTime result = granularity.getPrevious(offsetDateTime);
        OffsetDateTime expectedResult = OffsetDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

        assertThat(result, sameBeanAs(expectedResult));
    }


}