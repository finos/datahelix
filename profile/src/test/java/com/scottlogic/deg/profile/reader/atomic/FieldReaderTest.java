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
import com.scottlogic.deg.common.profile.fields.Field;
import com.scottlogic.deg.common.profile.fields.FieldBuilder;
import com.scottlogic.deg.common.profile.fields.SpecificFieldType;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.reader.FieldReader;
import com.scottlogic.deg.profile.reader.file.names.NameRetriever;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FieldReaderTest
{

    private Field decimalField = FieldBuilder.createField("test", SpecificFieldType.DECIMAL);
    private Field integerField = FieldBuilder.createField("test", SpecificFieldType.INTEGER);
    private Field ricField = FieldBuilder.createField("test", SpecificFieldType.RIC);
    private Field fullNameField = FieldBuilder.createField("test", SpecificFieldType.FULL_NAME);

    @Test
    void returnsNullWhenPassedDecimalLowerCase() {
        Optional<Constraint> constraint = FieldReader.read(decimalField);
        assertFalse(constraint.isPresent());
    }

    @Test
    void returnsGranularToOneWhenPassedInteger() {
        Optional<Constraint> constraint = FieldReader.read(integerField);
        assertTrue(constraint.isPresent());
        assertEquals(((IsGranularToNumericConstraint) constraint.get()).granularity, new NumericGranularity(0));
    }

    @Test
    void returnsStandardRICConstraintWhenPassedUpperCaseRIC() {
        Optional<Constraint> constraint = FieldReader.read(ricField);
        assertTrue(constraint.isPresent());
        assertEquals(((MatchesStandardConstraint) constraint.get()).standard, StandardConstraintTypes.RIC);
    }

    @Test
    void returnsInSetConstraintWhenPassedLowerCaseFullName() {
        Optional<Constraint> constraint = FieldReader.read(fullNameField);
        IsInSetConstraint isInSetConstraint = new IsInSetConstraint(
            fullNameField,
            NameRetriever.loadNamesFromFile(NameConstraintTypes.lookupProfileText("fullname"))
        );

        assertTrue(constraint.isPresent());
        assertEquals((constraint.get()), isInSetConstraint);
    }
}