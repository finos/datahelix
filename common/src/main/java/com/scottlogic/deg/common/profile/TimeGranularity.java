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

package com.scottlogic.deg.common.profile;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class TimeGranularity implements Granularity<LocalTime>{

    private final ChronoUnit granularity;

    public TimeGranularity(ChronoUnit granularity) {
        if (!granularity.isTimeBased()){
            throw new ValidationException("Granularity for time field must be time based but was " +
                granularity.toString());
        }

        this.granularity = granularity;}

    public static TimeGranularity create(String granularity) {
        return new TimeGranularity(ChronoUnit.valueOf(granularity.toUpperCase()));
    }

    @Override
    public boolean isCorrectScale(LocalTime value) {
        return trimToGranularity(value).equals(value);
    }

    @Override
    public Granularity<LocalTime> merge(Granularity<LocalTime> otherGranularity) {
        return ((TimeGranularity) otherGranularity).granularity.compareTo(granularity) > 0
            ? otherGranularity
            : this;
    }

    @Override
    public LocalTime getNext(LocalTime value, int amount) {
        value = value.truncatedTo(granularity);
        return value.plus(granularity.getDuration().multipliedBy(amount));
    }

    @Override
    public LocalTime trimToGranularity(LocalTime value) {
        return value.truncatedTo(granularity);
    }

    @Override
    public LocalTime getPrevious(LocalTime value) {
        if (!isCorrectScale(value)) {
            return trimToGranularity(value);
        }
        return value.minus(granularity.getDuration());
    }

    @Override
    public LocalTime getRandom(LocalTime min, LocalTime max, RandomNumberGenerator randomNumberGenerator) {
        long a = min.until(max, granularity);
        double b = randomNumberGenerator.nextDouble(0, (double)a);
        return min.plus((long)b,granularity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeGranularity that = (TimeGranularity) o;
        return granularity == that.granularity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(granularity);
    }

    @Override
    public String toString() {
        return "TimeGranularity{" +
            "granularity=" + granularity +
            '}';
    }
}
