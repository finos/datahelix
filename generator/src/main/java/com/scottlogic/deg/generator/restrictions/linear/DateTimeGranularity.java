package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;

import java.time.OffsetDateTime;

public class DateTimeGranularity implements Granularity<OffsetDateTime> {

    private final Timescale granularity;

    @Override
    public Granularity<OffsetDateTime> copy() {
        return new DateTimeGranularity(granularity);
    }

    public DateTimeGranularity(Timescale granularity){
        this.granularity = granularity;
    }

    @Override
    public boolean isCorrectScale(OffsetDateTime value) {
        OffsetDateTime granularDate = granularity.getGranularityFunction().apply(value);

        return value.equals(granularDate);
    }

    @Override
    public Granularity<OffsetDateTime> merge(Granularity<OffsetDateTime> otherGranularity) {
        DateTimeGranularity other = (DateTimeGranularity) otherGranularity;
        if(granularity == Timescale.getMostCoarse(granularity,other.getTimeScale())) {
            return this;
        } else {
            return otherGranularity;
        }
    }

    @Override
    public OffsetDateTime getNext(OffsetDateTime dateTime) {
        return granularity.getNext().apply(dateTime);
    }

    public Timescale getTimeScale() {
        return granularity;
    }

    @Override
    public String toString() {
        return "granularity=" + granularity;
    }
}
