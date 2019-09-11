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
import com.scottlogic.deg.generator.restrictions.DateTimeLimit;
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;

import java.time.OffsetDateTime;
import java.util.Objects;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.DATETIME;

public class DateTimeRestrictions implements TypedRestrictions {
    private static final Timescale DEFAULT_GRANULARITY = Timescale.MILLIS;
    private final Timescale granularity;
    public DateTimeLimit min;
    public DateTimeLimit max;

    public DateTimeRestrictions() {
        this(DEFAULT_GRANULARITY);
    }

    public DateTimeRestrictions(final Timescale granularity) {
        this.granularity = granularity;
    }

    public Timescale getGranularity() {
        return granularity;
    }

    @Override
    public String toString() {
        return "min=" + min + ", max=" + max + " " + granularity.name();
    }


    @Override
    public boolean match(Object o) {
        if (!isInstanceOf(o)) {
            return false;
        }

        OffsetDateTime d = (OffsetDateTime) o;

        if (min != null) {
            if (d.compareTo(min.getLimit()) < (min.isInclusive() ? 0 : 1)) {
                return false;
            }
        }

        if (max != null) {
            if (d.compareTo(max.getLimit()) > (max.isInclusive() ? 0 : -1)) {
                return false;
            }
        }

        return isCorrectGranularity(d);
    }

    @Override
    public boolean isInstanceOf(Object o) {
        return DATETIME.isInstanceOf(o);
    }

    private boolean isCorrectGranularity(OffsetDateTime inputDate) {
        OffsetDateTime granularDate = granularity.getGranularityFunction().apply(inputDate);

        return inputDate.equals(granularDate);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimeRestrictions that = (DateTimeRestrictions) o;
        return Objects.equals(min, that.min) &&
            Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

}
