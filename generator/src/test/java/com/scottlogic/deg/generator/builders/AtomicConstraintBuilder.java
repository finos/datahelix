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

package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.utils.SetUtils;

public class AtomicConstraintBuilder {
    private ConstraintNodeBuilderDepreciated constraintNodeBuilderDepreciated;
    private Field field;

    protected AtomicConstraintBuilder(ConstraintNodeBuilderDepreciated constraintNodeBuilderDepreciated, Field field) {
        this.constraintNodeBuilderDepreciated = constraintNodeBuilderDepreciated;
        this.field = field;
    }

    private DistributedSet<Object> whitelistOf(Object... values) {
        return FrequencyDistributedSet.uniform(SetUtils.setOf(values));
    }

    public ConstraintNodeBuilderDepreciated isInSet(Object... legalValues) {
        IsInSetConstraint isInSetConstraint = new IsInSetConstraint(
            field,
            whitelistOf(legalValues));
        constraintNodeBuilderDepreciated.constraints.add(isInSetConstraint);
        return constraintNodeBuilderDepreciated;
    }

    public ConstraintNodeBuilderDepreciated isNotInSet(Object... legalValues) {
        AtomicConstraint isInSetConstraint = new IsInSetConstraint(
            field,
            whitelistOf(legalValues)
        ).negate();
        constraintNodeBuilderDepreciated.constraints.add(isInSetConstraint);
        return constraintNodeBuilderDepreciated;
    }

    public ConstraintNodeBuilderDepreciated isNull() {
        IsNullConstraint isNullConstraint = new IsNullConstraint(field);
        constraintNodeBuilderDepreciated.constraints.add(isNullConstraint);
        return constraintNodeBuilderDepreciated;
    }

    public ConstraintNodeBuilderDepreciated isNotNull() {
        AtomicConstraint isNotNullConstraint = new IsNullConstraint(field).negate();
        constraintNodeBuilderDepreciated.constraints.add(isNotNullConstraint);
        return constraintNodeBuilderDepreciated;
    }

    public ConstraintNodeBuilderDepreciated isSelfContradictory() {
        IsNullConstraint isNullConstraint = new IsNullConstraint(field);
        AtomicConstraint isNotNullConstraint = new IsNullConstraint(field).negate();
        constraintNodeBuilderDepreciated.constraints.add(isNullConstraint);
        constraintNodeBuilderDepreciated.constraints.add(isNotNullConstraint);
        return constraintNodeBuilderDepreciated;
    }
}
