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
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

public class HelixDateTime
{
    private static final HelixDateTime NOW = new HelixDateTime(OffsetDateTime.now());
    private static final DateTimeFormatter FORMATTER = getDateTimeFormatter();
    private final OffsetDateTime value;

    private HelixDateTime(OffsetDateTime value)
    {
        this.value = value;
    }

    public static HelixDateTime create(String dateTime)
    {
        if(dateTime == null) throw  new ValidationException("HelixDateTime cannot be null");
        if (dateTime.equalsIgnoreCase("NOW")) return NOW;
        OffsetDateTime offsetDateTime = fromString(dateTime);
        validateDateRange(offsetDateTime);
        return new HelixDateTime(offsetDateTime);
    }

    public static HelixDateTime create(OffsetDateTime dateTime)
    {
        validateDateRange(dateTime);
        return new HelixDateTime(dateTime);
    }

    public OffsetDateTime getValue()
    {
        return value;
    }

    private static OffsetDateTime fromString(String dateTime)
    {
        try
        {
            TemporalAccessor temporalAccessor = FORMATTER.parse(dateTime);
            return temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS)
                    ? OffsetDateTime.from(temporalAccessor)
                    : LocalDateTime.from(temporalAccessor).atOffset(ZoneOffset.UTC);
        }
        catch (DateTimeParseException exception)
        {
            throw new ValidationException(String.format("Date string '%s' must be in ISO-8601 format: Either yyyy-MM-ddTHH:mm:ss.SSS[Z] between " +
                    "0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z or yyyy-mm-dd between 0001-01-01 and 9999-12-31", dateTime));
        }
    }

    private static void validateDateRange(OffsetDateTime dateTime)
    {
        if (dateTime != null && dateTime.getYear() <= 9999 && dateTime.getYear() >= 1) return;
        throw new ValidationException("Dates must be between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z");
    }

    private static DateTimeFormatter getDateTimeFormatter() {
        DateTimeFormatter dateFormat = new DateTimeFormatterBuilder()
            .appendPattern("u-MM-dd")
            .parseDefaulting(ChronoField.SECOND_OF_DAY,0)
            .toFormatter();

        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("u-MM-dd'T'HH:mm:ss'.'SSS"))
            .optionalStart()
            .appendOffset("+HH", "Z")
            .toFormatter();

        return new DateTimeFormatterBuilder()
            .appendOptional(dateTimeFormatter)
            .appendOptional(dateFormat)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);
    }
}
