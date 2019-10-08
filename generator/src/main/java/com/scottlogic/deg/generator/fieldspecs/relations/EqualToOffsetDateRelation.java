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
import com.scottlogic.deg.common.profile.constraintdetail.DateTimeGranularity;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;

import java.time.OffsetDateTime;

public class EqualToOffsetDateRelation implements FieldSpecRelations {
    private final Field main;
    private final Field other;
    private final DateTimeGranularity offsetGranularity;
    private final int offset;

    public EqualToOffsetDateRelation(Field main,
                                     Field other,
                                     DateTimeGranularity offsetGranularity,
                                     int offset) {
        this.main = main;
        this.other = other;
        this.offsetGranularity = offsetGranularity;
        this.offset = offset;
    }

    @Override
    public FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue) {
        if (otherValue.getRestrictions() != null) {
            OffsetDateTime time = ((LinearRestrictions<OffsetDateTime>) otherValue.getRestrictions()).getMin();
            OffsetDateTime newTime = offsetGranularity.getNext(time, offset);
            LinearRestrictions<OffsetDateTime> newRestrictions = new LinearRestrictions<>(newTime, newTime, ((LinearRestrictions<OffsetDateTime>) otherValue.getRestrictions()).getGranularity());
            return FieldSpec.fromRestriction(newRestrictions);
        } else {
            return FieldSpec.empty();
        }
    }

    @Override
    public FieldSpec reduceValueToFieldSpec(DataBagValue generatedValue) {
        Limit<OffsetDateTime> limit = new Limit<>((OffsetDateTime)generatedValue.getValue(), true);
        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(limit,limit);
        FieldSpec newSpec = FieldSpec.fromRestriction(restrictions).withNotNull();
        return reduceToRelatedFieldSpec(newSpec);
    }

    @Override
    public FieldSpecRelations inverse() {
        return new EqualToOffsetDateRelation(other, main, offsetGranularity, -offset);
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
    public Constraint negate() {
        throw new UnsupportedOperationException("equalTo relations cannot currently be negated");
    }
}
