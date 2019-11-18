package com.scottlogic.datahelix.generator.profile.factories;

import com.scottlogic.datahelix.generator.common.ValidationException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

class TimeFactoryTests
{
    @Test
    void create_withValidTime_ReturnsValue() {
        LocalTime LocalTime = TimeFactory.create("20:01:05");
        Assert.assertEquals("20:01:05", LocalTime.toString());
    }

    @Test
    void create_withValidTimeWithMilliseconds_ReturnsValue() {
        LocalTime LocalTime = TimeFactory.create("20:01:05.551");
        Assert.assertEquals("20:01:05.551", LocalTime.toString());
    }

    @Test
    void create_withInvalidTime_ThrowsError() {
        Assertions.assertThrows(ValidationException.class, () -> TimeFactory.create("22-01-05"));
    }
}