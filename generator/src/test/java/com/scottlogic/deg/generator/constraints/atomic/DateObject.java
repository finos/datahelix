package com.scottlogic.deg.generator.constraints.atomic;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class DateObject extends HashMap {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public DateObject(String date){
        this.put("date", date);
    }

    public static DateObject fromDate(OffsetDateTime date){
        return new DateObject(dateTimeFormatter.format(date));
    }
}
