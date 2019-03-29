package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Iterator;

class SequentialDateIterator implements Iterator<OffsetDateTime> {
    private final OffsetDateTime maxDate;
    private final TemporalUnit unit;

    private OffsetDateTime current;
    private boolean hasNext;

    SequentialDateIterator(OffsetDateTime inclusiveMinDate, OffsetDateTime exclusiveMaxDate, TemporalUnit granularity) {
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
    public OffsetDateTime next() {

        OffsetDateTime next = current;

        current = current.plus(1, unit);
        if (current.isAfter(maxDate) || current.isEqual(maxDate)) {
            hasNext = false;
        }

        return next;
    }
}

