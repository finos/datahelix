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
package com.scottlogic.datahelix.generator.core.profile.constraints.atomic;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.HelixTime;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpec;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecFactory;
import com.scottlogic.datahelix.generator.core.restrictions.linear.Limit;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictionsFactory;
import com.scottlogic.datahelix.generator.core.utils.GeneratorDefaults;

import java.time.LocalTime;

public class BeforeOrEqualToConstantTimeConstraint implements AtomicConstraint {
    public final Field field;
    public final HelixTime referenceValue;

    public BeforeOrEqualToConstantTimeConstraint(Field field, HelixTime referenceValue) {
        this.field = field;
        this.referenceValue = referenceValue;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        return new AfterConstantTimeConstraint(field, referenceValue);
    }

    @Override
    public FieldSpec toFieldSpec() {
        final Limit<LocalTime> max = new Limit<>(referenceValue.getValue(), true);
        final LinearRestrictions<LocalTime> timeRestriction =
            LinearRestrictionsFactory.createTimeRestrictions(GeneratorDefaults.TIME_MIN_LIMIT, max);
        return FieldSpecFactory.fromRestriction(timeRestriction);
    }
}
