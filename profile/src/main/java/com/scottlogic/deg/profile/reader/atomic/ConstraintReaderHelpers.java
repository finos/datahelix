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

package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.constraintdetail.DateTimeGranularity;
import com.scottlogic.deg.profile.reader.InvalidProfileException;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

public class ConstraintReaderHelpers {

    private static final OffsetDateTime NOW = OffsetDateTime.now();

    public static OffsetDateTime parseDate(String value) {
        if (value.toUpperCase().equals("NOW")) {
            return NOW;
        }

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("u-MM-dd'T'HH:mm:ss'.'SSS"))
            .optionalStart()
            .appendOffset("+HH", "Z")
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

        try {
            TemporalAccessor temporalAccessor = formatter.parse(value);

            return temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS)
                ? OffsetDateTime.from(temporalAccessor)
                : LocalDateTime.from(temporalAccessor).atOffset(ZoneOffset.UTC);
        } catch (DateTimeParseException dtpe) {
            throw new InvalidProfileException(String.format(
                "Date string '%s' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS[Z] between (inclusive) " +
                    "0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z",
                value
            ));
        }
    }

    public static FieldType getFieldType(String type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case "decimal":
            case "integer":
                return FieldType.NUMERIC;

            case "string":
            case "ISIN":
            case "SEDOL":
            case "CUSIP":
            case "RIC":
            case "firstname":
            case "lastname":
            case "fullname":
                return FieldType.STRING;

            case "datetime":
                return FieldType.DATETIME;
        }

        throw new InvalidProfileException("Profile is invalid: no type known for " + type);

    }

    public static DateTimeGranularity getDateTimeGranularity(String granularity) {
        String offsetUnitUpperCase = granularity.toUpperCase();
        boolean workingDay = offsetUnitUpperCase.equals("WORKING DAYS");
        return new DateTimeGranularity(
            ChronoUnit.valueOf(ChronoUnit.class, workingDay ? "DAYS" : offsetUnitUpperCase),
            workingDay);
    }

}
