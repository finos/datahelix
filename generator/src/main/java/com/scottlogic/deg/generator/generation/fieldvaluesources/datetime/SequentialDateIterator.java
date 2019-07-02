Copyright 2019 Scott Logic Ltd /
/
Licensed under the Apache License, Version 2.0 (the \"License\");/
you may not use this file except in compliance with the License./
You may obtain a copy of the License at/
/
    http://www.apache.org/licenses/LICENSE-2.0/
/
Unless required by applicable law or agreed to in writing, software/
distributed under the License is distributed on an \"AS IS\" BASIS,/
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied./
See the License for the specific language governing permissions and/
limitations under the License.
package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;

import java.time.OffsetDateTime;
import java.util.Iterator;

class SequentialDateIterator implements Iterator<OffsetDateTime> {
    private final OffsetDateTime maxDate;
    private final Timescale granularityUnit;

    private OffsetDateTime current;
    private boolean hasNext;

    SequentialDateIterator(OffsetDateTime inclusiveMinDate, OffsetDateTime exclusiveMaxDate, Timescale granularity) {
        maxDate = exclusiveMaxDate;
        granularityUnit = granularity;
        current = roundUpToGranularity(inclusiveMinDate);
        hasNext = current.compareTo(exclusiveMaxDate) < 0;
    }

    /**
     * initial datetime is rounded up after granularity is applied (if the returned datetime would have been lower than the initial datetime.)
     * This can be used to ensure a datetime is not lower than a previously specified minimum after granularity is applied.
     * 10:00 -> HOURS => 10:00
     * 10:01 -> HOURS => 11:00
     * @param initial initial datetime which will have granularity applied     *
     * @return datetime that has had granularity applied
     */
    private OffsetDateTime roundUpToGranularity(final OffsetDateTime initial) {
        OffsetDateTime earlierOrEqual = granularityUnit.getGranularityFunction().apply(initial);
        return earlierOrEqual.equals(initial) ? earlierOrEqual : granularityUnit.getNext().apply(earlierOrEqual);
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public OffsetDateTime next() {

        OffsetDateTime next = current;

        current = nextDate(current);
        if (current.isAfter(maxDate) || current.isEqual(maxDate)) {
            hasNext = false;
        }

        return next;
    }

    /**
     * Get the next available date at a given granularity.
     * 24/07/1990 -> days = 25/07/1990
     * 01/07/1990 -> months = 01/08/1990
     * @param previous input date
     * @return date after previous at given granularity
     */
    private OffsetDateTime nextDate(OffsetDateTime previous) {
        return granularityUnit.getNext().apply(previous);
    }
}

