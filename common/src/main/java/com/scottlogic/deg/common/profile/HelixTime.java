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
            throw new ValidationException("Time string " + time + " must be in ISO-8601 format: " +
                "Either hh:mm:ss or hh:mm:ss.ms");
        }
    }
}
