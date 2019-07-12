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

package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.fieldspecs.whitelist.ElementFrequency;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;

public class ConstraintReaderHelpers {
    public static Object getValidatedValue(ConstraintDTO dto) {
        return getValidatedValue(dto, dto.value, Object.class);
    }

    public static <T> T getValidatedValue(ConstraintDTO dto, Class<T> requiredType) {
        return getValidatedValue(dto, dto.value, requiredType);
    }

    public static Set<ElementFrequency<Object>> getValidatedValues(ConstraintDTO dto) {
        if (dto.values == null) {
            throw new InvalidProfileException(String.format(
                "Field [%s]: Couldn't recognise 'values' property, it must not contain 'null'",
                dto.field
            ));
        }

        Set<Object> mappedValues = new HashSet<>();
        for (Object value : dto.values) {
            if (value == null) {
                throw new InvalidProfileException(String.format(
                    "Field [%s]: Set must not contain null",
                    dto.field
                ));
            }

            mappedValues.add(getValidatedValue(dto, value, Object.class));
        }

        return mappedValues.stream()
            .map(element -> new ElementFrequency<>(element, 1.0F))
            .collect(Collectors.toSet());
    }

    public static <T> Optional<T> tryGetValidatedValue(ConstraintDTO dto, Class<T> requiredType) {
        try {
            return Optional.of(getValidatedValue(dto, dto.value, requiredType));
        }
        catch(Exception exp) {
            return Optional.empty();
        }
    }

    public static <T> T ensureValueBetween(
        ConstraintDTO dto,
        @SuppressWarnings("SameParameterValue") Class<T> requiredType,
        BigDecimal min,
        BigDecimal max) throws InvalidProfileException {

        T value = getValidatedValue(dto, dto.value, requiredType);
        return ensureValueBetween(dto, value, min, max);
    }

    /**
     * @param dto          The ConstraintDTO instance
     * @param requiredType the type of value required, pass Object.class if any type is acceptable
     * @return the value in the ConstraintDTO cast as T
     * @throws InvalidProfileException if the value is null, not of type T, or (when a number) outside of the allowed range
     */
    private static <T> T getValidatedValue(
        ConstraintDTO dto,
        Object value,
        Class<T> requiredType) throws InvalidProfileException {

        if (value == null) {
            throw new InvalidProfileException(String.format(
                "Field [%s]: Couldn't recognise 'value' property, it must be set to a value",
                dto.field
            ));
        }

        if (requiredType == OffsetDateTime.class || value instanceof Map) {
            //the only value that is currently permitted to be a Map is a DateObject
            value = getValueAsDate(dto, value);
        }

        if (requiredType == Integer.class && value instanceof BigDecimal) {
            BigDecimal valueAsBigDecimal = (BigDecimal) value;
            if (valueAsBigDecimal.stripTrailingZeros().scale() > 0) {
                throw new InvalidProfileException(String.format(
                    "Field [%s]: Couldn't recognise 'value' property, it must be an integer but was a decimal with value `%s`",
                    dto.field,
                    value
                ));
            }

            value = valueAsBigDecimal.intValueExact();
        }

        if (!requiredType.isInstance(value)) {
            throw new InvalidProfileException(
                String.format(
                    "Field [%s]: Couldn't recognise 'value' property, it must be a %s but was a %s with value `%s`",
                    dto.field,
                    requiredType.getSimpleName(),
                    value.getClass().getSimpleName(),
                    value
                )
            );
        }

        if (value instanceof Number) {
            return requiredType.cast(validateNumber(dto, (Number) value));
        } else if (value instanceof String) {
            return requiredType.cast(validateString(dto, (String) value));
        }

        return requiredType.cast(value);
    }

    public static String getValueAsString(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return (String)value;
        }

        if (value instanceof Map) {
            // the only values that are Maps are dates in object format
            Map objectMap = (Map) value;
            if (!objectMap.containsKey("date")) {
                return null;
            }
            if (objectMap.get("date") instanceof String) {
                return (String) objectMap.get("date");
            }
            return objectMap.get("date").toString();
        }

        return value.toString();
    }

    private static OffsetDateTime getValueAsDate(ConstraintDTO dto, Object value) {
        if (!(value instanceof Map)) {
            throw new InvalidProfileException(String.format(
                "Field [%s]: Dates should be expressed in object format e.g. { \"date\": \"%s\" }",
                dto.field,
                value
            ));
        }

        Map objectMap = (Map) value;
        if (!objectMap.containsKey("date")) {
            throw new InvalidProfileException(String.format(
                "Field [%s]: Object found but no 'date' property exists, found %s",
                dto.field,
                Objects.toString(objectMap.keySet())
            ));
        }

        Object date = objectMap.get("date");
        if (!(date instanceof String)) {
            throw new InvalidProfileException(String.format(
                "Field [%s]: Date on date object must be a string, found %s",
                dto.field,
                date
            ));
        }

        OffsetDateTime offsetDateTime = parseDate((String) date, dto);
        if (offsetDateTime != null && (offsetDateTime.getYear() > 9999 || offsetDateTime.getYear() < 1)) {
            throwDateTimeError((String) date, dto);
        }

        return offsetDateTime;
    }

    private static Number validateNumber(ConstraintDTO dto, Number value) {
        return ensureValueBetween(dto, value, Defaults.NUMERIC_MIN, Defaults.NUMERIC_MAX);
    }

    private static String validateString(ConstraintDTO dto, String value) {
        if (value.length() > Defaults.MAX_STRING_LENGTH) {
            throw new InvalidProfileException(String.format(
                "Field [%s]: set contains a string longer than maximum permitted length, was: %d, max-length: %d",
                dto.field,
                value.length(),
                Defaults.MAX_STRING_LENGTH));
        }

        return value;
    }

    private static OffsetDateTime parseDate(String value, ConstraintDTO dto) {
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
            throwDateTimeError(value, dto);
            return null;
        }
    }

    private static void throwDateTimeError(String profileDate, ConstraintDTO dto) {
        throw new InvalidProfileException(String.format(
            "Field [%s]: Date string '%s' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS[Z] between (inclusive) " +
                "0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z",
            dto.field,
            profileDate
        ));
    }

    private static <T> T ensureValueBetween(ConstraintDTO dto, T value, BigDecimal min, BigDecimal max) {
        BigDecimal valueAsBigDecimal = NumberUtils.coerceToBigDecimal(value);
        if (valueAsBigDecimal.compareTo(min) < 0) {
            throw new InvalidProfileException(String.format(
                "Field [%s]: %s constraint must have an operand/value >= %s, currently is %s",
                dto.field,
                dto.is,
                min.toPlainString(),
                valueAsBigDecimal.toPlainString()));
        }

        if (valueAsBigDecimal.compareTo(max) > 0) {
            throw new InvalidProfileException(String.format(
                "Field [%s]: %s constraint must have an operand/value <= %s, currently is %s",
                dto.field,
                dto.is,
                max.toPlainString(),
                valueAsBigDecimal.toPlainString()));
        }

        return value;
    }
}
