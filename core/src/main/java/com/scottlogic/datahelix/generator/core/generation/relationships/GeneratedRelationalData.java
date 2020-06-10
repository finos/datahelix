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

import com.scottlogic.datahelix.generator.common.output.GeneratedObject;
import com.scottlogic.datahelix.generator.common.output.RelationalGeneratedObject;
import com.scottlogic.datahelix.generator.common.output.SubGeneratedObject;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.core.profile.relationships.Relationship;

import java.util.HashMap;
import java.util.Map;

public class GeneratedRelationalData implements GeneratedObject, RelationalGeneratedObject {
    private final GeneratedObject underlyingObject;
    private final Map<String, SubGeneratedObject> subObjects = new HashMap<>();

    public GeneratedRelationalData(GeneratedObject underlyingObject) {
        this.underlyingObject = underlyingObject;
    }

    @Override
    public Object getFormattedValue(Field field) {
        return underlyingObject.getFormattedValue(field);
    }

    @Override
    public Object getValue(Field field) {
        return underlyingObject.getValue(field);
    }

    @Override
    public Map<String, SubGeneratedObject> getSubObjects() {
        return subObjects;
    }

    public void addSubObject(Relationship relationship, SubGeneratedObject subObject) {
        if (!subObjects.containsKey(relationship.getName())) {
            subObjects.put(relationship.getName(), subObject);
        } else {
            throw new RuntimeException("Sub-object for this relationship already exists");
        }
    }
}
