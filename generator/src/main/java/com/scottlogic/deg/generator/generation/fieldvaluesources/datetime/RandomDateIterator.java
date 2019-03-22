package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;

public class RandomDateIterator implements Iterator<LocalDateTime> {
    private final LocalDateTime minDate;
    private final LocalDateTime maxDate;
    private final RandomNumberGenerator random;

    RandomDateIterator(LocalDateTime minDate, LocalDateTime maxDate, RandomNumberGenerator randomNumberGenerator) {
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.random = randomNumberGenerator;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public LocalDateTime next() {
        long min = this.minDate.toInstant(ZoneOffset.UTC).toEpochMilli();
        long max = this.maxDate.toInstant(ZoneOffset.UTC).toEpochMilli() - 1;

        long generatedLong = (long)random.nextDouble(min, max);

        return Instant.ofEpochMilli(generatedLong).atZone(ZoneOffset.UTC).toLocalDateTime();
    }
}
