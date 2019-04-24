package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import com.sun.scenario.effect.Offset;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Iterator;
import java.util.stream.Stream;

class SequentialDateIterator implements Iterator<OffsetDateTime> {
    private final OffsetDateTime maxDate;
    private final Timescale unit;

    private OffsetDateTime current;
    private boolean hasNext;

    SequentialDateIterator(OffsetDateTime inclusiveMinDate, OffsetDateTime exclusiveMaxDate, Timescale granularity) {
        maxDate = exclusiveMaxDate;
        unit = granularity;
        current = roundUpToGranularity(inclusiveMinDate, granularity);
        hasNext = current.compareTo(exclusiveMaxDate) < 0;
    }

    private OffsetDateTime roundUpToGranularity(final OffsetDateTime initial, final Timescale unit) {
        OffsetDateTime earlierOrEqual = unit.getGranularityFunction().apply(initial);
        return earlierOrEqual.equals(initial) ? earlierOrEqual : unit.getNext().apply(earlierOrEqual);
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

