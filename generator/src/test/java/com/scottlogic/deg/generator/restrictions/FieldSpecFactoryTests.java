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

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RestrictionsFieldSpec;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsStringLongerThanConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsStringShorterThanConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.StringHasLengthConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.ViolatedAtomicConstraint;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class FieldSpecFactoryTests {
    private static final StringRestrictionsFactory stringRestrictionsFactory = new StringRestrictionsFactory();
    private Field testField = createField("Test");


    @Test
    void construct_stringHasLengthConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        StringHasLengthConstraint constraint = new StringHasLengthConstraint(
            testField,
            10
        );

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec)constraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec)constraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_stringHasLengthConstraintViolatedTwice_returnsTheSameGeneratorInstance() {
        ViolatedAtomicConstraint constraint = new ViolatedAtomicConstraint(
            new StringHasLengthConstraint(
                testField,
                10
            )
        );

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_twoInstancesOfStringHasLengthConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        StringHasLengthConstraint firstConstraint = new StringHasLengthConstraint(
            testField,
            20
        );
        StringHasLengthConstraint secondConstraint = new StringHasLengthConstraint(
            testField,
            20
        );

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) firstConstraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) secondConstraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_isStringLongerThanConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        IsStringLongerThanConstraint constraint = new IsStringLongerThanConstraint(
            testField,
            15
        );

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_isStringLongerThanConstraintViolatedTwice_returnsTheSameGeneratorInstance() {
        ViolatedAtomicConstraint constraint = new ViolatedAtomicConstraint(
            new IsStringLongerThanConstraint(
                testField,
                10
            )
        );

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_twoInstancesOfIsStringLongerThanConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        IsStringLongerThanConstraint firstConstraint = new IsStringLongerThanConstraint(
            testField,
            20
        );
        IsStringLongerThanConstraint secondConstraint = new IsStringLongerThanConstraint(
            testField,
            20
        );

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) firstConstraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) secondConstraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_isStringShorterThanConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        IsStringShorterThanConstraint constraint = new IsStringShorterThanConstraint(
            testField,
            25
        );

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_isStringShorterThanConstraintViolatedTwice_returnsTheSameGeneratorInstance() {
        ViolatedAtomicConstraint constraint = new ViolatedAtomicConstraint(
            new IsStringShorterThanConstraint(
                testField,
                10
            )
        );

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_twoInstancesOfIsStringShorterThanConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        IsStringShorterThanConstraint firstConstraint = new IsStringShorterThanConstraint(
            testField,
            20
        );
        IsStringShorterThanConstraint secondConstraint = new IsStringShorterThanConstraint(
            testField,
            20
        );

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) firstConstraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) secondConstraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }
}
