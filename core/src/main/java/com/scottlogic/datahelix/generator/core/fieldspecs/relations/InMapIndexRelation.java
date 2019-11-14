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
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpec;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecFactory;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;
import com.scottlogic.datahelix.generator.core.generation.databags.DataBagValue;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InMapIndexRelation implements FieldSpecRelation
{
    private final Field main;
    private final Field other;
    private final DistributedList<Object> underlyingList;

    public InMapIndexRelation(Field main, Field other, DistributedList<Object> underlyingList) {
        this.main = main;
        this.other = other;
        this.underlyingList = underlyingList;
    }

    @Override
    public FieldSpec createModifierFromOtherFieldSpec(FieldSpec otherFieldSpec) {
        List<Object> whiteList = new ArrayList<>();

        for (int i = 0; i < underlyingList.list().size(); i++) {
            Object testingElement = underlyingList.list().get(i);
            if (otherFieldSpec.canCombineWithWhitelistValue(testingElement)) {
                whiteList.add(BigDecimal.valueOf(i));
            }
        }
        return FieldSpecFactory.fromList(DistributedList.uniform(whiteList)).withNotNull();
    }

    @Override
    public FieldSpec createModifierFromOtherValue(DataBagValue otherFieldGeneratedValue) {
        throw new UnsupportedOperationException("reduceToFieldSpec is unsuported in InMapIndexRelation");
    }

    @Override
    public FieldSpecRelation inverse() {
        return new InMapRelation(main, other, underlyingList);
    }

    @Override
    public Field main() {
        return main;
    }

    @Override
    public Field other() {
        return other;
    }

    public DistributedList<Object> getUnderlyingList() {
        return this.underlyingList;
    }

    @Override
    public Constraint negate() {
        throw new UnsupportedOperationException("in map relations cannot currently be negated");
    }
}
