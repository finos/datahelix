package com.scottlogic.deg.common.util.defaults;

import com.scottlogic.deg.common.profile.Granularity;

import java.time.LocalTime;

import static com.scottlogic.deg.common.util.Defaults.*;

public class TimeDefaults implements LinearDefaults {


    private static TimeDefaults singleton;
    private TimeDefaults(){ }
    public static synchronized TimeDefaults get() {
        if (singleton == null)
            singleton = new TimeDefaults();
        return singleton;
    }

    @Override
    public LocalTime min() {
        return TIME_MIN;
    }

    @Override
    public LocalTime max() {
        return TIME_MAX;
    }

    @Override
    public Granularity<LocalTime> granularity() {
        return DEFAULT_TIME_GRANULARITY;
    }

}
