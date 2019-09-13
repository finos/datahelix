package com.scottlogic.deg.generator.restrictions.linear;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter implements Converter<OffsetDateTime> {

    @Override
    public OffsetDateTime convert(Object value) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return OffsetDateTime.parse(value.toString(), formatter);
    }

    @Override
    public boolean isCorrectType(Object value) {
        return value instanceof OffsetDateTime;
    }
}
