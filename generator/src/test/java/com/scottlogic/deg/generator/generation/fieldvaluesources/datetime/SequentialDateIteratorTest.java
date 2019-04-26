package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.OffsetDateTime;
import java.time.ZoneOffset;


class SequentialDateIteratorTest {

    private OffsetDateTime inclusiveMinDate;
    private OffsetDateTime exclusiveMaxDate;
    private OffsetDateTime referenceDate;


    @BeforeEach
    public void setup() {
        // set some default values
        this.inclusiveMinDate = OffsetDateTime.of(2001, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        this.exclusiveMaxDate= OffsetDateTime.of(2010, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
    }

    @Test
    void checkHasNextWhenCurrentIsEqualToMax() {

        this.exclusiveMaxDate = inclusiveMinDate.plusYears(1);

        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.YEARS);

        // assert there is a date we can take.
        Assert.assertTrue(sequentialDateIterator.hasNext());

        // take the first date
        OffsetDateTime firstDate = sequentialDateIterator.next();

        // assert there are no more available dates.
        Assert.assertFalse(sequentialDateIterator.hasNext());
    }

    @Test
    void checkHasNextWhenCurrentIsLessThanMax() {

        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.YEARS);

        // assert there is a date we can take.
        Assert.assertTrue(sequentialDateIterator.hasNext());

        // take the first date
        OffsetDateTime firstDate = sequentialDateIterator.next();

        // assert there are no more available dates.
        Assert.assertTrue(sequentialDateIterator.hasNext());
    }

    @Test
    void checkNextIsCorrectWhenGranularityIsYear() {

        referenceDate = Timescale.YEARS.getGranularityFunction().apply(inclusiveMinDate.plusYears(1));

        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.YEARS);

        Assert.assertEquals(sequentialDateIterator.next(),referenceDate);
    }

    @Test
    void checkNextIsCorrectWhenGranularityIsMonth() {

        referenceDate = Timescale.MONTHS.getGranularityFunction().apply(inclusiveMinDate.plusMonths(1));

        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.MONTHS);

        Assert.assertEquals(sequentialDateIterator.next(), referenceDate);

    }

    @Test
    void checkNextIsCorrectWhenGranularityIsDay() {
        referenceDate = Timescale.DAYS.getGranularityFunction().apply(inclusiveMinDate.plusDays(1));

        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.DAYS);

        Assert.assertEquals(sequentialDateIterator.next(), referenceDate);
    }

    @Test
    void checkNextIsCorrectWhenGranularityIsHour() {
        referenceDate = Timescale.HOURS.getGranularityFunction().apply(inclusiveMinDate.plusHours(1));

        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.HOURS);

        Assert.assertEquals(sequentialDateIterator.next(), referenceDate);
    }

    @Test
    void checkNextIsCorrectWhenGranularityIsMinute() {
        referenceDate = Timescale.MINUTES.getGranularityFunction().apply(inclusiveMinDate.plusMinutes(1));

        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.MINUTES);

        Assert.assertEquals(sequentialDateIterator.next(), referenceDate);
    }

    @Test
    void checkNextIsCorrectWhenGranularityIsMillis() {
        referenceDate = Timescale.MILLIS.getGranularityFunction().apply(inclusiveMinDate.plusNanos(1_000_000));

        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.MILLIS);

        Assert.assertEquals(sequentialDateIterator.next(), referenceDate);
    }

    @Test
    void checkNextIsIncorrectWhenGranularityIsNotMillis() {
        referenceDate = Timescale.MILLIS.getGranularityFunction().apply(inclusiveMinDate.plusNanos(1_000));

        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.MILLIS);

        Assert.assertNotEquals(sequentialDateIterator.next(), referenceDate);
    }
}