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


package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;

import static org.junit.jupiter.api.Assertions.*;

class GranularityTest {
    @Test
    public void dateTimeRestrictions_getsGranularity(){
        NumericGranularity granularity = new NumericGranularity(0);
        BigDecimal num = new BigDecimal(5);
        BigDecimal result = granularity.getNext(num);
        BigDecimal expectedResult = new BigDecimal(6);
        assertTrue(expectedResult.compareTo(result) == 0);
    }

}