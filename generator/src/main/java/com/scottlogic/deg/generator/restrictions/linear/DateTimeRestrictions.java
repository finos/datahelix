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

package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;

import java.time.OffsetDateTime;
import java.util.Objects;

import static com.scottlogic.deg.common.util.Defaults.*;

public class DateTimeRestrictions extends LinearRestrictions<OffsetDateTime> {

    public static final DateTimeLimit DATETIME_MIN_LIMIT = new DateTimeLimit(ISO_MIN_DATE, true);
    public static final DateTimeLimit DATETIME_MAX_LIMIT = new DateTimeLimit(ISO_MAX_DATE, true);
    private static final DateTimeConverter CONVERTER = new DateTimeConverter();

    public DateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max) {
        this(min, max, DEFAULT_DATETIME_GRANULARITY);
    }

    public DateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max, Timescale granularity) {
        this(min, max, new DateTimeGranularity(granularity));
    }

    public DateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max, Granularity<OffsetDateTime> granularity){
        super(min, max, granularity, CONVERTER);
    }

    @Override
    public String toString() {
        return "min=" + getMin() + ", max=" + getMax() + " " + getGranularity().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimeRestrictions that = (DateTimeRestrictions) o;
        return Objects.equals(getMin(), that.getMin()) &&
            Objects.equals(getMax(), that.getMax());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMin(), getMax());
    }
}
