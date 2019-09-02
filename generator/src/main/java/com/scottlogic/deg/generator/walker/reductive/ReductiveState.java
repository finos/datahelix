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

package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;

import java.util.*;
import java.util.stream.Collectors;

public class ReductiveState {

    private final ProfileFields fields;
    private final Map<Field, DataBagValue> fieldValues;

    public ReductiveState(ProfileFields fields) {
        this(fields, new HashMap<>());
    }

    private ReductiveState(
        ProfileFields fields,
        Map<Field, DataBagValue> fieldValues) {
        this.fields = fields;
        this.fieldValues = fieldValues;
    }

    public ReductiveState withFixedFieldValue(Field field, DataBagValue value) {
        Map<Field, DataBagValue> newFixedFieldsMap = new HashMap<>(fieldValues);
        newFixedFieldsMap.put(field, value);

        return new ReductiveState(fields, newFixedFieldsMap);
    }

    public boolean allFieldsAreFixed() {
        return fieldValues.size() == fields.size();
    }

    public boolean isFieldFixed(Field field) {
        return fieldValues.containsKey(field);
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public Map<Field, DataBagValue> getFieldValues() {
        return this.fieldValues;
    }

    public String toString(boolean detailAllFields) {
        if (fieldValues.size() > 10 && !detailAllFields){
            return String.format("Fixed fields: %d of %d", this.fieldValues.size(), this.fields.size());
        }

        return String.join(", ", fieldValues.entrySet()
            .stream()
            .sorted(Comparator.comparing(ff -> ff.getKey().toString()))
            .map(ff -> String.format("%s: %s", ff.getKey(), ff.getValue()))
            .collect(Collectors.toList()));
    }

    public ProfileFields getFields() {
        return this.fields;
    }

    public DataBag asDataBag() {
        return new DataBag(fieldValues);
    }
}
