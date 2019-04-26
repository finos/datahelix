package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class TimescaleTest {

    public OffsetDateTime granularity(Timescale timescale, OffsetDateTime t) {
        return timescale.getGranularityFunction().apply(t);
    }

    @Test
    public void millisGranularity() {
        OffsetDateTime imprecise = OffsetDateTime.of(2001, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

        Timescale.MILLIS.getGranularityFunction().apply(imprecise);
    }

    @Test
    public void mostCoarseTest() {
        assertEquals(Timescale.DAYS, Timescale.getMostCoarse(Timescale.MILLIS, Timescale.DAYS));
    }

    @Test
    public void mostCoarseTestYear() {
        assertEquals(Timescale.YEARS, Timescale.getMostCoarse(Timescale.MINUTES, Timescale.YEARS));
    }

    @Test
    public void mostCoarseTestSame() {
        assertEquals(Timescale.MONTHS, Timescale.getMostCoarse(Timescale.MONTHS, Timescale.MONTHS));
    }

    @Test
    public void testGetByNameThrowsExceptionWithUsefulMessage(){
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> { Timescale.getByName("ShouldntWork"); })
            .withMessageContaining("Must be one of the supported datetime units");
    }

}