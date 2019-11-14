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

package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;

import java.util.HashMap;
import java.util.Map;

public class DataBagBuilder {
    private final Map<Field, DataBagValue> fieldToValue;

    public DataBagBuilder() {
        fieldToValue = new HashMap<>();
    }

    public DataBagBuilder set(Field field, DataBagValue value) {
        if (fieldToValue.containsKey(field)) {
            throw new IllegalArgumentException("Databag already contains a value for " + field);
        }
        fieldToValue.put(field, value);

        return this;
    }

    public DataBagBuilder set(Field field, Object value) {
        return this.set(field, new DataBagValue(value));
    }

    public DataBag build() {
        return new DataBag(fieldToValue);
    }

    public static DataBag of(Field field, Object value){
        return new DataBagBuilder().set(field, value).build();
    }
}
