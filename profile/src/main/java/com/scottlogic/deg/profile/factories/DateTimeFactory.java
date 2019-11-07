package com.scottlogic.deg.profile.factories;

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

public class DateTimeFactory
{
    private static final DateTimeFormatter FORMATTER = getDateTimeFormatter();
    private static final OffsetDateTime NOW = OffsetDateTime.now();

    public static OffsetDateTime create(String dateTime)
    {
        try
        {
            if (dateTime.equalsIgnoreCase("NOW")) return NOW;
            TemporalAccessor temporalAccessor = FORMATTER.parse(dateTime);
            return temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS)
                ? OffsetDateTime.from(temporalAccessor)
                : LocalDateTime.from(temporalAccessor).atOffset(ZoneOffset.UTC);
        } catch (DateTimeParseException exception)
        {
            throw new ValidationException(String.format("Date string '%s' must be in ISO-8601 format: Either yyyy-MM-ddTHH:mm:ss.SSS[Z] between " +
                "0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z or yyyy-mm-dd between 0001-01-01 and 9999-12-31", dateTime));
        }
    }

    private static DateTimeFormatter getDateTimeFormatter()
    {
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
