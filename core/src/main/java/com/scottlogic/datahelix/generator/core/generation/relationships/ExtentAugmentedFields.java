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

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.common.profile.SpecificFieldType;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ExtentAugmentedFields implements Fields {
    private static final SpecificFieldType integer = new SpecificFieldType("integer", FieldType.NUMERIC, null);
    private static final String minField = "min";
    private static final String maxField = "max";
    private static final Field min = new Field(minField, integer, false, null, false, true, null);
    private static final Field max = new Field(maxField, integer, false, null, false, true, null);

    private final Fields underlying;

    public ExtentAugmentedFields(Fields fields) {
        this.underlying = fields;
    }

    @Override
    public Field getByName(String fieldName) {
        if (fieldName.equals(minField)) {
            return min;
        }

        if (fieldName.equals(maxField)) {
            return max;
        }

        return underlying.getByName(fieldName);
    }

    @Override
    public int size() {
        return underlying.size();
    }

    @Override
    public Stream<Field> stream() {
        return Stream.concat(
            underlying.stream(),
            Stream.of(min, max));
    }

    @Override
    public Stream<Field> getExternalStream() {
        return underlying.getExternalStream();
    }

    @Override
    public List<Field> asList() {
        return underlying.asList();
    }

    @Override
    public Iterator<Field> iterator() {
        return underlying.iterator();
    }

    public boolean isExtentField(Field field) {
        return field.equals(min) || field.equals(max);
    }

    public OneToManyRange applyExtent(OneToManyRange range, Field field, int extent) {
        if (field.equals(min)) {
            return range.withMin(extent);
        } else if (field.equals(max)) {
            return range.withMax(extent);
        }

        return range;
    }
}
