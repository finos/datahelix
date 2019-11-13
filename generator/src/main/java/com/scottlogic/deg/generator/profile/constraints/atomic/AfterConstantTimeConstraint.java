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
import com.scottlogic.deg.common.profile.HelixTime;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;
import com.scottlogic.deg.generator.utils.Defaults;

import java.time.LocalTime;

public class AfterConstantTimeConstraint implements AtomicConstraint {

    public final Field field;
    public final HelixTime referenceValue;

    public AfterConstantTimeConstraint(Field field, HelixTime referenceValue) {
        this.field = field;
        this.referenceValue = referenceValue;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        return new BeforeOrEqualToConstantTimeConstraint(field,referenceValue);
    }

    @Override
    public FieldSpec toFieldSpec() {
        final Limit<LocalTime> min = new Limit<>(referenceValue.getValue(), false);
        final LinearRestrictions<LocalTime> timeRestrictions =
            LinearRestrictionsFactory.createTimeRestrictions(min, Defaults.TIME_MAX_LIMIT);
        return FieldSpecFactory.fromRestriction(timeRestrictions);
    }
}
