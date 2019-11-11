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

package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.common.output.GeneratedObject;

import java.util.*;


public class DataBag implements GeneratedObject {
    public static final DataBag empty = new DataBag(new HashMap<>());

    private final Map<Field, DataBagValue> fieldToValue;

    public DataBag(Map<Field, DataBagValue> fieldToValue) {
        this.fieldToValue = fieldToValue;
    }

    @Override
    public Object getFormattedValue(Field field) {
        Object value = getDataBagValue(field).getValue();

        if (field.getFormatting() == null || value == null) {
            return value;
        }

        try {
            return String.format(field.getFormatting(), value);
        } catch (IllegalFormatException e){
            return value;
        }
    }

    public DataBagValue getDataBagValue(Field field) {
        if (!fieldToValue.containsKey(field)) {
            throw new IllegalStateException("DataBag has no value stored for " + field);
        }

        return fieldToValue.get(field);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataBag generatedObject = (DataBag) o;
        return Objects.equals(fieldToValue, generatedObject.fieldToValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldToValue);
    }

    public static DataBag merge(DataBag... bags) {
        Map<Field, DataBagValue> newFieldToValue = new HashMap<>();

        FlatMappingSpliterator.flatMap(Arrays.stream(bags)
            .map(r -> r.fieldToValue.entrySet().stream()),
            entrySetStream -> entrySetStream)
            .forEach(entry -> {
                if (newFieldToValue.containsKey(entry.getKey()))
                    throw new IllegalArgumentException("Databags can't be merged because they overlap on field " + entry.getKey().getName());

                newFieldToValue.put(entry.getKey(), entry.getValue());
            });

        return new DataBag(newFieldToValue);
    }

    @Override
    public String toString() {
        return "DataBag{" +
            "fieldToValue=" + fieldToValue +
            '}';
    }

    public boolean isUnique(){
        return fieldToValue.keySet().stream()
            .anyMatch(Field::isUnique);
    }
}
