package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.function.Function;

class RandomDateIterator implements Iterator<OffsetDateTime> {
    private final OffsetDateTime minDate;
    private final OffsetDateTime maxDate;
    private final RandomNumberGenerator random;
    private final Timescale granularity;

    RandomDateIterator(OffsetDateTime minDate, OffsetDateTime maxDate, RandomNumberGenerator randomNumberGenerator, Timescale granularity) {
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.random = randomNumberGenerator;
        this.granularity = granularity;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
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
