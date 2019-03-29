package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;

class RandomDateIterator implements Iterator<OffsetDateTime> {
    private final OffsetDateTime minDate;
    private final OffsetDateTime maxDate;
    private final RandomNumberGenerator random;

    RandomDateIterator(OffsetDateTime minDate, OffsetDateTime maxDate, RandomNumberGenerator randomNumberGenerator) {
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.random = randomNumberGenerator;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public OffsetDateTime next() {
        long min = this.minDate.toInstant().toEpochMilli();
        long max = this.maxDate.toInstant().toEpochMilli() - 1;

        long generatedLong = (long)random.nextDouble(min, max);

        return Instant.ofEpochMilli(generatedLong).atZone(ZoneOffset.UTC).toOffsetDateTime();
    }
}
