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

package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Granularity;
import com.scottlogic.deg.common.util.defaults.LinearDefaults;
import com.scottlogic.deg.generator.fieldspecs.*;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;

public class AfterRelation<T extends Comparable<T>> implements FieldSpecRelations {
    private final Field main;
    private final Field other;
    private final boolean inclusive;
    private final LinearDefaults<T> defaults;

    public AfterRelation(Field main, Field other, boolean inclusive, LinearDefaults<T> defaults) {
        this.main = main;
        this.other = other;
        this.inclusive = inclusive;
        this.defaults = defaults;
    }

    @Override
    public FieldSpec createModifierFromOtherFieldSpec(FieldSpec otherFieldSpec) {
        if (otherFieldSpec instanceof NullOnlyFieldSpec){
            return FieldSpecFactory.nullOnly();
        }
        if (otherFieldSpec instanceof WhitelistFieldSpec) {
            throw new UnsupportedOperationException("cannot combine sets with after relation, Issue #1489");
        }

        LinearRestrictions<T> lr = (LinearRestrictions)((RestrictionsFieldSpec) otherFieldSpec).getRestrictions();
        return createFieldSpec(lr.getMin(), lr.getGranularity());
    }

    @Override
    public FieldSpec createModifierFromOtherValue(DataBagValue otherFieldGeneratedValue) {
        if (otherFieldGeneratedValue.getValue() == null) return FieldSpecFactory.fromType(main.getType());
        return createFieldSpec((T) otherFieldGeneratedValue.getValue(), defaults.granularity());
    }

    private FieldSpec createFieldSpec(T min, Granularity<T> granularity) {
        if (!inclusive){
            min = granularity.getNext(min);
        }

        return FieldSpecFactory.fromRestriction(new LinearRestrictions<>(min, defaults.max(), granularity));
    }

    @Override
    public FieldSpecRelations inverse() {
        return new BeforeRelation(other, main, inclusive, defaults);
    }

    @Override
    public Field main() {
        return main;
    }

    @Override
    public Field other() {
        return other;
    }

    @Override
    public String toString() {
        return String.format("%s is after %s%s", main, inclusive ? "or equal to " : "", other);
    }

    @Override
    public Constraint negate() {
        return new BeforeRelation(main, other, !inclusive, defaults);
    }
}
