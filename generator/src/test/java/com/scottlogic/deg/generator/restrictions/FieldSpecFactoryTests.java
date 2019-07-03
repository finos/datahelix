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

package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class FieldSpecFactoryTests {
    private static final StringRestrictionsFactory stringRestrictionsFactory = new StringRestrictionsFactory();
    private FieldSpecFactory fieldSpecFactory = new FieldSpecFactory(stringRestrictionsFactory);


    @Test
    void construct_stringHasLengthConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        StringHasLengthConstraint constraint = new StringHasLengthConstraint(
            new Field("Test"),
            10
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_stringHasLengthConstraintViolatedTwice_returnsTheSameGeneratorInstance() {
        ViolatedAtomicConstraint constraint = new ViolatedAtomicConstraint(
            new StringHasLengthConstraint(
                new Field("Test"),
                10
            )
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_twoInstancesOfStringHasLengthConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        StringHasLengthConstraint firstConstraint = new StringHasLengthConstraint(
            new Field("Test"),
            20
        );
        StringHasLengthConstraint secondConstraint = new StringHasLengthConstraint(
            new Field("Test"),
            20
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_isStringLongerThanConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        IsStringLongerThanConstraint constraint = new IsStringLongerThanConstraint(
            new Field("Test"),
            15
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_isStringLongerThanConstraintViolatedTwice_returnsTheSameGeneratorInstance() {
        ViolatedAtomicConstraint constraint = new ViolatedAtomicConstraint(
            new IsStringLongerThanConstraint(
                new Field("Test"),
                10
            )
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_twoInstancesOfIsStringLongerThanConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        IsStringLongerThanConstraint firstConstraint = new IsStringLongerThanConstraint(
            new Field("Test"),
            20
        );
        IsStringLongerThanConstraint secondConstraint = new IsStringLongerThanConstraint(
            new Field("Test"),
            20
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_isStringShorterThanConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        IsStringShorterThanConstraint constraint = new IsStringShorterThanConstraint(
            new Field("Test"),
            25
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_isStringShorterThanConstraintViolatedTwice_returnsTheSameGeneratorInstance() {
        ViolatedAtomicConstraint constraint = new ViolatedAtomicConstraint(
            new IsStringShorterThanConstraint(
                new Field("Test"),
                10
            )
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_twoInstancesOfIsStringShorterThanConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        IsStringShorterThanConstraint firstConstraint = new IsStringShorterThanConstraint(
            new Field("Test"),
            20
        );
        IsStringShorterThanConstraint secondConstraint = new IsStringShorterThanConstraint(
            new Field("Test"),
            20
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }
}
