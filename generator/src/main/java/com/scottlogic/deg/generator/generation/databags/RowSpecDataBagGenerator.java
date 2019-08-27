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

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecGroup;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.generation.FieldSpecGroupValueGenerator;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.RowSpecGrouper;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RowSpecDataBagGenerator {
    private final FieldSpecValueGenerator generator;
    private final CombinationStrategy combinationStrategy;

    @Inject
    public RowSpecDataBagGenerator(
        FieldSpecValueGenerator generator,
        CombinationStrategy combinationStrategy) {
        this.generator = generator;
        this.combinationStrategy = combinationStrategy;
    }

    public DataBagStream createDataBags(RowSpec rowSpec) {
        Stream<DataBagStream> dataBagsForGroups = RowSpecGrouper.createGroups(rowSpec).stream()
            .map(group -> generateDataForGroup(rowSpec, group));

        return new DataBagStream(combinationStrategy.permute(dataBagsForGroups), rowSpec.getFields().stream().anyMatch(field -> field.unique));
    }

    private DataBagStream generateDataForGroup(RowSpec rowSpec, FieldGroup group) {
        List<Field> fields = group.fields();
        List<FieldSpecRelations> relations = rowSpec.getRelations().stream()
            .filter(relation -> fields.contains(relation.main()) || fields.contains(relation.other()))
            .collect(Collectors.toList());

        Map<Field, FieldSpec> fieldSpecMap = fields.stream()
            .collect(Collectors.toMap(field -> field, rowSpec::getSpecForField));

        FieldSpecGroup specGroup = new FieldSpecGroup(fieldSpecMap, relations);

        FieldSpecGroupValueGenerator groupGenerator = new FieldSpecGroupValueGenerator(generator);

        return groupGenerator.generate(specGroup);
    }
}
