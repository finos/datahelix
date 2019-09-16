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
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeGranularity;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions;
import com.scottlogic.deg.generator.utils.FilteringIterator;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.time.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.scottlogic.deg.generator.utils.SetUtils.stream;

public class DateTimeFieldValueSource implements FieldValueSource {

    public static final OffsetDateTime ISO_MAX_DATE = OffsetDateTime.of(9999, 12, 31, 23, 59, 59, 999_999_999, ZoneOffset.UTC);
    public static final OffsetDateTime ISO_MIN_DATE = OffsetDateTime.of(1, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    private final Timescale granularity;
    private final DateTimeRestrictions restrictions;
    private final Set<Object> blacklist;
    private final OffsetDateTime inclusiveLower;
    private final OffsetDateTime exclusiveUpper;

    public DateTimeFieldValueSource(
        DateTimeRestrictions restrictions,
        Set<Object> blacklist) {

        this.restrictions = restrictions;
        this.granularity = ((DateTimeGranularity)restrictions.getGranularity()).getTimeScale();

        this.inclusiveLower = getInclusiveLowerBounds(restrictions);
        this.exclusiveUpper = getExclusiveUpperBound(restrictions);

        this.blacklist = blacklist;
    }

    @Override
    public Stream<Object> generateAllValues() {
        Iterator<OffsetDateTime> sequentialDateIterator = new SequentialDateIterator(
            inclusiveLower != null ? inclusiveLower : ISO_MIN_DATE,
            exclusiveUpper != null ? exclusiveUpper : ISO_MAX_DATE,
            granularity);

        return stream(sequentialDateIterator)
            .filter(i -> !blacklist.contains(i))
            .map(Function.identity());
    }

    @Override
    public Stream<Object> generateInterestingValues() {

        ArrayList<Object> interestingValues = new ArrayList<>();

        if (restrictions.getMin() != null && restrictions.getMin().getValue() != null) {
            OffsetDateTime min = restrictions.getMin().getValue();
            interestingValues.add(restrictions.getMin().isInclusive() ? min : min.plusNanos(1_000_000));
        } else {
            interestingValues.add(OffsetDateTime.of(
                LocalDate.of(1900, 01, 01),
                LocalTime.MIDNIGHT,
                ZoneOffset.UTC));
        }

        if (restrictions.getMax() != null && restrictions.getMax().getValue() != null) {
            OffsetDateTime max = restrictions.getMax().getValue();
            interestingValues.add(restrictions.getMax().isInclusive() ? max : max.minusNanos(1_000_000));
        } else {
            interestingValues.add(OffsetDateTime.of(
                LocalDate.of(2100, 01, 01),
                LocalTime.MIDNIGHT,
                ZoneOffset.UTC));
        }

        return interestingValues.stream()
            .filter(i -> !blacklist.contains(i));
    }

    @Override
    public Stream<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {

        OffsetDateTime lower = inclusiveLower != null
            ? inclusiveLower
            : ISO_MIN_DATE;


        OffsetDateTime upper = exclusiveUpper != null
            ? exclusiveUpper
            : ISO_MAX_DATE.plusNanos(1_000_000);

        RandomDateGenerator randomDateGenerator = new RandomDateGenerator(lower, upper, randomNumberGenerator, granularity);
        return Stream.generate(() -> randomDateGenerator.next())
            .filter(i -> !blacklist.contains(i))
            .map(Function.identity());

    }

    private OffsetDateTime getExclusiveUpperBound(DateTimeRestrictions upper) {
        if (upper.getMax() == null || upper.getMax().getValue() == null) return null;
        return upper.getMax().isInclusive() ? upper.getMax().getValue().plusNanos(1_000_000) : upper.getMax().getValue();
    }

    private OffsetDateTime getInclusiveLowerBounds(DateTimeRestrictions lower) {
        if (lower.getMin() == null || lower.getMin().getValue() == null) return null;
        return lower.getMin().isInclusive() ? lower.getMin().getValue() : lower.getMin().getValue().plusNanos(1_000_000);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        DateTimeFieldValueSource otherSource = (DateTimeFieldValueSource) obj;
        return restrictions.equals(otherSource.restrictions) &&
            blacklist.equals(otherSource.blacklist) &&
            equals(inclusiveLower, otherSource.inclusiveLower) &&
            equals(exclusiveUpper, otherSource.exclusiveUpper);
    }

    private static boolean equals(OffsetDateTime x, OffsetDateTime y) {
        if (x == null && y == null) {
            return true;
        }

        if (x == null || y == null) {
            return false; //either x OR y is null, but not both (XOR)
        }

        return x.equals(y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restrictions, blacklist, inclusiveLower, exclusiveUpper);
    }
}
