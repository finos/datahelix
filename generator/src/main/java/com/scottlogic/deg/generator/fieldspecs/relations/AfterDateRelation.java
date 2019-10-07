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
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;

import java.time.OffsetDateTime;

import static com.scottlogic.deg.common.util.Defaults.DEFAULT_DATETIME_GRANULARITY;
import static com.scottlogic.deg.common.util.Defaults.ISO_MAX_DATE;
import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MAX_LIMIT;
import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MIN_LIMIT;


public class AfterDateRelation implements FieldSpecRelations {
    private final Field main;
    private final Field other;
    private final boolean inclusive;
    private final Granularity<OffsetDateTime> defaultGranularity = DEFAULT_DATETIME_GRANULARITY;
    private final OffsetDateTime defaultMax = ISO_MAX_DATE;

    public AfterDateRelation(Field main, Field other, boolean inclusive) {
        this.main = main;
        this.other = other;
        this.inclusive = inclusive;
    }

    @Override
    public FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue) {
        LinearRestrictions<OffsetDateTime> lr = (LinearRestrictions) otherValue.getRestrictions();
        if (lr == null){
            return FieldSpec.empty();
        }

        return createFieldSpec(lr.getMin(), lr.getGranularity());
    }

    @Override
    public FieldSpec reduceValueToFieldSpec(DataBagValue generatedValue) {
        return createFieldSpec((OffsetDateTime) generatedValue.getValue(), defaultGranularity);
    }

    private FieldSpec createFieldSpec(OffsetDateTime min, Granularity<OffsetDateTime> granularity) {
        if (!inclusive){
            min = granularity.getNext(min);
        }

        return FieldSpec.fromRestriction(new LinearRestrictions<>(min, defaultMax, granularity));
    }

    @Override
    public FieldSpecRelations inverse() {
        return new BeforeDateRelation(other, main, inclusive);
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
        return new BeforeDateRelation(main, other, !inclusive);
    }
}
