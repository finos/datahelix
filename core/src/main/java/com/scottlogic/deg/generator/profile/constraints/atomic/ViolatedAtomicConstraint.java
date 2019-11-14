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

package com.scottlogic.deg.generator.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;

public class ViolatedAtomicConstraint implements AtomicConstraint {
    public final AtomicConstraint violatedConstraint;

    public ViolatedAtomicConstraint(AtomicConstraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
    }

    @Override
    public Field getField() {
        return violatedConstraint.getField();
    }

    @Override
    public AtomicConstraint negate() {
        return new ViolatedAtomicConstraint(violatedConstraint.negate());
    }

    @Override
    public FieldSpec toFieldSpec() {
        return violatedConstraint.toFieldSpec();
    }

    @Override
    public String toString() {
        return String.format("Violated: %s", violatedConstraint.toString());
    }

    public int hashCode(){
        return violatedConstraint.hashCode();
    }

    public boolean equals(Object obj){
        return violatedConstraint.equals(obj);
    }
}
