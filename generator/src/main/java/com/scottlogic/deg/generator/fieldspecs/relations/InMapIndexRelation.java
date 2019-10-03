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
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class InMapIndexRelation implements FieldSpecRelations {
    private final Field main;
    private final Field other;
    private final DistributedList<String> underlyingList;

    public InMapIndexRelation(Field main, Field other, DistributedList<String> underlyingList) {
        this.main = main;
        this.other = other;
        this.underlyingList = underlyingList;
    }

    @Override
    public FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue) {
        int size = underlyingList.list().size();

        FieldSpec controllerSpec = FieldSpec.fromRestriction(LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(BigDecimal.ZERO, true),
            new Limit<>(BigDecimal.valueOf(size), false),
            0
        )).withNotNull();

        for (int i = 0; i < size; i++) {
            if (controllerSpec.getBlacklist().contains(BigDecimal.valueOf(i))) {
                continue;
            }
            Object testingElement = underlyingList.list().get(i);
            if (!otherValue.permits(testingElement)) {
                Set<Object> newBlackList = new HashSet<>(controllerSpec.getBlacklist());
                newBlackList.add(BigDecimal.valueOf(i));
                controllerSpec = controllerSpec.withBlacklist(newBlackList);
            }
        }
        return controllerSpec;
    }

    @Override
    public FieldSpec reduceValueToFieldSpec(DataBagValue generatedValue) {
        throw new UnsupportedOperationException("reduceToFieldSpec is unsuported in InMapIndexRelation");
    }

    @Override
    public FieldSpecRelations inverse() {
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

    public DistributedList<String> getUnderlyingList() {
        return this.underlyingList;
    }
}
