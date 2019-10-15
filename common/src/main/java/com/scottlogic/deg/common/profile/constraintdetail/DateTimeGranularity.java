package com.scottlogic.deg.common.profile.constraintdetail;

import com.scottlogic.deg.common.date.TemporalAdjusterGenerator;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DateTimeGranularity implements Granularity<OffsetDateTime> {

    private final ChronoUnit chronoUnit;
    private final boolean workingDay;
    private final TemporalAdjusterGenerator temporalAdjusterGenerator;

    public DateTimeGranularity(ChronoUnit chronoUnit) {
        this(chronoUnit, false);
    }

    public DateTimeGranularity(ChronoUnit chronoUnit, boolean workingDay) {
        this.chronoUnit = chronoUnit;
        this.workingDay = workingDay;
        this.temporalAdjusterGenerator = new TemporalAdjusterGenerator(chronoUnit, workingDay);
    }

    @Override
    public boolean isCorrectScale(OffsetDateTime value) {
        return value.equals(trimToGranularity(value));
    }

    @Override
    public Granularity<OffsetDateTime> merge(Granularity<OffsetDateTime> otherGranularity) {
        DateTimeGranularity other = (DateTimeGranularity) otherGranularity;
        return chronoUnit.compareTo(other.chronoUnit) <= 0 ? other : this;
    }

    @Override
    public OffsetDateTime getNext(OffsetDateTime value, int amount){
        return OffsetDateTime.from(temporalAdjusterGenerator.adjuster(amount).adjustInto(value));
    }

    @Override
    public OffsetDateTime trimToGranularity(OffsetDateTime d) {
        // is there a generic way of doing this with chronounit?
        switch (chronoUnit) {
            case MILLIS:
                return OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), d.getMinute(), d.getSecond(), nanoToMilli(d.getNano()), ZoneOffset.UTC);
            case SECONDS:
                return OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), d.getMinute(), d.getSecond(), 0, ZoneOffset.UTC);
            case MINUTES:
                return OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), d.getMinute(), 0, 0, ZoneOffset.UTC);
            case HOURS:
                return OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), 0, 0, 0, ZoneOffset.UTC);
            case DAYS:
                return OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), 0, 0, 0, 0, ZoneOffset.UTC);
            case MONTHS:
                return OffsetDateTime.of(d.getYear(), d.getMonth().getValue(), 1, 0, 0, 0, 0, ZoneOffset.UTC);
            case YEARS:
                return OffsetDateTime.of(d.getYear(), 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
            default:
                throw new UnsupportedOperationException(chronoUnit + "not yet supported as a granularity");
        }
    }

    @Override
    public OffsetDateTime getPrevious(OffsetDateTime value) {
        if (isCorrectScale(value)){
            return getNext(value, -1);
        }

        return trimToGranularity(value);
    }

    @Override
    public OffsetDateTime getRandom(OffsetDateTime min, OffsetDateTime max, RandomNumberGenerator randomNumberGenerator) {
        long generatedLong = (long) randomNumberGenerator.nextDouble(getMilli(min), getMilli(max));

        OffsetDateTime generatedDate = Instant.ofEpochMilli(generatedLong).atZone(ZoneOffset.UTC).toOffsetDateTime();

        return trimToGranularity(generatedDate);
    }

    private long getMilli(OffsetDateTime date) {
        return date.toInstant().toEpochMilli();
    }

    private static int nanoToMilli(int nano) {
        int factor = NANOS_IN_MILLIS;
        return (nano / factor) * factor;
    }
    private static final int NANOS_IN_MILLIS = 1_000_000;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimeGranularity that = (DateTimeGranularity) o;
        return workingDay == that.workingDay &&
            chronoUnit == that.chronoUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chronoUnit, workingDay);
    }

    @Override
    public String toString() {
        return workingDay ? "working days" : chronoUnit.toString();
    }
}
