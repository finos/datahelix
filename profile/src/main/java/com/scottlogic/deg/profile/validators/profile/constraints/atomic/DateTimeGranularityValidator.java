package com.scottlogic.deg.profile.validators.profile.constraints.atomic;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;

import java.time.temporal.ChronoUnit;

public class DateTimeGranularityValidator implements Validator<String>
{

    private final String errorInfo;

    public DateTimeGranularityValidator(String errorInfo)
    {
        this.errorInfo = errorInfo;
    }

    @Override
    public final ValidationResult validate(String value)
    {
        try
        {
            if(value.equalsIgnoreCase("WORKING DAYS"))
            {
                return ValidationResult.success();
            }
            ChronoUnit chronoUnit = Enum.valueOf(ChronoUnit.class, value.toUpperCase());
            switch (chronoUnit)
            {
                case MILLIS:
                case SECONDS:
                case MINUTES:
                case HOURS:
                case DAYS:
                case MONTHS:
                case YEARS:
                    return ValidationResult.success();
                default:
                    throw new IllegalStateException("Unsupported granularity: " + chronoUnit);
            }
        } catch (Exception e)
        {
           return ValidationResult.failure("Granularity " + value + " is not supported" + errorInfo);
        }
    }
}
