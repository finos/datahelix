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

package com.scottlogic.deg.common.profile.constraintdetail;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  * Order of the enums are important. Ensure most fine is top, and each further down is increasingly coarse.
 * See getMostCoarse(Timescale, Timescale) for more info.
 *
 */
public enum Timescale implements Granularity<OffsetDateTime> {

    MILLIS("millis",
        current -> current.plusNanos(1_000_000),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), d.getMinute(), d.getSecond(), nanoToMilli(d.getNano()), ZoneOffset.UTC)),

    SECONDS("seconds",
        current -> current.plusSeconds(1),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), d.getMinute(), d.getSecond(), 0, ZoneOffset.UTC)),

    MINUTES("minutes",
        current -> current.plusMinutes(1),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), d.getMinute(), 0, 0, ZoneOffset.UTC)),

    HOURS("hours",
        current -> current.plusHours(1),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), 0, 0, 0, ZoneOffset.UTC)),

    DAYS("days",
        current -> current.plusDays(1),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), 0, 0, 0, 0, ZoneOffset.UTC)),

    MONTHS("months",
        current -> current.plusMonths(1),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), 1, 0, 0, 0, 0, ZoneOffset.UTC)),

    YEARS("years",
        current -> current.plusYears(1),
        d -> OffsetDateTime.of(d.getYear(), 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));

    private static final int NANOS_IN_MILLIS = 1_000_000;

    private final String name;

    private final Function<OffsetDateTime, OffsetDateTime> next;

    private final Function<OffsetDateTime, OffsetDateTime> granularityFunction;

    Timescale(final String name, final Function<OffsetDateTime, OffsetDateTime> next, final Function<OffsetDateTime, OffsetDateTime> granularityFunction) {
        this.name = name;
        this.next = next;
        this.granularityFunction = granularityFunction;
    }

    public static Timescale getByName(String name) {

        String enumNames = Stream.of(Timescale.values())
            .map(Timescale::name)
            .map(String::toLowerCase)
            .collect(Collectors.joining(", "));

        return Arrays.stream(Timescale.values())
            .filter(t -> t.name.equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Must be one of the supported datetime units (%s)", enumNames)));
    }

    private static int nanoToMilli(int nano) {
        int factor = NANOS_IN_MILLIS;
        return (nano / factor) * factor;
    }

    @Override
    public boolean isCorrectScale(OffsetDateTime value) {
        OffsetDateTime trimmed = trimToGranularity(value);
        return value.equals(trimmed);
    }

    @Override
    public Granularity<OffsetDateTime> merge(Granularity<OffsetDateTime> otherGranularity) {
        Timescale other = (Timescale) otherGranularity;
        OffsetDateTime initial = OffsetDateTime.MIN;
        return getNext(initial).compareTo(other.getNext(initial)) <= 0 ? other : this;
    }

    @Override
    public OffsetDateTime getNext(OffsetDateTime value) {
        return next.apply(value);
    }

    @Override
    public OffsetDateTime trimToGranularity(OffsetDateTime value) {
        return granularityFunction.apply(value);
    }

    @Override
    public String toString() {
        return name;
    }
}
