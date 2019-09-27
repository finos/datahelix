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

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.config.detail.CombinationStrategyType;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecGroup;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.databags.*;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Stream;

import static com.scottlogic.deg.common.util.FlatMappingSpliterator.flatMap;
import static com.scottlogic.deg.generator.generation.grouped.FieldSpecGroupDateHelper.adjustBoundsOfDate;

public class FieldSpecGroupValueGenerator {

    private final CombinationStrategyType combinationStrategy;
    private final FieldSpecValueGenerator underlyingGenerator;
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();

    @Inject
    public FieldSpecGroupValueGenerator(FieldSpecValueGenerator underlyingGenerator, CombinationStrategyType combinationStrategy) {
        this.underlyingGenerator = underlyingGenerator;
        this.combinationStrategy = combinationStrategy;
    }

    public Stream<DataBag> generate(FieldSpecGroup group) {
        Field first = SetUtils.firstIteratorElement(group.fieldSpecs().keySet());
        if (group.fieldSpecs().size() == 1){
            return underlyingGenerator.generate(first, group.fieldSpecs().get(first))
                .map(val -> toDataBag(first, val));
        }

        FieldSpec firstSpec = updateFirstSpecFromRelations(first, group);
        FieldSpecGroup updatedGroup = removeSpecFromGroup(first, group);

        Stream<DataBag> firstDataBagValues = underlyingGenerator.generate(first, firstSpec)
            .map(value -> toDataBag(first, value));

        return createRemainingDataBags(firstDataBagValues, first, updatedGroup);
    }

    private FieldSpec updateFirstSpecFromRelations(Field first, FieldSpecGroup group) {
        FieldSpec mutatingSpec = group.fieldSpecs().get(first);

        for (FieldSpecRelations relation : group.relations()) {
            //TODO only works pairwise
            FieldSpec reduced = createMergedSpecFromRelation(first, relation, group);

            mutatingSpec = fieldSpecMerger.merge(reduced, mutatingSpec)
                .orElseThrow(() -> new IllegalStateException("Failed to merge field specs in related fields"));
        }

        return mutatingSpec;
    }

    private FieldSpec createMergedSpecFromRelation(Field first,
                                                   FieldSpecRelations relation,
                                                   FieldSpecGroup group) {
        Field other = relation.main().equals(first) ? relation.other() : relation.main();
        return relation.inverse().reduceToRelatedFieldSpec(group.fieldSpecs().get(other));
    }


    private Stream<DataBag> createRemainingDataBags(Stream<DataBag> generatedDataBsgs, Field field, FieldSpecGroup group) {
        return flatMap(
            generatedDataBsgs,
            dataBag -> generateRemainingData(field, dataBag, group));
    }

    private Stream<DataBag> generateRemainingData(Field field, DataBag dataBag, FieldSpecGroup group) {
        FieldSpecGroup newGroup = adjustBounds(field, dataBag.getDataBagValue(field), group);

        Stream<DataBag> dataBagStream = generate(newGroup)
            .map(otherData -> DataBag.merge(dataBag, otherData));
        return applyCombinationStrategy(dataBagStream);
    }

    private FieldSpecGroup adjustBounds(Field field, DataBagValue value, FieldSpecGroup group) {
        if (value.getValue() instanceof OffsetDateTime) {
            return adjustBoundsOfDate(field, (OffsetDateTime) value.getValue(), group);
        }
        return group;
    }

    private Stream<DataBag> applyCombinationStrategy(Stream<DataBag> dataBagStream) {
        switch (combinationStrategy) {
            case EXHAUSTIVE:
                return dataBagStream;
            case MINIMAL:
            case PINNING:
                return dataBagStream.limit(1);
            default:
                throw new UnsupportedOperationException("no combination strategy provided");
        }
    }

    private DataBag toDataBag(Field field, DataBagValue value) {
        Map<Field, DataBagValue> map = new HashMap<>();
        map.put(field, value);
        return new DataBag(map);
    }

    private FieldSpecGroup removeSpecFromGroup(Field first, FieldSpecGroup group) {
        HashMap<Field, FieldSpec> newFieldSpecs = new HashMap<>(group.fieldSpecs());
        newFieldSpecs.remove(first);
        return new FieldSpecGroup(newFieldSpecs, group.relations());
    }
}
