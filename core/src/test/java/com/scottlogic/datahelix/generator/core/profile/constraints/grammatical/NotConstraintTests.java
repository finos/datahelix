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

package com.scottlogic.datahelix.generator.core.profile.constraints.grammatical;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.core.builders.TestAtomicConstraintBuilder;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.InSetConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.IsNullConstraint;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;

public class NotConstraintTests {
    @Test
    public void testConstraintIsEqual() {
        Field field1 = createField("TestField");
        Field field2 = createField("TestField");
        Constraint constraint1 = new IsNullConstraint(field1).negate();
        Constraint constraint2 = new IsNullConstraint(field2).negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursively() {
        Field field1 = createField("TestField");
        Field field2 = createField("TestField");
        Constraint constraint1 = new IsNullConstraint(field1).negate();
        Constraint constraint2 = new IsNullConstraint(field2).negate().negate().negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursivelySameLevel() {
        Field field1 = createField("TestField");
        Field field2 = createField("TestField");
        Constraint constraint1 = new IsNullConstraint(field1).negate().negate().negate();
        Constraint constraint2 = new IsNullConstraint(field2).negate().negate().negate();
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = createField("TestField");
        Field field2 = createField("TestField2");
        Constraint constraint1 = new IsNullConstraint(field1).negate();
        Constraint constraint2 = new IsNullConstraint(field2).negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = createField("TestField");
        Field field2 = createField("TestField");
        Constraint constraint1 = new InSetConstraint(
            field1,
            TestAtomicConstraintBuilder.inSetRecordsFrom("abc")
            ).negate();
        Constraint constraint2 = new InSetConstraint(
            field2,
            TestAtomicConstraintBuilder.inSetRecordsFrom("abcd")
            ).negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualRecursively() {
        Field field1 = createField("TestField");
        Field field2 = createField("TestField");
        Constraint constraint1 = new IsNullConstraint(field1).negate();
        Constraint constraint2 = new IsNullConstraint(field2).negate().negate();
        Assert.assertNotEquals(constraint1, constraint2);
    }

}
