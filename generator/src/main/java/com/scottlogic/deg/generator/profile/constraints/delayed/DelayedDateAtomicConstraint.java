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


package com.scottlogic.deg.generator.profile.constraints.delayed;

import com.scottlogic.deg.common.date.TemporalAdjusterGenerator;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;

public class DelayedDateAtomicConstraint implements DelayedAtomicConstraint {

    private final Field field;
    private final AtomicConstraintType underlyingConstraint;
    private final Field otherField;

    private final TemporalAdjusterGenerator offsetGenerator;

    private final Integer offsetUnit;
    public DelayedDateAtomicConstraint(Field field, AtomicConstraintType underlyingConstraint, Field otherField, TemporalAdjusterGenerator offsetGenerator, Integer offsetUnit) {
        this.field = field;
        this.underlyingConstraint = underlyingConstraint;
        this.otherField = otherField;
        this.offsetGenerator = offsetGenerator;
        this.offsetUnit = offsetUnit;
    }

    public DelayedDateAtomicConstraint(Field field, AtomicConstraintType underlyingConstraint, Field otherField) {
        this(field, underlyingConstraint, otherField, null, null);
    }

    @Override
    public Field getField(){
        return field;
    }

    @Override
    public AtomicConstraintType getUnderlyingConstraint(){
        return underlyingConstraint;
    }

    @Override
    public Field getOtherField(){
        return otherField;
    }

    @Override
    public DelayedAtomicConstraint negate() {
        throw new UnsupportedOperationException("negate is unsupported on a DelayedDateAtomicConstraint");
    }

    public TemporalAdjusterGenerator getOffsetGenerator() {
        return offsetGenerator;
    }

    public Integer getOffsetUnit() {
        return offsetUnit;
    }

}
