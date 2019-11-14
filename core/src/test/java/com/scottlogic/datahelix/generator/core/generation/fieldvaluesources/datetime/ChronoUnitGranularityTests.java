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

package com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.datetime;
import com.scottlogic.datahelix.generator.common.profile.DateTimeGranularity;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static java.time.temporal.ChronoUnit.*;

class ChronoUnitGranularityTests {
    @Test
    public void mostCoarseTest() {
        Assert.assertEquals(new DateTimeGranularity(DAYS), new DateTimeGranularity(MILLIS).merge(new DateTimeGranularity(DAYS)));
    }

    @Test
    public void mostCoarseTestYear() {
        Assert.assertEquals(new DateTimeGranularity(YEARS), new DateTimeGranularity(MINUTES).merge(new DateTimeGranularity(YEARS)));
    }

    @Test
    public void mostCoarseTestSame() {
        Assert.assertEquals(new DateTimeGranularity(MONTHS), new DateTimeGranularity(MONTHS).merge(new DateTimeGranularity(MONTHS)));
    }
}