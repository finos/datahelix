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
import com.scottlogic.deg.generator.fieldspecs.whitelist.ElementFrequency;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyWhitelist;
import com.scottlogic.deg.generator.fieldspecs.whitelist.Whitelist;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AtomicConstraintBuilder {
    private ConstraintNodeBuilder constraintNodeBuilder;
    private Field field;

    protected AtomicConstraintBuilder(ConstraintNodeBuilder constraintNodeBuilder, Field field) {
        this.constraintNodeBuilder = constraintNodeBuilder;
        this.field = field;
    }

    private Whitelist<Object> whitelistOf(Object... values) {
        return new FrequencyWhitelist<>(
            Arrays.stream(values)
                .map(value -> new ElementFrequency<>(value, 1.0F))
                .collect(Collectors.toSet())
        );
    }

    public ConstraintNodeBuilder isInSet(Object... legalValues) {
        IsInSetConstraint isInSetConstraint = new IsInSetConstraint(
            field,
            whitelistOf(legalValues));
        constraintNodeBuilder.constraints.add(isInSetConstraint);
        return constraintNodeBuilder;
    }

    public ConstraintNodeBuilder isNotInSet(Object... legalValues) {
        AtomicConstraint isInSetConstraint = new IsInSetConstraint(
            field,
            whitelistOf(legalValues)
        ).negate();
        constraintNodeBuilder.constraints.add(isInSetConstraint);
        return constraintNodeBuilder;
    }

    public ConstraintNodeBuilder isNull() {
        IsNullConstraint isNullConstraint = new IsNullConstraint(field);
        constraintNodeBuilder.constraints.add(isNullConstraint);
        return constraintNodeBuilder;
    }

    public ConstraintNodeBuilder isNotNull() {
        AtomicConstraint isNotNullConstraint = new IsNullConstraint(field).negate();
        constraintNodeBuilder.constraints.add(isNotNullConstraint);
        return constraintNodeBuilder;
    }

    public ConstraintNodeBuilder isSelfContradictory() {
        IsNullConstraint isNullConstraint = new IsNullConstraint(field);
        AtomicConstraint isNotNullConstraint = new IsNullConstraint(field).negate();
        constraintNodeBuilder.constraints.add(isNullConstraint);
        constraintNodeBuilder.constraints.add(isNotNullConstraint);
        return constraintNodeBuilder;
    }
}
