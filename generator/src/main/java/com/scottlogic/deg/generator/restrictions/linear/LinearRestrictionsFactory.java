package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.common.profile.constraintdetail.Granularity;
import com.scottlogic.deg.common.profile.constraintdetail.Timescale;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.function.Function;

import static com.scottlogic.deg.common.util.Defaults.*;

public class LinearRestrictionsFactory {

    public static LinearRestrictions<OffsetDateTime> createDateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max) {
        return createDateTimeRestrictions(min, max, DEFAULT_DATETIME_GRANULARITY);
    }

    public static LinearRestrictions<OffsetDateTime> createDateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max, Timescale granularity) {
        Limit<OffsetDateTime> cappedMin = capMin(min, ISO_MIN_DATE, ISO_MAX_DATE);
        Limit<OffsetDateTime> cappedMax = capMax(max, ISO_MIN_DATE, ISO_MAX_DATE);
        Limit<OffsetDateTime> inclusiveMin = getInclusiveMin(cappedMin, granularity);
        Limit<OffsetDateTime> inclusiveMax = getInclusiveMax(cappedMax, granularity, b->b.minusNanos(1));
        return new LinearRestrictions<>(inclusiveMin, inclusiveMax, granularity);
    }

    public static LinearRestrictions<BigDecimal> createNumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max) {
        return createNumericRestrictions(min, max, DEFAULT_NUMERIC_SCALE);
    }

    public static LinearRestrictions<BigDecimal> createNumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max, int numericScale) {
        NumericGranularity granularity = new NumericGranularity(numericScale);
        Limit<BigDecimal> cappedMin = capMin(min, NUMERIC_MIN, NUMERIC_MAX);
        Limit<BigDecimal> cappedMax = capMax(max, NUMERIC_MIN, NUMERIC_MAX);
        Limit<BigDecimal> inclusiveMin = getInclusiveMin(cappedMin, granularity);
        Limit<BigDecimal> inclusiveMax = getInclusiveMax(cappedMax, granularity, b->b.subtract(new BigDecimal("0.0000000000000000000001")));
        return new LinearRestrictions<>(inclusiveMin, inclusiveMax, granularity);
    }

    private static <T extends Comparable<? super T>> Limit<T> getInclusiveMin(Limit<T> min, Granularity<T> granularity) {
        return min.isInclusive()
            ? min
            : new Limit<>(granularity.getNext(granularity.trimToGranularity(min.getValue())), true);
    }

    private static <T extends Comparable<? super T>> Limit<T> getInclusiveMax(Limit<T> max, Granularity<T> granularity, Function<T, T> reduction) {
        return max.isInclusive()
            ? max
            : new Limit<>(granularity.trimToGranularity(reduction.apply(max.getValue())), true);
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
