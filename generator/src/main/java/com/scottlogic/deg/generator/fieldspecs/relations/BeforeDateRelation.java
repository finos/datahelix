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
import com.scottlogic.deg.common.profile.constraintdetail.Granularity;
import com.scottlogic.deg.common.util.defaults.LinearDefaults;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;

import java.time.OffsetDateTime;

import static com.scottlogic.deg.common.util.Defaults.*;

public class BeforeDateRelation implements FieldSpecRelations {
    private final Field main;
    private final Field other;
    private final boolean inclusive;
    private final LinearDefaults<OffsetDateTime> defaults;

    public BeforeDateRelation(Field main, Field other, boolean inclusive, LinearDefaults<OffsetDateTime> defaults) {
        this.main = main;
        this.other = other;
        this.inclusive = inclusive;
        this.defaults = defaults;
    }

    @Override
    public FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue) {
        LinearRestrictions<OffsetDateTime> lr = (LinearRestrictions) otherValue.getRestrictions();
        if (lr == null){
            return FieldSpec.empty();
        }

        return createFromMax(lr.getMax(), lr.getGranularity());
    }

    @Override
    public FieldSpec reduceValueToFieldSpec(DataBagValue generatedValue) {
        return createFromMax((OffsetDateTime) generatedValue.getValue(), defaults.granularity());
    }

    private FieldSpec createFromMax(OffsetDateTime max, Granularity<OffsetDateTime> granularity) {
        if (!inclusive){
            max = granularity.getPrevious(max);
        }

        return FieldSpec.fromRestriction(new LinearRestrictions<>(defaults.min(), max, granularity));
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
    public FieldSpecRelations inverse() {
        return new AfterDateRelation(other, main, inclusive, defaults);
    }

    @Override
    public String toString() {
        return String.format("%s is before %s%s", main, inclusive ? "or equal to " : "", other);
    }

    @Override
    public Constraint negate() {
        return new AfterDateRelation(main, other, !inclusive, defaults);
    }
}
