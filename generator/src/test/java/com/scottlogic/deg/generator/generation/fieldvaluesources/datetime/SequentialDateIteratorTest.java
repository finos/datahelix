package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class SequentialDateIteratorTest {

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
    void hasNext_WhenCurrentIsEqualToMax_IsCorrect() {
        // arrange
        this.exclusiveMaxDate = inclusiveMinDate.plusYears(1);
        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.YEARS);
        // act
        OffsetDateTime firstDate = sequentialDateIterator.next();
        boolean hasNextDate = sequentialDateIterator.hasNext();
        // assert
        Assert.assertFalse(hasNextDate);
    }

    @Test
    void hasNext_WhenCurrentIsLessThanMax_IsCorrect() {
        // arrange
        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.YEARS);
        // act
        OffsetDateTime firstDate = sequentialDateIterator.next();
        boolean hasNextDate = sequentialDateIterator.hasNext();
        // assert
        Assert.assertTrue(hasNextDate);
    }

    @Test
    void next_WhenGranularityIsYear_IsCorrect() {
        // arrange
        referenceDate = Timescale.YEARS.getGranularityFunction().apply(inclusiveMinDate.plusYears(1));
        // act
        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.YEARS);
        OffsetDateTime nextDate = sequentialDateIterator.next();
        // assert
        Assert.assertEquals(nextDate,referenceDate);
    }

    @Test
    void next_WhenGranularityIsMonth_IsCorrect() {
        // arrange
        referenceDate = Timescale.MONTHS.getGranularityFunction().apply(inclusiveMinDate.plusMonths(1));
        // act
        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.MONTHS);
        OffsetDateTime nextDate = sequentialDateIterator.next();
        // assert
        Assert.assertEquals(nextDate, referenceDate);

    }

    @Test
    void next_WhenGranularityIsDay_IsCorrect() {
        // arrange
        referenceDate = Timescale.DAYS.getGranularityFunction().apply(inclusiveMinDate.plusDays(1));
        // act
        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.DAYS);
        OffsetDateTime nextDate = sequentialDateIterator.next();
        // assert
        Assert.assertEquals(nextDate, referenceDate);
    }

    @Test
    void next_WhenGranularityIsHour_IsCorrect() {
        // arrange
        referenceDate = Timescale.HOURS.getGranularityFunction().apply(inclusiveMinDate.plusHours(1));
        // act
        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.HOURS);
        OffsetDateTime nextDate = sequentialDateIterator.next();
        // assert
        Assert.assertEquals(nextDate, referenceDate);
    }

    @Test
    void next_WhenGranularityIsMinute_IsCorrect() {
        // arrange
        referenceDate = Timescale.MINUTES.getGranularityFunction().apply(inclusiveMinDate.plusMinutes(1));
        // act
        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.MINUTES);
        OffsetDateTime nextDate = sequentialDateIterator.next();
        // assert
        Assert.assertEquals(nextDate, referenceDate);
    }

    @Test
    void next_WhenGranularityIsMillis_IsCorrect() {
        // arrange
        referenceDate = Timescale.MILLIS.getGranularityFunction().apply(inclusiveMinDate.plusNanos(1_000_000));
        // act
        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.MILLIS);
        OffsetDateTime nextDate = sequentialDateIterator.next();
        // assert
        Assert.assertEquals(nextDate, referenceDate);
    }

    @Test
    void next_WhenGranularityIsNotMillis_IsIncorrect() {
        // arrange
        referenceDate = Timescale.MILLIS.getGranularityFunction().apply(inclusiveMinDate.plusNanos(1_000));
        // act
        SequentialDateIterator sequentialDateIterator = new SequentialDateIterator(inclusiveMinDate, exclusiveMaxDate, Timescale.MILLIS);
        OffsetDateTime nextDate = sequentialDateIterator.next();
        // assert
        Assert.assertNotEquals(nextDate, referenceDate);
    }
}