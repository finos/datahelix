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

package com.scottlogic.datahelix.generator.core.generation.relationships;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.common.profile.SpecificFieldType;

public class ExtentAugmentedFields extends Fields {
    private static final SpecificFieldType integer = new SpecificFieldType("integer", FieldType.NUMERIC, null);

    public static final String minField = "min";
    public static final String maxField = "max";
    public static final Field min = new Field(minField, integer, false, null, false, true, null);
    public static final Field max = new Field(maxField, integer, false, null, false, true, null);

    public ExtentAugmentedFields(Fields fields) {
        super(fields.asList());
    }

    @Override
    public Field getByName(String fieldName) {
        if (fieldName.equals(minField)) {
            return min;
        }

        if (fieldName.equals(maxField)) {
            return max;
        }

        return super.getByName(fieldName);
    }
}
