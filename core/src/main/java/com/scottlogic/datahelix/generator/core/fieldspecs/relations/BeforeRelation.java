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

package com.scottlogic.datahelix.generator.core.fieldspecs.relations;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Granularity;
import com.scottlogic.datahelix.generator.common.util.defaults.LinearDefaults;
import com.scottlogic.datahelix.generator.core.fieldspecs.*;
import com.scottlogic.datahelix.generator.core.generation.databags.DataBagValue;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictions;

import static com.scottlogic.datahelix.generator.common.util.GranularityUtils.readGranularity;

public class BeforeRelation<T extends Comparable<T>> implements FieldSpecRelation {
    private final Field main;
    private final Field other;
    private final boolean inclusive;
    private final LinearDefaults<T> defaults;
    private final Granularity<T> offsetGranularity;
    private final int offset;

    public BeforeRelation(Field main, Field other, boolean inclusive, LinearDefaults<T> defaults, Granularity<T> offsetGranularity, int offset) {
        this.main = main;
        this.other = other;
        this.inclusive = inclusive;
        this.defaults = defaults;
        this.offsetGranularity = offsetGranularity != null ? offsetGranularity : readGranularity(main.getType(), null);
        this.offset = offset;
    }

    @Override
    public FieldSpec createModifierFromOtherFieldSpec(FieldSpec otherFieldSpec) {
        if (otherFieldSpec instanceof NullOnlyFieldSpec){
            return FieldSpecFactory.nullOnly();
        }
        if (otherFieldSpec instanceof WhitelistFieldSpec) {
            throw new UnsupportedOperationException("cannot combine sets with before relation, Issue #1489");
        }

        LinearRestrictions<T> otherRestrictions = (LinearRestrictions)((RestrictionsFieldSpec) otherFieldSpec).getRestrictions();
        if (otherRestrictions.isContradictory()){
            return FieldSpecFactory.nullOnly();
        }
        T max = otherRestrictions.getMax();
        T offsetMax = offsetGranularity.getPrevious(max, offset);

        return createFromMax(offsetMax, offsetGranularity);
    }

    @Override
    public FieldSpec createModifierFromOtherValue(DataBagValue otherFieldGeneratedValue) {
        if (otherFieldGeneratedValue.getValue() == null) return FieldSpecFactory.fromType(main.getType());

        T offsetValue = offset > 0
            ? offsetGranularity.getPrevious((T) otherFieldGeneratedValue.getValue(), offset)
            : (T) otherFieldGeneratedValue.getValue();

        return  createFromMax(offsetValue, defaults.granularity());
    }

    private FieldSpec createFromMax(T max, Granularity<T> granularity) {
        if (!inclusive){
            max = granularity.getPrevious(max);
        }

        return FieldSpecFactory.fromRestriction(new LinearRestrictions<>(defaults.min(), max, granularity));
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
    public FieldSpecRelation inverse() {
         return new AfterRelation(other, main, inclusive, defaults, offsetGranularity,-1 * offset);
    }

    @Override
    public String toString() {
        return String.format("%s is before %s%s %s %s", main, inclusive ? "or equal to " : "", other, offset >= 0 ? "plus" : "minus", Math.abs(offset));
    }

    @Override
    public Constraint negate() {
        throw new UnsupportedOperationException("Negating relations with an offset is not supported");
    }
}
