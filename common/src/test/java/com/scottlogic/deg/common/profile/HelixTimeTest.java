package com.scottlogic.deg.common.profile;

import com.scottlogic.deg.common.ValidationException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelixTimeTest {

    @Test
    void create_withValidTime_ReturnsValue() {
        HelixTime helixTime = HelixTime.create("20:01:05");
        Assert.assertEquals("20:01:05",helixTime.getValue().toString());
    }

    @Test
    void create_withValidTimeWithMilliseconds_ReturnsValue() {
        HelixTime helixTime = HelixTime.create("20:01:05.551");
        Assert.assertEquals("20:01:05.551",helixTime.getValue().toString());
    }

    @Test
    void create_withInvalidTime_ThrowsError() {
        Assertions.assertThrows(ValidationException.class, () -> {
            HelixTime.create("22-01-05");
        });

    }
}