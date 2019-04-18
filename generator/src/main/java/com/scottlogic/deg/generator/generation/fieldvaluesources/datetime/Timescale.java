package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

public enum Timescale {

    MILLIS(ChronoUnit.MILLIS,
        current -> current.plusNanos(1_000),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), d.getMinute(), d.getSecond(), nanoToMilli(d.getNano()), ZoneOffset.UTC)),

    SECONDS(ChronoUnit.SECONDS,
        current -> current.plusSeconds(1),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), d.getMinute(), d.getSecond(), 0, ZoneOffset.UTC)),

    MINUTES(ChronoUnit.MINUTES,
        current -> current.plusMinutes(1),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), d.getMinute(), 0, 0, ZoneOffset.UTC)),

    HOURS(ChronoUnit.HOURS,
        current -> current.plusHours(1),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), 0, 0, 0, ZoneOffset.UTC)),

    DAYS(ChronoUnit.DAYS,
        current -> current.plusDays(1),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), 0, 0, 0, 0, ZoneOffset.UTC)),

    MONTHS(ChronoUnit.MONTHS,
        current -> current.plusMonths(1),
        d -> OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), 1, 0, 0, 0, 0, ZoneOffset.UTC)),

    YEARS(ChronoUnit.YEARS,
        current -> current.plusYears(1),
        d ->  OffsetDateTime.of(d.getYear(), 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));

    private final ChronoUnit unit;

    private final Function<OffsetDateTime, OffsetDateTime> next;

    private final Function<OffsetDateTime, OffsetDateTime> granularityFunction;

    Timescale(final ChronoUnit unit, final Function<OffsetDateTime, OffsetDateTime> next, final Function<OffsetDateTime, OffsetDateTime> granularityFunction) {
        this.unit = unit;
        this.next = next;
        this.granularityFunction = granularityFunction;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public Function<OffsetDateTime, OffsetDateTime> getNext() {
        return next;
    }

    public Function<OffsetDateTime, OffsetDateTime> getGranularityFunction() {
        return granularityFunction;
    }

    private static int nanoToMilli(int nano) {
        int factor = 1_000;
        return (nano / factor) * 1_000;
    }
}
