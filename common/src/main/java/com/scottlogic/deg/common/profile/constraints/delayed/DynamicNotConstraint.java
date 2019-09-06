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

import com.scottlogic.deg.common.profile.Field;

import java.util.Objects;

public class DynamicNotConstraint extends DelayedAtomicConstraint {

    private final DelayedAtomicConstraint negatedConstraint;

    public DynamicNotConstraint(DelayedAtomicConstraint negatedConstraint) {
        super(negatedConstraint.getField(), negatedConstraint.getUnderlyingConstraint(), negatedConstraint.getOtherField());
        if (negatedConstraint instanceof DynamicNotConstraint) {
            throw new IllegalArgumentException("Nested DynamicNotConstraint not allowed");
        }
        this.negatedConstraint = negatedConstraint;
    }

    @Override
    public Field getField() {
        return negatedConstraint.getField();
    }

    @Override
    public Field getOtherField() {
        return negatedConstraint.getOtherField();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicNotConstraint that = (DynamicNotConstraint) o;
        return Objects.equals(negatedConstraint, that.negatedConstraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash("NOT", negatedConstraint);
    }
}
