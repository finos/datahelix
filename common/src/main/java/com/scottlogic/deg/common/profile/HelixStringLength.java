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
