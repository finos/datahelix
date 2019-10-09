package com.scottlogic.deg.common.util.defaults;

import com.scottlogic.deg.common.profile.constraintdetail.Granularity;

import java.math.BigDecimal;

import static com.scottlogic.deg.common.util.Defaults.*;

public class NumericDefaults implements LinearDefaults<BigDecimal> {

    private static NumericDefaults singleton;
    private NumericDefaults(){ }
    public static synchronized NumericDefaults get() {
        if (singleton == null)
            singleton = new NumericDefaults();
        return singleton;
    }

    @Override
    public BigDecimal min() {
        return NUMERIC_MIN;
    }

    @Override
    public BigDecimal max() {
        return NUMERIC_MAX;
    }

    @Override
    public Granularity<BigDecimal> granularity() {
        return DEFAULT_NUMERIC_GRANULARITY;
    }
}
