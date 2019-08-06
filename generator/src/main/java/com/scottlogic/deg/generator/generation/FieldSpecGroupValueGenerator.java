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


package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecGroup;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldSpecGroupValueGenerator {

    private final FieldSpecValueGenerator underlyingGenerator;

    public FieldSpecGroupValueGenerator(FieldSpecValueGenerator underlyingGenerator) {
        this.underlyingGenerator = underlyingGenerator;
    }

    public Stream<DataBag> generate(FieldSpecGroup group) {
        Field first = group.fieldSpecs().keySet().iterator().next();

        FieldSpecGroup groupRespectingFirstField = initialAdjustments(first, group);
        FieldSpec firstSpec = groupRespectingFirstField.fieldSpecs().get(first);

        Stream<DataBag> firstDataBagValues = underlyingGenerator.generate(firstSpec)
            .map(value -> toDataBag(first, value));

        return createRemainingDataBags(firstDataBagValues, first, groupRespectingFirstField);
    }

    private static DataBag toDataBag(Field field, DataBagValue value) {
        Map<Field, DataBagValue> map = new HashMap<>();
        map.put(field, value);
        return new DataBag(map);
    }

    private static FieldSpecGroup initialAdjustments(Field field, FieldSpecGroup group) {
        for (FieldSpecRelations relation : group.relations()) {

        }

        return group;
    }

    private static FieldSpecGroup adjustBounds(Field field, DataBagValue value, FieldSpecGroup group) {
        Object object = value.getUnformattedValue();

        // TODO: Consider null cases
        if (object instanceof OffsetDateTime) {
            return adjustBoundsOfDate(field, (OffsetDateTime) object, group);
        }


        return group;
    }

    private static final class FieldWrapper<T> {
        private final Field field;
        private final T other;

        public FieldWrapper(Field field, T other) {
            this.field = field;
            this.other = other;
        }
    }

    private static FieldSpecGroup adjustBoundsOfDate(Field field, OffsetDateTime value, FieldSpecGroup group) {
        Map<Field, FieldSpec> specs = new HashMap<>(group.fieldSpecs());
        // Set the value we've specified to be specific
        DateTimeRestrictions.DateTimeLimit limit = new DateTimeRestrictions.DateTimeLimit(value, true);
        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = limit;
        restrictions.max = limit;
        FieldSpec newSpec = FieldSpec.Empty.withNotNull().withDateTimeRestrictions(restrictions);
        specs.replace(field, newSpec);

        // Operate on the new bounds
        Set<FieldSpecRelations> relations = group.relations().stream()
            .filter(relation -> relation.main().equals(field) || relation.other().equals(field))
            .collect(Collectors.toSet());
        Stream<FieldWrapper<FieldSpec>> relationsOrdered = relations.stream()
            .map(relation -> relation.main().equals(field) ? relation : relation.inverse())
            .map(relation -> new FieldWrapper<>(relation.other(), relation.reduceToRelatedFieldSpec(newSpec)));

        relationsOrdered.forEach(wrapper -> applyToFieldSpecMap(specs, specs.get(wrapper.field), wrapper.other, wrapper.field));
        return new FieldSpecGroup(specs, relations);
    }

    private static final Map<Field, FieldSpec> applyToFieldSpecMap(Map<Field, FieldSpec> map,
                                                                   FieldSpec left,
                                                                   FieldSpec right,
                                                                   Field field) {
        FieldSpecMerger merger = new FieldSpecMerger();

        Optional<FieldSpec> newSpec = merger.merge(left, right);
        if (newSpec.isPresent()) {
            map.put(field, newSpec.get());
            return map;
        } else {
            throw new IllegalStateException("Failed to create field spec from value");
        }
    }

    private static final class DataBagGroupWrapper {

        private final DataBag dataBag;
        private final FieldSpecGroup group;
        private final FieldSpecValueGenerator generator;

        private DataBagGroupWrapper(DataBag databag,
                                    FieldSpecGroup group,
                                    FieldSpecValueGenerator generator) {
            this.dataBag = databag;
            this.group = group;
            this.generator = generator;
        }

        public DataBag dataBag() {
            return dataBag;
        }

        public FieldSpecValueGenerator generator() {
            return generator;
        }

    }

    private Stream<DataBag> createRemainingDataBags(Stream<DataBag> stream, Field first, FieldSpecGroup group) {
        Stream<DataBagGroupWrapper> initial = stream
            .map(dataBag -> new DataBagGroupWrapper(dataBag, group, underlyingGenerator))
            .map(wrapper -> adjustWrapperBounds(wrapper, first));
        Set<Field> toProcess = filterFromSet(group.fieldSpecs().keySet(), first);

        return recursiveMap(initial, toProcess).map(DataBagGroupWrapper::dataBag);
    }

    private static DataBagGroupWrapper adjustWrapperBounds(DataBagGroupWrapper wrapper, Field field) {
        DataBagValue value = wrapper.dataBag.getUnformattedValue(field);
        FieldSpecGroup newGroup = adjustBounds(field, value, wrapper.group);
        return new DataBagGroupWrapper(wrapper.dataBag, newGroup, wrapper.generator);

    }

    private static Stream<DataBagGroupWrapper> recursiveMap(Stream<DataBagGroupWrapper> wrapperStream,
                                                            Set<Field> fieldsToProcess) {
        if (fieldsToProcess.isEmpty()) {
            return wrapperStream;
        }

        Field field = fieldsToProcess.iterator().next();

        Stream<DataBagGroupWrapper> mappedStream =
            FlatMappingSpliterator.flatMap(wrapperStream, wrapper -> acceptNextValue(wrapper, field));

        Set<Field> remainingFields = filterFromSet(fieldsToProcess, field);

        return recursiveMap(mappedStream, remainingFields);
    }

    private static <T> Set<T> filterFromSet(Set<T> original, T element) {
        return original.stream()
            .filter(f -> !f.equals(element))
            .collect(Collectors.toSet());
    }

    private static Stream<DataBagGroupWrapper> acceptNextValue(DataBagGroupWrapper wrapper, Field field) {
        if (wrapper.generator.isRandom()) {
            return Stream.of(acceptNextRandomValue(wrapper, field));
        } else {
            return acceptNextNonRandomValue(wrapper, field);
        }
    }

    private static DataBagGroupWrapper acceptNextRandomValue(DataBagGroupWrapper wrapper, Field field) {
        FieldSpecGroup group = wrapper.group;

        DataBagValue nextValue = wrapper.generator().generateOne(group.fieldSpecs().get(field));

        DataBag combined = DataBag.merge(toDataBag(field, nextValue), wrapper.dataBag);

        FieldSpecGroup newGroup = adjustBounds(field, nextValue, group);

        return new DataBagGroupWrapper(combined, newGroup, wrapper.generator);
    }

    private static final class WrappedDataBag {
        private final DataBag dataBag;
        private final DataBagValue value;

        public WrappedDataBag(DataBag dataBag, DataBagValue value) {
            this.dataBag = dataBag;
            this.value = value;
        }
    }

    private static Stream<DataBagGroupWrapper> acceptNextNonRandomValue(DataBagGroupWrapper wrapper, Field field) {
        FieldSpecGroup group = wrapper.group;
        return wrapper.generator().generate(group.fieldSpecs().get(field))
            .map(value -> new WrappedDataBag(toDataBag(field, value), value))
            .map(wrapped -> new WrappedDataBag(DataBag.merge(wrapped.dataBag, wrapper.dataBag), wrapped.value))
            .map(combined -> new DataBagGroupWrapper(
                combined.dataBag, adjustBounds(field, combined.value, wrapper.group), wrapper.generator)
            );
    }

}
