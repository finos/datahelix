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
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.utils.SetUtils;

public class TestAtomicConstraintBuilder {
    private TestConstraintNodeBuilder testConstraintNodeBuilder;
    private Field field;

    protected TestAtomicConstraintBuilder(TestConstraintNodeBuilder testConstraintNodeBuilder, Field field) {
        this.testConstraintNodeBuilder = testConstraintNodeBuilder;
        this.field = field;
    }

    private DistributedList<Object> whitelistOf(Object... values) {
        return DistributedList.uniform(SetUtils.setOf(values));
    }

    public TestConstraintNodeBuilder isInSet(Object... legalValues) {
        IsInSetConstraint isInSetConstraint = new IsInSetConstraint(
            field,
            whitelistOf(legalValues));
        testConstraintNodeBuilder.constraints.add(isInSetConstraint);
        return testConstraintNodeBuilder;
    }

    public TestConstraintNodeBuilder isNotInSet(Object... legalValues) {
        AtomicConstraint isInSetConstraint = new IsInSetConstraint(
            field,
            whitelistOf(legalValues)
        ).negate();
        testConstraintNodeBuilder.constraints.add(isInSetConstraint);
        return testConstraintNodeBuilder;
    }

    public TestConstraintNodeBuilder isNull() {
        IsNullConstraint isNullConstraint = new IsNullConstraint(field);
        testConstraintNodeBuilder.constraints.add(isNullConstraint);
        return testConstraintNodeBuilder;
    }

    public TestConstraintNodeBuilder isNotNull() {
        AtomicConstraint isNotNullConstraint = new IsNullConstraint(field).negate();
        testConstraintNodeBuilder.constraints.add(isNotNullConstraint);
        return testConstraintNodeBuilder;
    }

    public TestConstraintNodeBuilder isSelfContradictory() {
        IsNullConstraint isNullConstraint = new IsNullConstraint(field);
        AtomicConstraint isNotNullConstraint = new IsNullConstraint(field).negate();
        testConstraintNodeBuilder.constraints.add(isNullConstraint);
        testConstraintNodeBuilder.constraints.add(isNotNullConstraint);
        return testConstraintNodeBuilder;
    }
}
