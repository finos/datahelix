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

package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;

import java.util.*;

/**
 * A complete set of information needed to generate a row satisfying a set of constraints.
 * <p>
 * Typically created by combining choices over a decision tree.
 */
public class RowSpec {
    private final ProfileFields fields;

    private final Map<Field, FieldSpec> fieldToFieldSpec;

    private final List<FieldSpecRelations> relations;
    public RowSpec(ProfileFields fields,
                   Map<Field, FieldSpec> fieldToFieldSpec,
                   List<FieldSpecRelations> relations) {

        this.fields = fields;
        this.fieldToFieldSpec = fieldToFieldSpec;
        this.relations = relations;
    }

    public ProfileFields getFields() {
        return fields;
    }

    public FieldSpec getSpecForField(Field field) {
        FieldSpec ownFieldSpec = this.fieldToFieldSpec.get(field);

        if (ownFieldSpec == null) {
            return FieldSpecFactory.fromType(field.getType());
        }

        return ownFieldSpec;
    }

    public List<FieldSpecRelations> getRelations() {
        return relations;
    }

    public Map<Field, FieldSpec> getFieldToFieldSpec() {
        return fieldToFieldSpec;
    }

    @Override
    public String toString() {
        return Objects.toString(fieldToFieldSpec);
    }
}

