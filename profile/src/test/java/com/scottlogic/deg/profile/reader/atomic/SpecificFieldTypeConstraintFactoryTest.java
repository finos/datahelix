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

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldBuilder;
import com.scottlogic.deg.common.profile.SpecificFieldType;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularity;
import com.scottlogic.deg.profile.reader.file.names.NameRetriever;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SpecificFieldTypeConstraintFactoryTest
{

    private Field field = FieldBuilder.createField("test");

    @Test
    void returnsNullWhenPassedDecimalLowerCase() {
        Optional<Constraint> constraint = SpecificFieldTypeConstraintFactory.create(field, SpecificFieldType.DECIMAL);
        assertFalse(constraint.isPresent());
    }

    @Test
    void returnsGranularToOneWhenPassedInteger() {
        Optional<Constraint> constraint = SpecificFieldTypeConstraintFactory.create(field,SpecificFieldType.INTEGER);
        assertTrue(constraint.isPresent());
        assertEquals(((IsGranularToNumericConstraint) constraint.get()).granularity, new NumericGranularity(0));
    }

    @Test
    void returnsStandardRICConstraintWhenPassedUpperCaseRIC() {
        Optional<Constraint> constraint = SpecificFieldTypeConstraintFactory.create(field,SpecificFieldType.RIC);
        assertTrue(constraint.isPresent());
        assertEquals(((MatchesStandardConstraint) constraint.get()).standard, StandardConstraintTypes.RIC);
    }

    @Test
    void returnsInSetConstraintWhenPassedLowerCaseFullName() {
        Optional<Constraint> constraint = SpecificFieldTypeConstraintFactory.create(field,SpecificFieldType.FULL_NAME);
        IsInSetConstraint isInSetConstraint = new IsInSetConstraint(
            field,
            NameRetriever.loadNamesFromFile(NameConstraintTypes.lookupProfileText("fullname"))
        );

        assertTrue(constraint.isPresent());
        assertEquals((constraint.get()), isInSetConstraint);
    }
}