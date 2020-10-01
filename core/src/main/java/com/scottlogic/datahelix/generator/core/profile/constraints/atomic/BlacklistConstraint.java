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
package com.scottlogic.datahelix.generator.core.profile.constraints.atomic;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpec;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecFactory;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

public class BlacklistConstraint implements AtomicConstraint {
    public final Field field;
    public final DistributedList<Object> legalValues;

    public BlacklistConstraint(Field field, DistributedList<Object> legalValues) {
        this.field = field;
        this.legalValues = legalValues;

        if (legalValues.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an IsInSetConstraint for field '" +
                field.getName() + "' with an empty set");
        }

        if (legalValues.list().contains(null)) {
            throw new IllegalArgumentException("Cannot create an IsInSetConstraint for field '" +
                field.getName() + "' with a set containing null");
        }
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        return new InSetConstraint(field, legalValues);
    }

    @Override
    public FieldSpec toFieldSpec() {
        return FieldSpecFactory.fromType(field.getType()).withBlacklist(new HashSet<>(legalValues.list()));
    }

    public String toString(){
        boolean overLimit = legalValues.list().size() > 3;
        return String.format("%s in [%s%s](%d values)",
            field.getName(),
            legalValues.stream().limit(3).map(Object::toString).collect(Collectors.joining(", ")),
            overLimit ? ", ..." : "",
            legalValues.list().size());
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlacklistConstraint constraint = (BlacklistConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(legalValues, constraint.legalValues);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, legalValues);
    }
}
