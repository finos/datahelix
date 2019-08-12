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


package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.date.ChronoUnitWorkingDayWrapper;
import com.scottlogic.deg.common.profile.constraints.delayed.*;
import com.scottlogic.deg.generator.fieldspecs.relations.*;
import org.threeten.extra.Minutes;
import org.threeten.extra.Temporals;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAmount;

public class FieldRelationsFactory {

   public FieldSpecRelations construct(DelayedAtomicConstraint constraint) {
       return construct(constraint, false);
   }

   private FieldSpecRelations construct(DelayedAtomicConstraint constraint, boolean negate) {
       if (constraint instanceof DynamicNotConstraint) {
           return construct(constraint.negate(), !negate);
       } else if (constraint instanceof IsAfterDynamicDateConstraint) {
           return constructAfterDate((IsAfterDynamicDateConstraint) constraint);
       } else if (constraint instanceof IsBeforeDynamicDateConstraint) {
           return constructBeforeDate((IsBeforeDynamicDateConstraint) constraint);
       } else if (constraint instanceof IsEqualToDynamicDateConstraint) {
            return constructEqualToDate((IsEqualToDynamicDateConstraint) constraint);
       } else {
           throw new IllegalArgumentException("Unsupported field spec relations: " + constraint.getClass());
       }
   }

   private FieldSpecRelations constructBeforeDate(IsBeforeDynamicDateConstraint constraint) {
       return new BeforeDateRelation(
           constraint.underlyingConstraint().getField(),
           constraint.field(),
           constraint.inclusive());
   }

   private FieldSpecRelations constructAfterDate(IsAfterDynamicDateConstraint constraint) {
       return new AfterDateRelation(
           constraint.underlyingConstraint().getField(),
           constraint.field(),
           constraint.inclusive());
   }

   private FieldSpecRelations constructEqualToDate(IsEqualToDynamicDateConstraint constraint) {
       if (constraint.unit() != null) {
           return new EqualToOffsetDateRelation(
               constraint.underlyingConstraint().getField(),
               constraint.field(),
               constructDate(constraint.unit()),
               constraint.offset());
       } else {
           return new EqualToDateRelation(
               constraint.underlyingConstraint().getField(),
               constraint.field());
       }
   }

   private TemporalAdjuster constructDate(ChronoUnitWorkingDayWrapper unit) {
       if (unit.workingDay()) {
           return Temporals.nextWorkingDay();
       }
       switch(unit.chronoUnit()) {
           case MILLIS: return t -> t.plus(Duration.ofMillis(1));
           case SECONDS: return t -> t.plus(Duration.ofSeconds(1));
           case MINUTES: return t -> t.plus(Duration.ofMinutes(1));
           case HOURS: return t -> t.plus(Duration.ofHours(1));
           case DAYS: return t -> t.plus(Period.ofDays(1));
           case WEEKS: return t -> t.plus(Period.ofWeeks(1));
           case MONTHS: return t -> t.plus(Period.ofMonths(1));
           case YEARS: return t -> t.plus(Period.ofYears(1));
           default: throw new IllegalArgumentException("Couldn't construct offset of unit " + unit);
       }
   }
}
