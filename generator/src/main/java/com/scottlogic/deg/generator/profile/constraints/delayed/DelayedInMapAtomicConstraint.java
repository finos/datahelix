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

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;

public class DelayedInMapAtomicConstraint implements DelayedAtomicConstraint {

    private final Field field;
    private final AtomicConstraintType underlyingConstraint;
    private final Field otherField;
    private final DistributedList<String> underlyingList;

    public DelayedInMapAtomicConstraint(Field field, AtomicConstraintType underlyingConstraint, Field otherField, DistributedList<String> underlyingList) {
        this.field = field;
        this.underlyingConstraint = underlyingConstraint;
        this.otherField = otherField;
        this.underlyingList = underlyingList;
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
        throw new UnsupportedOperationException("Negate for inMap not supported");
    }

    public DistributedList<String> getUnderlyingList() {
        return underlyingList;
    }
}
