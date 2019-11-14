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

import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.TimeGranularity;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpec;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecFactory;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictionsFactory;
import com.scottlogic.datahelix.generator.core.utils.GeneratorDefaults;

public class GranularToTimeConstraint implements AtomicConstraint {
    public final TimeGranularity timeGranularity;
    public final Field field;

    public GranularToTimeConstraint(Field field, TimeGranularity timeGranularity) {
        if(field == null)
            throw new IllegalArgumentException("field must not be null");
        if(timeGranularity == null)
            throw new IllegalArgumentException("granularity must not be null");

        this.timeGranularity = timeGranularity;
        this.field = field;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        throw new ValidationException("Time Granularity cannot be negated or used in if statements");
    }

    @Override
    public FieldSpec toFieldSpec() {
        return FieldSpecFactory.fromRestriction(
            LinearRestrictionsFactory.createTimeRestrictions(
                GeneratorDefaults.TIME_MIN_LIMIT,
                GeneratorDefaults.TIME_MAX_LIMIT,
                timeGranularity));
    }
}
