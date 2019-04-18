package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Iterator;

class SequentialDateIterator implements Iterator<OffsetDateTime> {
    private final OffsetDateTime maxDate;
    private final Timescale unit;

    private OffsetDateTime current;
    private boolean hasNext;

    SequentialDateIterator(OffsetDateTime inclusiveMinDate, OffsetDateTime exclusiveMaxDate, Timescale granularity) {
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

        current = nextDate(current, unit);
        if (current.isAfter(maxDate) || current.isEqual(maxDate)) {
            hasNext = false;
        }

        return next;
    }

    private OffsetDateTime nextDate(OffsetDateTime previous, Timescale granularity) {
        return granularity.getNext().apply(previous);
    }
}

