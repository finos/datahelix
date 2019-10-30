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


package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.reader.FieldReader;
import com.scottlogic.deg.profile.reader.NameRetriever;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FieldReaderTest
{
    @Test
    void returnsNullWhenPassedDecimalLowerCase() {
        Field field = FieldBuilder.createField("test", SpecificFieldType.DECIMAL);
        Optional<Constraint> constraint = FieldReader.read(field);
        assertFalse(constraint.isPresent());
    }

    @Test
    void returnsGranularToOneWhenPassedInteger() {
        Field field = FieldBuilder.createField("test", SpecificFieldType.INTEGER);
        Optional<Constraint> constraint = FieldReader.read(field);
        assertTrue(constraint.isPresent());
        assertEquals(((IsGranularToNumericConstraint) constraint.get()).granularity, new NumericGranularity(0));
    }

    @Test
    void returnsStandardRICConstraintWhenPassedUpperCaseRIC() {
        Field field = FieldBuilder.createField("test", SpecificFieldType.RIC);
        Optional<Constraint> constraint = FieldReader.read(field);
        assertTrue(constraint.isPresent());
        assertEquals(((MatchesStandardConstraint) constraint.get()).standard, StandardConstraintTypes.RIC);
    }

    @Test
    void returnsInSetConstraintWhenPassedLowerCaseFullName() {
        Field field = FieldBuilder.createField("test", SpecificFieldType.FULL_NAME);
        Optional<Constraint> constraint = FieldReader.read(field);
        IsInSetConstraint isInSetConstraint = new IsInSetConstraint(
            field,
            NameRetriever.loadNamesFromFile(NameConstraintTypes.lookupProfileText("fullname"))
        );

        assertTrue(constraint.isPresent());
        assertEquals((constraint.get()), isInSetConstraint);
    }

    @Test
    void returnsGranularToDateConstraintWhenPassedDate() {
        Field field = FieldBuilder.createField("test", SpecificFieldType.DATE);
        Optional<Constraint> constraint = FieldReader.read(field);
        IsGranularToDateConstraint isGranularToDateConstraint = new IsGranularToDateConstraint(
            field,
            new DateTimeGranularity(ChronoUnit.DAYS)
        );

        assertTrue(constraint.isPresent());
        assertEquals((constraint.get()), isGranularToDateConstraint);
    }
}