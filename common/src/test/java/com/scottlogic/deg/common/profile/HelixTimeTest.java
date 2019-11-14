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
        Assert.assertEquals("20:01:05", helixTime.getValue().toString());
    }

    @Test
    void create_withValidTimeWithMilliseconds_ReturnsValue() {
        HelixTime helixTime = HelixTime.create("20:01:05.551");
        Assert.assertEquals("20:01:05.551", helixTime.getValue().toString());
    }

    @Test
    void create_withInvalidTime_ThrowsError() {
        Assertions.assertThrows(ValidationException.class, () -> {
            HelixTime.create("22-01-05");
        });
    }
}
