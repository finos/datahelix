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
package com.scottlogic.deg.common.util.defaults;

import com.scottlogic.deg.common.profile.Granularity;

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
