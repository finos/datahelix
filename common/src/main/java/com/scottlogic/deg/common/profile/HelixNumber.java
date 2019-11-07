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
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.common.util.defaults.NumericDefaults;

import java.math.BigDecimal;

public class HelixNumber
{
    private final BigDecimal value;

    private HelixNumber(BigDecimal value)
    {
        this.value = value;
    }

    public static HelixNumber create(Object value)
    {
        BigDecimal number = NumberUtils.coerceToBigDecimal(value);
        validateNumberRange(number);
        return new HelixNumber(number);
    }

    public BigDecimal getValue()
    {
        return value;
    }

    private static void validateNumberRange(BigDecimal number)
    {
        BigDecimal max = NumericDefaults.get().max();
        BigDecimal min = NumericDefaults.get().min();
        if (number.compareTo(min) < 0)
        {
            throw new ValidationException(String.format("Number must have a value >= %s, currently is %s", min.toPlainString(), number.toPlainString()));
        }
        if (number.compareTo(max) > 0)
        {
            throw new ValidationException(String.format("Number must have a value <= %s, currently is %s", max.toPlainString(), number.toPlainString()));
        }
    }
}
