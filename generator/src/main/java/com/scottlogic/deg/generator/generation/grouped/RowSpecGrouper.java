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

package com.scottlogic.deg.generator.generation.grouped;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecGroup;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.generation.FieldPair;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.*;
import java.util.stream.Collectors;

public class RowSpecGrouper {
    public static Set<FieldSpecGroup> createGroups(RowSpec rowSpec) {
        List<FieldPair> pairs = rowSpec.getRelations().stream()
            .map(relation -> new FieldPair(relation.main(), relation.other()))
            .collect(Collectors.toList());

        return findGroups(rowSpec.getFields().asList(), pairs)
            .stream().map(fs->createFieldSpecGroups(fs.fields(), rowSpec))
            .collect(Collectors.toSet());
    }

    private static Set<FieldGroup> findGroups(List<Field> fields, List<FieldPair> pairs) {
        if (fields.isEmpty()) {
            return new HashSet<>();
        }

        Map<Field, List<Field>> fieldMapping = fields.stream()
            .collect(Collectors.toMap(field -> field, field -> new ArrayList<>()));

        for (FieldPair pair : pairs) {
            fieldMapping.get(pair.first()).add(pair.second());
            fieldMapping.get(pair.second()).add(pair.first());
        }

        return findGroupsFromMap(fieldMapping);
    }

    // This method is recursive

    private static Set<FieldGroup> findGroupsFromMap(Map<Field, List<Field>> map) {
        if (map.isEmpty()) {
            return new HashSet<>();
        }

        Map<Field, List<Field>> copiedMap = new HashMap<>(map);

        Set<Field> fields = findGroup(SetUtils.firstIteratorElement(copiedMap.keySet()), copiedMap);

        copiedMap.keySet().removeAll(fields);
        copiedMap.values().forEach(fieldList -> fieldList.removeAll(fields));

        FieldGroup converted = new FieldGroup(new ArrayList<>(fields));
        Set<FieldGroup> result = findGroupsFromMap(copiedMap);
        result.add(converted);
        return result;
    }

    private static Set<Field> findGroup(Field initial, Map<Field, List<Field>> map) {
        Set<Field> searchedFields = new HashSet<>();
        searchedFields.add(initial);

        Deque<Field> fieldsToSearch = new ArrayDeque<>();
        fieldsToSearch.add(initial);

        searchedFields.addAll(findGroupRecursive(fieldsToSearch, Collections.singleton(initial), map));
        return searchedFields;
    }

    private static Set<Field> findGroupRecursive(Deque<Field> fieldsToSearch,
                                                 Set<Field> found,
                                                 Map<Field, List<Field>> map) {
        if (fieldsToSearch.isEmpty()) {
            return found;
        }

        Deque<Field> toProcessCopy = new ArrayDeque<>(fieldsToSearch);
        Set<Field> newFound = new HashSet<>(found);

        Field next = toProcessCopy.pop();
        List<Field> links = map.get(next);

        links.stream()
            .filter(field -> !found.contains(field))
            .forEach(field -> addToBoth(field, newFound, toProcessCopy));

        return findGroupRecursive(toProcessCopy, newFound, map);
    }

    private static FieldSpecGroup createFieldSpecGroups(List<Field> fields, RowSpec rowSpec){
        List<FieldSpecRelations> relations = rowSpec.getRelations().stream()
            .filter(relation -> fields.contains(relation.main()) || fields.contains(relation.other()))
            .collect(Collectors.toList());

        Map<Field, FieldSpec> fieldSpecMap = fields.stream()
            .collect(Collectors.toMap(field -> field, rowSpec::getSpecForField));

        return new FieldSpecGroup(fieldSpecMap, relations);
    }

    private static <T> void addToBoth(T element, Collection<T> first, Collection<T> second) {
        first.add(element);
        second.add(element);
    }

}
