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

package com.scottlogic.deg.generator.profile.constraints.grammatical;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsNullConstraint;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

public class AndConstraintTests {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = createField("TestField");
        Field field2 = createField("TestField");

        Field field3 = createField("TestField");
        Field field4 = createField("TestField");
        AndConstraint constraint1 = new AndConstraint(new IsNullConstraint(field1), new IsNullConstraint(field2));
        AndConstraint constraint2 = new AndConstraint(new IsNullConstraint(field3), new IsNullConstraint(field4));
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursively() {
        Field field1 = createField("TestField");
        Field field2 = createField("TestField");

        Field field3 = createField("TestField");
        Field field4 = createField("TestField");
        AndConstraint constraint1 = new AndConstraint(new AndConstraint(new IsNullConstraint(field1), new IsNullConstraint(field2)));
        AndConstraint constraint2 = new AndConstraint(new AndConstraint(new IsNullConstraint(field3), new IsNullConstraint(field4)));
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }
}
