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

package com.scottlogic.deg.common.date;

import org.threeten.extra.Temporals;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

public class ChronoUnitWorkingDayWrapper {

    private final ChronoUnit chronoUnit;

    private final boolean workingDay;

    public ChronoUnitWorkingDayWrapper(ChronoUnit chronoUnit, boolean workingDay) {
        this.chronoUnit = chronoUnit;
        this.workingDay = workingDay;
    }

    public ChronoUnit chronoUnit() {
        return chronoUnit;
    }

    public boolean workingDay() {
        return workingDay;
    }

    public TemporalAdjuster adjuster() {
        if (workingDay) {
            return Temporals.nextWorkingDay();
        } else {
            switch(chronoUnit) {
                case MILLIS: return t -> t.plus(Duration.ofMillis(1));
                case SECONDS: return t -> t.plus(Duration.ofSeconds(1));
                case MINUTES: return t -> t.plus(Duration.ofMinutes(1));
                case HOURS: return t -> t.plus(Duration.ofHours(1));
                case DAYS: return t -> t.plus(Period.ofDays(1));
                case WEEKS: return t -> t.plus(Period.ofWeeks(1));
                case MONTHS: return t -> t.plus(Period.ofMonths(1));
                case YEARS: return t -> t.plus(Period.ofYears(1));
                default: throw new IllegalArgumentException("Couldn't construct offset of unit " + chronoUnit);
            }
        }
    }
}
