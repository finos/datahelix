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

package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.capabilities;

import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.common.validators.Validator;

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
        try {
            if (value.equalsIgnoreCase("WORKING DAYS")) {
                return ValidationResult.success();
            }
            ChronoUnit chronoUnit = Enum.valueOf(ChronoUnit.class, value.toUpperCase());
            switch (chronoUnit) {
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
        } catch (Exception e) {
            return ValidationResult.failure(String.format("Granularity %s is not supported%s", ValidationResult.quote(value), errorInfo));
        }
    }
}
