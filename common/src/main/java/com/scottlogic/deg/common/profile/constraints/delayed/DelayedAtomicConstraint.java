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


package com.scottlogic.deg.common.profile.constraints.delayed;

import com.scottlogic.deg.common.date.TemporalAdjusterGenerator;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import com.scottlogic.deg.common.profile.constraints.Constraint;

public class DelayedAtomicConstraint implements Constraint {

    private final Field field;
    private final AtomicConstraintType underlyingConstraint;
    private final Field otherField;

    private final TemporalAdjusterGenerator offsetGenerator;

    private final Integer offsetUnit;
    public DelayedAtomicConstraint(Field field, AtomicConstraintType underlyingConstraint, Field otherField, TemporalAdjusterGenerator offsetGenerator, Integer offsetUnit) {
        this.field = field;
        this.underlyingConstraint = underlyingConstraint;
        this.otherField = otherField;
        this.offsetGenerator = offsetGenerator;
        this.offsetUnit = offsetUnit;
    }

    public DelayedAtomicConstraint(Field field, AtomicConstraintType underlyingConstraint, Field otherField) {
        this(field, underlyingConstraint, otherField, null, null);
    }

    static void validateFieldsAreDifferent(Field first, Field second) {
        if (first.equals(second)) {
            throw new IllegalArgumentException("Cannot have a relational field referring to itself");
        }
    }

    public Field getField(){
        return field;
    }

    public AtomicConstraintType getUnderlyingConstraint(){
        return underlyingConstraint;
    }

    public Field getOtherField(){
        return otherField;
    }

    public DelayedAtomicConstraint negate() {
        return new DynamicNotConstraint(this);
    }

    public TemporalAdjusterGenerator getOffsetGenerator() {
        return offsetGenerator;
    }

    public Integer getOffsetUnit() {
        return offsetUnit;
    }

}
