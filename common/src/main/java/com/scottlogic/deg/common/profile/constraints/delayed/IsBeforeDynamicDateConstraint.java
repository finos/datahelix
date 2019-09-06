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
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;

public class IsBeforeDynamicDateConstraint implements DelayedAtomicConstraint {

    private final Field field;

    private final boolean inclusive;

    private final Field otherField;

    public IsBeforeDynamicDateConstraint(Field field,
                                         boolean inclusive,
                                         Field otherField) {
        DelayedAtomicConstraint.validateFieldsAreDifferent(otherField, field);
        this.field = field;
        this.inclusive = inclusive;
        this.otherField = otherField;
    }

    @Override
    public Field field() {
        return field;
    }

    @Override
    public Field getOtherField() {
        return otherField;
    }

    public boolean inclusive() {
        return inclusive;
    }
}
