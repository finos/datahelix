package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimescaleTests {
    @Test
    public void mostCoarseTest() {
        Assert.assertEquals(Timescale.DAYS, Timescale.getMostCoarse(Timescale.MILLIS, Timescale.DAYS));
    }

    @Test
    public void mostCoarseTestYear() {
        Assert.assertEquals(Timescale.YEARS, Timescale.getMostCoarse(Timescale.MINUTES, Timescale.YEARS));
    }

    @Test
    public void mostCoarseTestSame() {
        Assert.assertEquals(Timescale.MONTHS, Timescale.getMostCoarse(Timescale.MONTHS, Timescale.MONTHS));
    }

    @Test
    public void testGetByNameThrowsExceptionWithUsefulMessage(){

        // arrange
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->{
            // act
            Timescale.getByName("ShouldNotWork");
        });
        // assert
        Assert.assertThat(exception.getMessage(), CoreMatchers.containsString("Must be one of the supported datetime units"));
    }

}