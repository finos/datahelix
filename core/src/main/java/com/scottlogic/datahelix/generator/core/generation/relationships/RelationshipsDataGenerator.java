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

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.output.GeneratedObject;
import com.scottlogic.datahelix.generator.common.output.OutputFormat;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.core.generation.DataGenerator;
import com.scottlogic.datahelix.generator.core.profile.relationships.Relationship;

import java.util.Collection;

public class RelationshipsDataGenerator {
    private final OutputFormat outputFormat;
    private final RelationshipProcessor oneToOne;
    private final RelationshipProcessor oneToMany;

    @Inject
    public RelationshipsDataGenerator(
        OutputFormat outputFormat,
        OneToOneRelationshipProcessor oneToOne,
        OneToManyRelationshipProcessor oneToMany) {
        this.outputFormat = outputFormat;
        this.oneToOne = oneToOne;
        this.oneToMany = oneToMany;
    }

    public GeneratedObject produceRelationalObjects(Fields profileFields, GeneratedObject generatedObject, Collection<Relationship> relationships, DataGenerator dataGenerator) {
        if (relationships == null || relationships.isEmpty()){
            return generatedObject;
        }

        if (outputFormat != OutputFormat.JSON) {
            throw new RuntimeException("Unable to produce relational data except in JSON format, please supply the --output-format=JSON command line argument");
        }

        GeneratedRelationalData generatedRelationalData = new GeneratedRelationalData(generatedObject);

        relationships.forEach(relationship -> processRelationship(profileFields, relationship, generatedRelationalData, dataGenerator));

        return generatedRelationalData;
    }

    private void processRelationship(Fields profileFields, Relationship relationship, GeneratedRelationalData generatedObject, DataGenerator dataGenerator) {
        if (relationship.getProfile() == null) {
            throw new RuntimeException("Profile must be supplied for the relationship");
        }

        if (relationship.getExtents().isEmpty()) {
            oneToOne.processRelationship(profileFields, relationship, generatedObject, dataGenerator);
        } else {
            oneToMany.processRelationship(profileFields, relationship, generatedObject, dataGenerator);
        }
    }
}
