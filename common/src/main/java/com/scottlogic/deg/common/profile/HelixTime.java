package com.scottlogic.deg.common.profile;

import com.scottlogic.deg.common.ValidationException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class HelixTime {
    private final LocalTime value;
    private static final HelixTime NOW = new HelixTime(LocalTime.now());

    private HelixTime(LocalTime value){
        this.value = value;
    }

    public LocalTime getValue(){
        return value;
    }

    public static HelixTime create(String value) {
        if (value == null) throw  new ValidationException("HelixTime cannot be null");
        if (value.equalsIgnoreCase("NOW")) return NOW;
        return new HelixTime(timeFromString(value));
    }

    private static LocalTime timeFromString(String time) {
        try {
            return LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Time string "+ time + " must be in ISO-8601 format: " +
                "Either hh:mm:ss or hh:mm:ss.ms");
        }
    }
}
