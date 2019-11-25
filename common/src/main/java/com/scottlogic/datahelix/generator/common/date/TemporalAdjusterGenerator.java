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

package com.scottlogic.datahelix.generator.common.date;

import org.threeten.extra.Temporals;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;
import java.util.function.IntFunction;

public class TemporalAdjusterGenerator {
    private final ChronoUnit chronoUnit;

    private final boolean workingDay;

    public TemporalAdjusterGenerator(ChronoUnit chronoUnit, boolean workingDay) {
        this.chronoUnit = chronoUnit;
        this.workingDay = workingDay;
    }

    public TemporalAdjuster adjuster(int value) {
        return workingDay ? getWorkingDayAdjusterFunction(value) : getAdjusterFunction(chronoUnit, value);
    }

    private TemporalAdjuster getWorkingDayAdjusterFunction(int value) {
        TemporalAdjuster adjuster = value >= 0 ? Temporals.nextWorkingDay() : Temporals.previousWorkingDay();
        return new RepeatedTemporalAdjuster(adjuster, Math.abs(value));
    }

    private TemporalAdjuster getAdjusterFunction(ChronoUnit unit, int value) {
        TemporalAmount temporalAmount = getFunction(unit).apply(Math.abs(value));
        return value >= 0 ? t -> t.plus(temporalAmount) : t -> t.minus(temporalAmount);
    }

    private IntFunction<TemporalAmount> getFunction(ChronoUnit unit) {
        switch(unit) {
            case MILLIS: return Duration::ofMillis;
            case SECONDS: return Duration::ofSeconds;
            case MINUTES: return Duration::ofMinutes;
            case HOURS: return Duration::ofHours;
            case DAYS: return Period::ofDays;
            case WEEKS: return Period::ofWeeks;
            case MONTHS: return Period::ofMonths;
            case YEARS: return Period::ofYears;
            default: throw new IllegalArgumentException("was " + unit + ", Must be one of the supported datetime units (millis, seconds, minutes, hours, days, months, years)");
        }
    }
}
