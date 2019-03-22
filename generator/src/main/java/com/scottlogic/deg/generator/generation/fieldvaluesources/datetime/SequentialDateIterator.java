package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

public class SequentialDateIterator implements Iterator<LocalDateTime> {
    private final LocalDateTime maxDate;
    private final ChronoUnit unit;

    private LocalDateTime current;
    private boolean hasNext;

    SequentialDateIterator(LocalDateTime inclusiveMinDate, LocalDateTime exclusiveMaxDate, ChronoUnit granularity) {
        this.maxDate = exclusiveMaxDate;

        current = inclusiveMinDate;

        unit = granularity;
        hasNext = current.compareTo(exclusiveMaxDate) < 0;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public LocalDateTime next() {

        LocalDateTime next = current;

        current = current.plus(1, unit);
        if (current.isAfter(maxDate) || current.isEqual(maxDate)) {
            hasNext = false;
        }

        return next;
    }
}

