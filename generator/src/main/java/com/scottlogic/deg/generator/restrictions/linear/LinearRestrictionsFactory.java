package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static com.scottlogic.deg.common.util.Defaults.*;
import static com.scottlogic.deg.common.util.Defaults.ISO_MAX_DATE;

public class LinearRestrictionsFactory {
    private static final DateTimeConverter DATE_TIME_CONVERTER = new DateTimeConverter();
    public static final NumericConverter NUMERIC_CONVERTER = new NumericConverter();

    public static LinearRestrictions<OffsetDateTime> createDateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max) {
        return createDateTimeRestrictions(min, max, DEFAULT_DATETIME_GRANULARITY);
    }

    public static LinearRestrictions<OffsetDateTime> createDateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max, Timescale granularity) {
        Limit<OffsetDateTime> cappedMin = capMin(min, ISO_MIN_DATE, ISO_MAX_DATE);
        Limit<OffsetDateTime> cappedMax = capMax(max, ISO_MIN_DATE, ISO_MAX_DATE);
        return new LinearRestrictions<>(cappedMin, cappedMax, new DateTimeGranularity(granularity), DATE_TIME_CONVERTER);
    }

    public static NumericRestrictions createNumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max) {
        return createNumericRestrictions(min, max, DEFAULT_NUMERIC_SCALE);
    }

    public static NumericRestrictions createNumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max, int numericScale) {
        Limit<BigDecimal> cappedMin = capMin(min, NUMERIC_MIN, NUMERIC_MAX);
        Limit<BigDecimal> cappedMax = capMax(max, NUMERIC_MIN, NUMERIC_MAX);
        return new NumericRestrictions(cappedMin, cappedMax, new NumericGranularity(numericScale), NUMERIC_CONVERTER);
    }


    private static <T extends Comparable<? super T>> Limit<T> capMin(Limit<T> min, T minCap, T maxCap) {
        if (min.isBefore(minCap)) {
            return new Limit<>(minCap, true);
        } else if (!min.isBefore(maxCap)) {
            return new Limit<>(maxCap, false);
        } else {
            return min;
        }
    }

    private static <T extends Comparable<? super T>> Limit<T> capMax(Limit<T> max, T minCap, T maxCap) {
        if (max.isAfter(maxCap)) {
            return new Limit<>(maxCap, true);
        } else if (!max.isAfter(minCap)) {
            return new Limit<>(minCap, false);
        } else {
            return max;
        }
    }
}
