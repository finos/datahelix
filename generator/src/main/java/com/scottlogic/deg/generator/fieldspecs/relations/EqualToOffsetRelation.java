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
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;

public class EqualToOffsetRelation<T extends Comparable<T>> implements FieldSpecRelations {
    private final Field main;
    private final Field other;
    private final Granularity<T> offsetGranularity;
    private final int offset;

    public EqualToOffsetRelation(Field main,
                                 Field other,
                                 Granularity<T> offsetGranularity,
                                 int offset) {
        this.main = main;
        this.other = other;
        this.offsetGranularity = offsetGranularity;
        this.offset = offset;
    }

    @Override
    public FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue) {
        if (otherValue.getRestrictions() == null) {
            return FieldSpecFactory.fromType(main.getType());
        }

        LinearRestrictions<T> otherRestrictions = (LinearRestrictions) otherValue.getRestrictions();
        T min = otherRestrictions.getMin();
        T offsetMin = offsetGranularity.getNext(min, offset);
        T max = otherRestrictions.getMax();
        T offsetMax = offsetGranularity.getNext(max, offset);

        return FieldSpecFactory.fromRestriction(new LinearRestrictions(offsetMin, offsetMax, otherRestrictions.getGranularity()));
    }

    @Override
    public FieldSpec reduceValueToFieldSpec(DataBagValue generatedValue) {
        T offsetValue = offsetGranularity.getNext((T) generatedValue.getValue(), offset);
        return FieldSpecFactory.fromList(DistributedList.singleton(offsetValue));
    }

    @Override
    public FieldSpecRelations inverse() {
        return new EqualToOffsetRelation(other, main, offsetGranularity, -offset);
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
