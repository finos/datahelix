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

public class DateTimeGranularity implements Granularity<OffsetDateTime> {

    private final Timescale granularity;

    public DateTimeGranularity(Timescale granularity){
        this.granularity = granularity;
    }

    @Override
    public boolean isCorrectScale(OffsetDateTime value) {
        OffsetDateTime granularDate = granularity.getGranularityFunction().apply(value);

        return value.equals(granularDate);
    }

    @Override
    public Granularity<OffsetDateTime> merge(Granularity<OffsetDateTime> otherGranularity) {
        DateTimeGranularity other = (DateTimeGranularity) otherGranularity;
        if(granularity == Timescale.getMostCoarse(granularity, other.granularity)) {
            return this;
        } else {
            return otherGranularity;
        }
    }

    @Override
    public OffsetDateTime getNext(OffsetDateTime dateTime) {
        return granularity.getNext().apply(dateTime);
    }

    @Override
    public OffsetDateTime trimToGranularity(OffsetDateTime value) {
        return granularity.getGranularityFunction().apply(value);
    }

    @Override
    public String toString() {
        return "granularity=" + granularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimeGranularity that = (DateTimeGranularity) o;
        return granularity == that.granularity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(granularity);
    }
}
