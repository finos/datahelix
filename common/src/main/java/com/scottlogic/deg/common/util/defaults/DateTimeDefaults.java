package com.scottlogic.deg.common.util.defaults;

import com.scottlogic.deg.common.profile.constraintdetail.Granularity;

import java.time.OffsetDateTime;

import static com.scottlogic.deg.common.util.Defaults.*;

public class DateTimeDefaults implements LinearDefaults<OffsetDateTime> {

    private static DateTimeDefaults singleton;
    private DateTimeDefaults(){ }
    public static synchronized DateTimeDefaults get() {
        if (singleton == null)
            singleton = new DateTimeDefaults();
        return singleton;
    }

    @Override
    public OffsetDateTime min() {
        return ISO_MIN_DATE;
    }

    @Override
    public OffsetDateTime max() {
        return ISO_MAX_DATE;
    }

    @Override
    public Granularity<OffsetDateTime> granularity() {
        return DEFAULT_DATETIME_GRANULARITY;
    }
}
