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
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.utils.SetUtils;

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

    public Stream<DataBag> createDataBags(RowSpec rowSpec) {
        Stream<Stream<DataBag>> dataBagsForGroups =
            createGroups(rowSpec).stream()
            .map(group -> generateDataForGroup(rowSpec, group));

        return combinationStrategy.permute(dataBagsForGroups);
    }

    private static Set<FieldGroup> createGroups(RowSpec rowSpec) {
        List<FieldSpecRelations> relations = rowSpec.getRelations();
        List<FieldPair> pairs = relations.stream()
            .map(relation -> new FieldPair(relation.main(), relation.other()))
            .collect(Collectors.toList());

        return findGroups(rowSpec.getFields().asList(), pairs);
    }

    private static Set<FieldGroup> findGroups(List<Field> fields, List<FieldPair> pairs) {
        if (fields.isEmpty()) {
            return new HashSet<>();
        }

        Map<Field, List<Field>> fieldMapping = new HashMap<>();
        for (Field field : fields) {
            fieldMapping.put(field, new ArrayList<>());
        }

        for (FieldPair pair : pairs) {
            fieldMapping.compute(pair.first, (key, list) -> updateList(list, key));
            fieldMapping.compute(pair.second, (key, list) -> updateList(list, key));
        }

        return findGroupsFromMap(fieldMapping);
    }

    // This method is recursive
    private static Set<FieldGroup> findGroupsFromMap(Map<Field, List<Field>> map) {
        if (map.isEmpty()) {
            return new HashSet<>();
        }

        Map<Field, List<Field>> copiedMap = new HashMap<>(map);

        Set<Field> fields = findGroup(copiedMap.keySet().iterator().next(), copiedMap);

        copiedMap.keySet().removeAll(fields);
        FieldGroup converted = new FieldGroup(new ArrayList<>(fields));
        Set<FieldGroup> result = findGroupsFromMap(copiedMap);
        result.add(converted);
        return result;
    }

    private static Set<Field> findGroup(Field initial, Map<Field, List<Field>> map) {
        Set<Field> searchedFields = new HashSet<>();
        searchedFields.add(initial);

        Deque<Field> fieldsToSearch = new ArrayDeque<>(map.get(initial));

        searchedFields.addAll(findGroupRecursive(fieldsToSearch, SetUtils.setOf(initial), map));
        return searchedFields;
    }

    private static Set<Field> findGroupRecursive(Deque<Field> toProcess,
                                                 Set<Field> found,
                                                 Map<Field, List<Field>> map) {
        if (toProcess.isEmpty()) {
            return new HashSet<>();
        }

        Deque<Field> toProcessCopy = new ArrayDeque<>(toProcess);
        Set<Field> newFound = new HashSet<>(found);

        Field next = toProcessCopy.pop();
        List<Field> links = map.get(next);

        for (Field field : links) {
            if (!found.contains(field)) {
                newFound.add(field);
                toProcessCopy.add(field);
            }
        }

        return findGroupRecursive(toProcessCopy, newFound, map);
    }

    private static <T> List<T> updateList(List<T> list, T value) {
        list.add(value);
        return list;
    }

    private static final class FieldPair {

        private final Field first;

        private final Field second;

        private FieldPair(Field first, Field second) {
            this.first = first;
            this.second = second;
        }

    }

    private Stream<DataBag> generateDataForGroup(RowSpec rowSpec, FieldGroup group) {
        List<Field> fields = group.fields();
        List<FieldSpecRelations> relations = rowSpec.getRelations().stream()
            .filter(relation -> fields.contains(relation.main()) || fields.contains(relation.other()))
            .collect(Collectors.toList());

        Map<Field, FieldSpec> fieldSpecMap = new HashMap<>();
        for (Field field : fields) {
            fieldSpecMap.put(field, rowSpec.getSpecForField(field));
        }

        FieldSpecGroup specGroup = new FieldSpecGroup(fieldSpecMap, relations);

        FieldSpecGroupValueGenerator groupGenerator = new FieldSpecGroupValueGenerator(generator);

        return groupGenerator.generate(specGroup);
    }
}
