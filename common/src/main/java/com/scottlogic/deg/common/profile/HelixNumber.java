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
