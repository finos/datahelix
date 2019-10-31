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

package com.scottlogic.deg.profile.custom;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.custom.CustomGenerator;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;

public class CustomConstraint implements AtomicConstraint {
    private final Field field;
    private final CustomGenerator customGenerator;
    private final boolean negated;

    public CustomConstraint(Field field, CustomGenerator customGenerator){
        this(field, customGenerator, false);
    }

    private CustomConstraint(Field field, CustomGenerator customGenerator, boolean negated) {
        this.field = field;
        this.customGenerator = customGenerator;
        this.negated = negated;

        if (!correctType()){
            throw new ValidationException(String.format("Custom generator %s requires type %s, but field %s is typed %s",
                customGenerator.generatorName(), customGenerator.fieldType(), field.name, field.getType()));
        }
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        return new CustomConstraint(field, customGenerator, !negated);
    }

    @Override
    public FieldSpec toFieldSpec() {
        return FieldSpecFactory
            .fromGenerator(
                new CustomFieldValueSource(customGenerator, negated),
                customGenerator::setMatchingFunction);
    }

    private boolean correctType() {
        switch (customGenerator.fieldType()) {
            case STRING:
                return field.getType() == FieldType.STRING;
            case DATETIME:
                return field.getType() == FieldType.DATETIME;
            case NUMERIC:
                return field.getType() == FieldType.NUMERIC;
            default:
                return false;
        }
    }
}
