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

package com.scottlogic.deg.generator.generation.fieldvaluesources.datetime;

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TimescaleTests {
    @Test
    public void mostCoarseTest() {
        Assert.assertEquals(Timescale.DAYS, Timescale.MILLIS.merge(Timescale.DAYS));
    }

    @Test
    public void mostCoarseTestYear() {
        Assert.assertEquals(Timescale.YEARS, Timescale.MINUTES.merge(Timescale.YEARS));
    }

    @Test
    public void mostCoarseTestSame() {
        Assert.assertEquals(Timescale.MONTHS, Timescale.MONTHS.merge(Timescale.MONTHS));
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