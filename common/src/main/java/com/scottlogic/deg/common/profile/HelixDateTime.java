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
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("u-MM-dd'T'HH:mm:ss'.'SSS"))
            .optionalStart()
            .appendOffset("+HH", "Z")
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

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
            throw new ValidationException(String.format("Date string '%s' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS[Z] between (inclusive) " +
                    "0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z", dateTime));
        }
    }

    private static void validateDateRange(OffsetDateTime dateTime)
    {
        if (dateTime != null && dateTime.getYear() <= 9999 && dateTime.getYear() >= 1) return;
        throw new ValidationException("Dates must be between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z");
    }
}
