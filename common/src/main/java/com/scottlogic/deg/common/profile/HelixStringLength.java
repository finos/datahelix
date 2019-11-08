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
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.common.util.NumberUtils;

import java.math.BigDecimal;

public class HelixStringLength
{
    private final int value;

    private HelixStringLength(int value)
    {
        this.value = value;
    }

    public static HelixStringLength create(Object value)
    {
        BigDecimal stringLength = NumberUtils.coerceToBigDecimal(value);
        validateIsInteger(stringLength);
        validateNumberRange(stringLength);
        return new HelixStringLength(stringLength.intValueExact());
    }

    public int getValue()
    {
        return value;
    }

    private static void validateIsInteger(BigDecimal stringLength)
    {
        if (stringLength.stripTrailingZeros().scale() > 0)
        {
            throw new ValidationException(String.format("String length `%s` is not an integer", stringLength));
        }
    }

    private static void validateNumberRange(BigDecimal stringLength)
    {
        BigDecimal max = BigDecimal.valueOf(Defaults.MAX_STRING_LENGTH);
        BigDecimal min = BigDecimal.ZERO;
        if (stringLength.compareTo(min) < 0)
        {
            throw new ValidationException(String.format("String length must have a value >= %s, currently is %s", min.toPlainString(), stringLength.toPlainString()));
        }
        if (stringLength.compareTo(max) > 0)
        {
            throw new ValidationException(String.format("String length must have a value <= %s, currently is %s", max.toPlainString(), stringLength.toPlainString()));
        }
    }
}
