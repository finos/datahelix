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

    /**
     * initial datetime is rounded up after granularity is applied (if the returned datetime would have been lower than the initial datetime.)
     * This can be used to ensure a datetime is not lower than a previously specified minimum after granularity is applied.
     * 10:00 -> HOURS => 10:00
     * 10:01 -> HOURS => 11:00
     * @param initial initial datetime which will have granularity applied
     * @param unit unit of granularity
     * @return datetime that has had granularity applied
     */
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

