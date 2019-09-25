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

import com.scottlogic.deg.common.profile.constraintdetail.Granularity;
import com.scottlogic.deg.common.profile.constraintdetail.Timescale;

import java.time.OffsetDateTime;

import static com.scottlogic.deg.common.util.Defaults.*;


public class DateTimeRestrictions extends LinearRestrictions<OffsetDateTime> {

    public DateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max) {
        this(min, max, DEFAULT_DATETIME_GRANULARITY);
    }

    public DateTimeRestrictions(Limit<OffsetDateTime> min, Limit<OffsetDateTime> max, Granularity<OffsetDateTime> granularity){
        super(capMin(min), capMax(max), granularity);
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

    @Override
    public String toString() {
        return "min=" + getMin() + ", max=" + getMax() + " " + getGranularity().toString();
    }
}
