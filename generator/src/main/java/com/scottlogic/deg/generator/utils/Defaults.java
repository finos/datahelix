package com.scottlogic.deg.generator.utils;

import com.scottlogic.deg.generator.restrictions.linear.Limit;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static com.scottlogic.deg.common.util.Defaults.*;

public class Defaults {

    public static final Limit<BigDecimal> NUMERIC_MAX_LIMIT = new Limit<>(NUMERIC_MAX, true);
    public static final Limit<BigDecimal> NUMERIC_MIN_LIMIT= new Limit<>(NUMERIC_MIN, true);

    public static final Limit<OffsetDateTime> DATETIME_MAX_LIMIT = new Limit<>(ISO_MAX_DATE, true);
    public static final Limit<OffsetDateTime> DATETIME_MIN_LIMIT = new Limit<>(ISO_MIN_DATE, true);

}
