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

package com.scottlogic.deg.common.util;

import com.scottlogic.deg.common.profile.constraintdetail.DateTimeGranularity;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class Defaults {

    public static final long DEFAULT_MAX_ROWS = 1000;

    public static final BigDecimal NUMERIC_MAX = new BigDecimal("1e20");
    public static final BigDecimal NUMERIC_MIN = new BigDecimal("-1e20");
    public static final int MAX_STRING_LENGTH = 1000;
    public static final OffsetDateTime ISO_MAX_DATE = OffsetDateTime.of(9999, 12, 31, 23, 59, 59, 999_000_000, ZoneOffset.UTC);
    public static final OffsetDateTime ISO_MIN_DATE = OffsetDateTime.of(1, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    public static final DateTimeGranularity DEFAULT_DATETIME_GRANULARITY = new DateTimeGranularity(ChronoUnit.MILLIS, false);
    public static final NumericGranularity DEFAULT_NUMERIC_GRANULARITY = new NumericGranularity(20);
}
