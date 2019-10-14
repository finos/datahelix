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
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.profile.constraints.Constraint;

import java.math.BigDecimal;

public class InMapRelation implements FieldSpecRelations {
    private final Field main;
    private final Field other;
    private final DistributedList<String> underlyingList;

    public InMapRelation(Field main, Field other, DistributedList<String> underlyingList) {
        this.main = main;
        this.other = other;
        this.underlyingList = underlyingList;
    }

    @Override
    public FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue) {
        throw new UnsupportedOperationException("ReduceToRelatedFieldSpec is unsupported in InMapRelation");
    }

    @Override
    public FieldSpec reduceValueToFieldSpec(DataBagValue generatedValue) {
        BigDecimal value = (BigDecimal)generatedValue.getValue();

        DistributedList<Object> newList = DistributedList.singleton(underlyingList.list().get(value.intValue()));
        return FieldSpecFactory.fromList(newList);
    }

    @Override
    public FieldSpecRelations inverse() {
        return new InMapIndexRelation(other, main, underlyingList);
    }

    @Override
    public Field main() {
        return main;
    }

    @Override
    public Field other() {
        return other;
    }

    public DistributedList<String> getUnderlyingList() {
        return this.underlyingList;
    }

    @Override
    public Constraint negate() {
        throw new UnsupportedOperationException("in map relations cannot currently be negated");
    }
}
