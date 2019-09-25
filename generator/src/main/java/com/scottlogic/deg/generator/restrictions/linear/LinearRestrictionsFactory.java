package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;

import java.time.OffsetDateTime;

import static com.scottlogic.deg.common.util.Defaults.*;
import static com.scottlogic.deg.common.util.Defaults.ISO_MAX_DATE;

public class LinearRestrictionsFactory {
    private static final DateTimeConverter DATE_TIME_CONVERTER = new DateTimeConverter();

    public static DateTimeRestrictions createDateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max) {
        return createDateTimeRestrictions(min, max, DEFAULT_DATETIME_GRANULARITY);
    }

    public static DateTimeRestrictions createDateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max, Timescale granularity) {
        return new DateTimeRestrictions(capMin(min), capMax(max), new DateTimeGranularity(granularity), DATE_TIME_CONVERTER);
    }

    private static Limit<OffsetDateTime> capMax(Limit<OffsetDateTime> max) {
        if (max.isAfter(ISO_MAX_DATE)) {
            return new Limit<>(ISO_MAX_DATE, true);
        } else if (!max.isAfter(ISO_MIN_DATE)) {
            return new Limit<>(ISO_MIN_DATE, false);
        } else {
            return max;
        }
    }

    private static Limit<OffsetDateTime> capMin(Limit<OffsetDateTime> min) {
        if (min.isBefore(ISO_MIN_DATE)) {
            return new Limit<>(ISO_MIN_DATE, true);
        } else if (!min.isBefore(ISO_MAX_DATE)) {
            return new Limit<>(ISO_MAX_DATE, false);
        } else {
            return min;
        }
    }
}
