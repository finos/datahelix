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
import com.scottlogic.datahelix.generator.common.RandomNumberGenerator;
import com.scottlogic.datahelix.generator.common.output.GeneratedObject;
import com.scottlogic.datahelix.generator.common.output.SubGeneratedObject;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.core.generation.DataGenerator;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.profile.relationships.Relationship;
import com.scottlogic.datahelix.generator.core.utils.JavaUtilRandomNumberGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class OneToManyRelationshipProcessor implements RelationshipProcessor {
    private final RandomNumberGenerator randomNumberGenerator;
    private final OneToManyRangeResolver rangeResolver;

    @Inject
    public OneToManyRelationshipProcessor(
        JavaUtilRandomNumberGenerator randomNumberGenerator,
        OneToManyRangeResolver rangeResolver) {
        this.randomNumberGenerator = randomNumberGenerator;
        this.rangeResolver = rangeResolver;
    }

    @Override
    public void processRelationship(Fields profileFields, Relationship relationship, GeneratedRelationalData generatedObject, DataGenerator dataGenerator) {
        List<Constraint> extents = relationship.getExtents();
        OneToManyRange range = this.rangeResolver.getRange(profileFields, extents, generatedObject);

        if (range.isEmpty()) {
            return;
        }

        int numberOfObjects = getNumberOfObjectsToProduce(range.getMin(), range.getMax());

        generatedObject.addSubObject(relationship, new SubGeneratedObject() {
            @Override
            public List<Field> getFields() {
                return relationship.getProfile().getFields().asList();
            }

            @Override
            public List<GeneratedObject> getData() {
                return dataGenerator.generateData(relationship.getProfile())
                    .limit(numberOfObjects)
                    .collect(Collectors.toList());
            }

            @Override
            public boolean isArray() {
                return true;
            }
        });
    }

    private Integer getNumberOfObjectsToProduce(int min, Integer max) {
        if (max == null) {
            throw new RuntimeException("Unable to produce sub-objects, range is unbounded");
        }

        int range = max - min;
        if (range == 0){
            return min;
        }

        return randomNumberGenerator.nextInt(range + 1) + min;
    }
}
