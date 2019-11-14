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

import com.scottlogic.datahelix.generator.common.profile.Granularity;
import com.scottlogic.datahelix.generator.common.util.Defaults;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;

import static com.scottlogic.datahelix.generator.common.utils.GeneratorDefaults.*;

public class LinearRestrictionsFactory {

    public static LinearRestrictions<OffsetDateTime> createDefaultDateTimeRestrictions() {
        return createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT);
    }

    public static LinearRestrictions<OffsetDateTime> createDateTimeRestrictions(
        Limit<OffsetDateTime> min,
        Limit<OffsetDateTime> max) {

        return createDateTimeRestrictions(min, max, Defaults.DEFAULT_DATETIME_GRANULARITY);
    }

    public static LinearRestrictions<OffsetDateTime> createDateTimeRestrictions(
        Limit<OffsetDateTime> min,
        Limit<OffsetDateTime> max,
        Granularity<OffsetDateTime> granularity) {

        OffsetDateTime inclusiveMin = getInclusiveMin(min, granularity, Defaults.ISO_MIN_DATE);
        OffsetDateTime inclusiveMax = getInclusiveMax(max, granularity, Defaults.ISO_MAX_DATE);
        return new LinearRestrictions<>(inclusiveMin, inclusiveMax, granularity);
    }

    public static LinearRestrictions<LocalTime> createDefaultTimeRestrictions() {
        return createTimeRestrictions(Defaults.DEFAULT_TIME_GRANULARITY);
    }

    public static LinearRestrictions<LocalTime> createTimeRestrictions(Granularity<LocalTime> granularity) {
        return createTimeRestrictions(TIME_MIN_LIMIT, TIME_MAX_LIMIT, granularity);
    }

    public static LinearRestrictions<LocalTime> createTimeRestrictions(
        Limit<LocalTime> min,
        Limit<LocalTime> max) {
        return createTimeRestrictions(min, max, Defaults.DEFAULT_TIME_GRANULARITY);
    }


    /**
     *This method creates a LocalTime LinearRestriction. If the upper or lower bounds are close to midnight and would
     * result in a contradictory restrictions, the method ensures that a contradictory LinearRestriction is created.
     * @param min
     * @param max
     * @param granularity
     * @return
     */
    public static LinearRestrictions<LocalTime> createTimeRestrictions(
        Limit<LocalTime> min,
        Limit<LocalTime> max,
        Granularity<LocalTime> granularity) {
        LocalTime inclusiveMin = getInclusiveMin(min, granularity, TIME_MIN_LIMIT.getValue());
        LocalTime inclusiveMax = getInclusiveMax(max, granularity, TIME_MAX_LIMIT.getValue());

        if (inclusiveMax.compareTo(max.getValue()) > 0
            || inclusiveMax.equals(Defaults.TIME_MIN) && !max.isInclusive()){
            inclusiveMax = Defaults.TIME_MIN;
            inclusiveMin = Defaults.TIME_MAX;
        } else if (inclusiveMin.compareTo(max.getValue()) > 0
            || inclusiveMin.equals(Defaults.TIME_MAX) && !min.isInclusive()){
            inclusiveMax = Defaults.TIME_MIN;
            inclusiveMin = Defaults.TIME_MAX;
        }

        return new LinearRestrictions<>(inclusiveMin, inclusiveMax, granularity);
    }

    public static LinearRestrictions<BigDecimal> createDefaultNumericRestrictions() {
        return createNumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT);
    }

    public static LinearRestrictions<BigDecimal> createNumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max) {
        return createNumericRestrictions(min, max, Defaults.DEFAULT_NUMERIC_GRANULARITY);
    }

    public static LinearRestrictions<BigDecimal> createNumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max, Granularity<BigDecimal> granularity) {
        BigDecimal inclusiveMin = getInclusiveMin(min, granularity, Defaults.NUMERIC_MIN);
        BigDecimal inclusiveMax = getInclusiveMax(max, granularity, Defaults.NUMERIC_MAX);
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
