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
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.reader.file.names.NameRetriever;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OfTypeConstraintFactoryTest {

    private Field field = FieldBuilder.createField("test");

    @Test
    void returnsNullWhenPassedDecimalLowerCase() {
        Optional<Constraint> constraint = OfTypeConstraintFactory.create(field,"decimal");
        assertFalse(constraint.isPresent());
    }

    @Test
    void returnsGranularToOneWhenPassedInteger() {
        Optional<Constraint> constraint = OfTypeConstraintFactory.create(field,"integer");
        assertTrue(constraint.isPresent());
        assertEquals(((IsGranularToNumericConstraint) constraint.get()).granularity, new ParsedGranularity(BigDecimal.ONE));
    }

    @Test
    void returnsStandardRICConstraintWhenPassedUpperCaseRIC() {
        Optional<Constraint> constraint = OfTypeConstraintFactory.create(field,"RIC");
        assertTrue(constraint.isPresent());
        assertEquals(StandardConstraintTypes.RIC, ((MatchesStandardConstraint) constraint.get()).standard);
    }

    @Test
    void returnsInSetConstraintWhenPassedLowerCaseFullName() {
        Optional<Constraint> constraint = OfTypeConstraintFactory.create(field,"fullname");
        IsInSetConstraint isInSetConstraint = new IsInSetConstraint(
            field,
            NameRetriever.loadNamesFromFile(NameConstraintTypes.lookupProfileText("fullname"))
        );

        assertTrue(constraint.isPresent());
        assertEquals((constraint.get()), isInSetConstraint);
    }
}