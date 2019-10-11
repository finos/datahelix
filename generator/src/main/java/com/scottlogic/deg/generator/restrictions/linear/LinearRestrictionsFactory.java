package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.common.profile.constraintdetail.Granularity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static com.scottlogic.deg.common.util.Defaults.*;
import static com.scottlogic.deg.generator.utils.Defaults.*;

public class LinearRestrictionsFactory {

    public static LinearRestrictions<OffsetDateTime> createDefaultDateTimeRestrictions() {
        return createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT);
    }

    public static LinearRestrictions<OffsetDateTime> createDateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max) {
        return createDateTimeRestrictions(min, max, DEFAULT_DATETIME_GRANULARITY);
    }

    public static LinearRestrictions<OffsetDateTime> createDateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max, Granularity<OffsetDateTime> granularity) {
        OffsetDateTime inclusiveMin = getInclusiveMin(min, granularity, ISO_MIN_DATE);
        OffsetDateTime inclusiveMax = getInclusiveMax(max, granularity, ISO_MAX_DATE);
        return new LinearRestrictions<>(inclusiveMin, inclusiveMax, granularity);
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
        if (min.getValue().compareTo(actualMin) < 0){
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
