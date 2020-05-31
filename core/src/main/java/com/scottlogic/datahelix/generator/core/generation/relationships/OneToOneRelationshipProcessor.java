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
import com.scottlogic.datahelix.generator.common.output.SubGeneratedObject;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.core.generation.DataGenerator;
import com.scottlogic.datahelix.generator.core.profile.relationships.Relationship;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OneToOneRelationshipProcessor implements RelationshipProcessor {
    @Override
    public void processRelationship(Fields profileFields, Relationship relationship, GeneratedRelationalData generatedObject, DataGenerator dataGenerator) {
        Optional<GeneratedObject> subObject = dataGenerator.generateData(relationship.getProfile()).limit(1).findFirst();
        if (!subObject.isPresent()) {
            return;
        }

        generatedObject.addSubObject(relationship, new SubGeneratedObject() {
            @Override
            public List<Field> getFields() {
                return relationship.getProfile().getFields().asList();
            }

            @Override
            public List<GeneratedObject> getData() {
                return Collections.singletonList(subObject.get());
            }

            @Override
            public boolean isArray() {
                return false;
            }
        });
    }
}
