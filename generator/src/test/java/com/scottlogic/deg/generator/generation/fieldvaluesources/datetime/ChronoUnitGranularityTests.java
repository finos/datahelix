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
import com.scottlogic.deg.common.profile.constraintdetail.ChronoUnitGranularity;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;

class ChronoUnitGranularityTests {
    @Test
    public void mostCoarseTest() {
        Assert.assertEquals(new ChronoUnitGranularity(DAYS), new ChronoUnitGranularity(MILLIS).merge(new ChronoUnitGranularity(DAYS)));
    }

    @Test
    public void mostCoarseTestYear() {
        Assert.assertEquals(new ChronoUnitGranularity(YEARS), new ChronoUnitGranularity(MINUTES).merge(new ChronoUnitGranularity(YEARS)));
    }

    @Test
    public void mostCoarseTestSame() {
        Assert.assertEquals(new ChronoUnitGranularity(MONTHS), new ChronoUnitGranularity(MONTHS).merge(new ChronoUnitGranularity(MONTHS)));
    }
}