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

package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.time.*;
import java.util.Iterator;

class RandomDateGenerator {
    private final OffsetDateTime minDate;
    private final OffsetDateTime maxDate;
    private final RandomNumberGenerator random;
    private final Timescale granularity;

    RandomDateGenerator(OffsetDateTime minDate, OffsetDateTime maxDate, RandomNumberGenerator randomNumberGenerator, Timescale granularity) {
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.random = randomNumberGenerator;
        this.granularity = granularity;
    }

    public OffsetDateTime next() {
        long min = getMilli(minDate);
        long max = getMilli(maxDate) - 1;

        long generatedLong = (long) random.nextDouble(min, max);

        OffsetDateTime generatedDate = Instant.ofEpochMilli(generatedLong).atZone(ZoneOffset.UTC).toOffsetDateTime();

        return trimUnwantedGranularity(generatedDate, granularity);
    }

    private long getMilli(OffsetDateTime date) {
        return date.toInstant().toEpochMilli();
    }

    private OffsetDateTime trimUnwantedGranularity(OffsetDateTime dateToTrim, Timescale granularity) {

        // Remove unneeded granularity from the dateToTrim.
        // For example: if a granularity of days is passed in; all smaller units of time will be set to the lowest possible value.
        // (hours, minutes, seconds and milliseconds will all be set to 0)
        return granularity.getGranularityFunction().apply(dateToTrim);
    }
}
