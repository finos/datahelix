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

package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularity;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularityFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.IsEqual.equalTo;

class ParsedGranularityTests {
    @Test
    public void shouldBeAbleToParseBigDecimalGranularity(){
        NumericGranularity parsed = NumericGranularityFactory.create(BigDecimal.valueOf(0.1));

        Assert.assertThat(parsed, equalTo(new NumericGranularity(1)));
    }

    @Test
    public void shouldPermitAGranularityOf1(){
        NumericGranularity parsed = NumericGranularityFactory.create(BigDecimal.valueOf(1));

        Assert.assertThat(parsed, equalTo(new NumericGranularity(0)));
    }

    @Test
    public void shouldBeAbleToParseBigIntegerGranularity(){
        NumericGranularity parsed = NumericGranularityFactory.create(BigInteger.ONE);

        Assert.assertThat(parsed, equalTo(new NumericGranularity(0)));
    }

    @Test
    public void shouldBeAbleToParseIntegerGranularity(){
        NumericGranularity parsed = NumericGranularityFactory.create(1);

        Assert.assertThat(parsed, equalTo(new NumericGranularity(0)));
    }

    @Test
    public void shouldBeAbleToParseLongGranularity(){
        NumericGranularity parsed = NumericGranularityFactory.create(1L);

        Assert.assertThat(parsed, equalTo(new NumericGranularity(0)));
    }

    @Test
    public void shouldBeAbleToParseDoubleGranularity(){
        NumericGranularity parsed = NumericGranularityFactory.create(0.1d);

        Assert.assertThat(parsed, equalTo(new NumericGranularity(1)));
    }

    @Test
    public void shouldThrowIfGivenNumberThatIsNotSupported(){
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> NumericGranularityFactory.create(new AtomicInteger()));
    }

    @Test
    public void shouldThrowIfGivenNull(){
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> NumericGranularityFactory.create(null));
    }

    @Test
    public void shouldThrowIfGivenSomethingOtherThanANumber(){
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> NumericGranularityFactory.create("hello"));
    }

    @Test
    public void shouldThrowIfGivenNumberGreaterThan1(){
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> NumericGranularityFactory.create(BigDecimal.valueOf(2)));
    }

    @Test
    public void shouldThrowIfGivenNumberThatIsNotAFractionalPowerOfTen(){
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> NumericGranularityFactory.create(BigDecimal.valueOf(0.2)));
    }
}