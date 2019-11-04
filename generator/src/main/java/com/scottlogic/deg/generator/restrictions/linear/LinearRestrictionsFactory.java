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

import com.scottlogic.deg.common.profile.Granularity;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;

import static com.scottlogic.deg.common.util.Defaults.*;
import static com.scottlogic.deg.generator.utils.Defaults.*;

public class LinearRestrictionsFactory {

    public static LinearRestrictions<OffsetDateTime> createDefaultDateTimeRestrictions() {
        return createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT);
    }

    public static LinearRestrictions<OffsetDateTime> createDateTimeRestrictions(
        Limit<OffsetDateTime> min,
        Limit<OffsetDateTime> max) {

        return createDateTimeRestrictions(min, max, DEFAULT_DATETIME_GRANULARITY);
    }

    public static LinearRestrictions<OffsetDateTime> createDateTimeRestrictions(
        Limit<OffsetDateTime> min,
        Limit<OffsetDateTime> max,
        Granularity<OffsetDateTime> granularity) {

        OffsetDateTime inclusiveMin = getInclusiveMin(min, granularity, ISO_MIN_DATE);
        OffsetDateTime inclusiveMax = getInclusiveMax(max, granularity, ISO_MAX_DATE);
        return new LinearRestrictions<>(inclusiveMin, inclusiveMax, granularity);
    }

    public static LinearRestrictions<LocalTime> createDefaultTimeRestrictions() {
        return createTimeRestrictions(DEFAULT_TIME_GRANULARITY);
    }

    public static LinearRestrictions<LocalTime> createTimeRestrictions(Granularity<LocalTime> granularity) {
        return createTimeRestrictions(TIME_MIN_LIMIT, TIME_MAX_LIMIT, granularity);
    }

    public static LinearRestrictions<LocalTime> createTimeRestrictions(
        Limit<LocalTime> min,
        Limit<LocalTime> max) {
        return createTimeRestrictions(min, max, DEFAULT_TIME_GRANULARITY);
    }

    public static LinearRestrictions<LocalTime> createTimeRestrictions(
        Limit<LocalTime> min,
        Limit<LocalTime> max,
        Granularity<LocalTime> granularity) {
        return new LinearRestrictions<>(min.getValue(), max.getValue(), granularity);
    }

    public static LinearRestrictions<BigDecimal> createDefaultNumericRestrictions() {
        return createNumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT);
    }

    public static LinearRestrictions<BigDecimal> createNumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max) {
        return createNumericRestrictions(min, max, DEFAULT_NUMERIC_GRANULARITY);
    }

    public static LinearRestrictions<BigDecimal> createNumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max, Granularity<BigDecimal> granularity) {
        BigDecimal inclusiveMin = getInclusiveMin(min, granularity, NUMERIC_MIN);
        BigDecimal inclusiveMax = getInclusiveMax(max, granularity, NUMERIC_MAX);
        return new LinearRestrictions<>(inclusiveMin, inclusiveMax, granularity);
    }

    private static <T extends Comparable<? super T>> T getInclusiveMin(Limit<T> min, Granularity<T> granularity, T actualMin) {
        if (min.getValue().compareTo(actualMin) < 0) {
            return actualMin;
        }

        return min.isInclusive()
            ? min.getValue()
            : granularity.getNext(granularity.trimToGranularity(min.getValue()));
    }

    private static <T extends Comparable<? super T>> T getInclusiveMax(Limit<T> max, Granularity<T> granularity, T actualMax) {
        if (max.getValue().compareTo(actualMax) > 0) {
            return actualMax;
        }

        return max.isInclusive()
            ? max.getValue()
            : granularity.getPrevious(max.getValue());
    }
}
