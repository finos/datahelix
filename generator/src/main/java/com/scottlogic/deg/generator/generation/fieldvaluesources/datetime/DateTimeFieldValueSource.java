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

import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.LinearIterator;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeGranularity;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MAX_LIMIT;
import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MIN_LIMIT;
import static com.scottlogic.deg.generator.utils.SetUtils.stream;

public class DateTimeFieldValueSource implements FieldValueSource {

    private final LinearRestrictions<OffsetDateTime> restrictions;
    private final Set<Object> blacklist;

    private final RandomDateGenerator randomDateGenerator;

    public DateTimeFieldValueSource(
        LinearRestrictions<OffsetDateTime> restrictions,
        Set<Object> blacklist) {
        this.restrictions = restrictions;
        this.blacklist = blacklist;

        this.randomDateGenerator = new RandomDateGenerator(restrictions);
    }

    @Override
    public Stream<Object> generateAllValues() {
        return stream(new LinearIterator<>(restrictions))
            .filter(i -> !blacklist.contains(i))
            .map(Function.identity());
    }

    @Override
    public Stream<Object> generateInterestingValues() {

        ArrayList<Object> interestingValues = new ArrayList<>();

        if (restrictions.getMin() != DATETIME_MIN_LIMIT
            && restrictions.getMin().getValue() != Defaults.ISO_MIN_DATE) {
            OffsetDateTime min = restrictions.getMin().getValue();
            interestingValues.add(restrictions.getMin().isInclusive() ? min : min.plusNanos(1_000_000));
        } else {
            interestingValues.add(OffsetDateTime.of(
                LocalDate.of(1900, 01, 01),
                LocalTime.MIDNIGHT,
                ZoneOffset.UTC));
        }

        if (restrictions.getMax() != DATETIME_MAX_LIMIT
            && restrictions.getMax().getValue() != Defaults.ISO_MAX_DATE) {
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
        return Stream.generate(() -> randomDateGenerator.next(randomNumberGenerator))
            .filter(i -> !blacklist.contains(i))
            .map(Function.identity());

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        DateTimeFieldValueSource otherSource = (DateTimeFieldValueSource) obj;
        return restrictions.equals(otherSource.restrictions) &&
            blacklist.equals(otherSource.blacklist);
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
        return Objects.hash(restrictions, blacklist);
    }
}
