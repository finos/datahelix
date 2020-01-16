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

package com.scottlogic.datahelix.generator.core.restrictions;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.core.fieldspecs.RestrictionsFieldSpec;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.LongerThanConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.ShorterThanConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.OfLengthConstraint;
import org.junit.jupiter.api.Test;

import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;
import static org.junit.Assert.assertEquals;

class ConstraintToFieldSpecTests {
    private Field testField = createField("Test");

    @Test
    void construct_stringHasLengthConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        OfLengthConstraint constraint = new OfLengthConstraint(testField, 10);

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec)constraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec)constraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_twoInstancesOfStringHasLengthConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        OfLengthConstraint firstConstraint = new OfLengthConstraint(testField,20);
        OfLengthConstraint secondConstraint = new OfLengthConstraint( testField, 20);

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) firstConstraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) secondConstraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_isStringLongerThanConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        LongerThanConstraint constraint = new LongerThanConstraint(testField, 15);

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_twoInstancesOfIsStringLongerThanConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        LongerThanConstraint firstConstraint = new LongerThanConstraint(testField, 20);
        LongerThanConstraint secondConstraint = new LongerThanConstraint(testField, 20);

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) firstConstraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) secondConstraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_isStringShorterThanConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        ShorterThanConstraint constraint = new ShorterThanConstraint(testField,25);

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) constraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }

    @Test
    void construct_twoInstancesOfIsStringShorterThanConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        ShorterThanConstraint firstConstraint = new ShorterThanConstraint(testField, 20);
        ShorterThanConstraint secondConstraint = new ShorterThanConstraint(testField, 20);

        final RestrictionsFieldSpec firstInstance = (RestrictionsFieldSpec) firstConstraint.toFieldSpec();
        final RestrictionsFieldSpec secondInstance = (RestrictionsFieldSpec) secondConstraint.toFieldSpec();

        assertEquals(firstInstance.getRestrictions(), secondInstance.getRestrictions());
    }
}
