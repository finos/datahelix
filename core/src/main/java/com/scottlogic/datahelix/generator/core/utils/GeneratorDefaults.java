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

package com.scottlogic.datahelix.generator.core.utils;

import com.scottlogic.datahelix.generator.common.util.Defaults;
import com.scottlogic.datahelix.generator.core.restrictions.linear.Limit;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;


public class GeneratorDefaults {
    public static final Limit<BigDecimal> NUMERIC_MAX_LIMIT = new Limit<>(Defaults.NUMERIC_MAX, true);
    public static final Limit<BigDecimal> NUMERIC_MIN_LIMIT= new Limit<>(Defaults.NUMERIC_MIN, true);

    public static final Limit<OffsetDateTime> DATETIME_MAX_LIMIT = new Limit<>(Defaults.ISO_MAX_DATE, true);
    public static final Limit<OffsetDateTime> DATETIME_MIN_LIMIT = new Limit<>(Defaults.ISO_MIN_DATE, true);

    public static final Limit<LocalTime> TIME_MAX_LIMIT = new Limit<>(LocalTime.MAX, true);
    public static final Limit<LocalTime> TIME_MIN_LIMIT = new Limit<>(LocalTime.MIN, true);
}
