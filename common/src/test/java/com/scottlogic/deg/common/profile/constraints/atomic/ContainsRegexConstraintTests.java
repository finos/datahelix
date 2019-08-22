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

package com.scottlogic.deg.common.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class ContainsRegexConstraintTests {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField", false);
        Field field2 = new Field("TestField", false);
        ContainsRegexConstraint constraint1 = new ContainsRegexConstraint(field1, Pattern.compile("[abc]"));
        ContainsRegexConstraint constraint2 = new ContainsRegexConstraint(field2, Pattern.compile("[abc]"));
        Assert.assertThat(constraint1, Matchers.equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = new Field("TestField", false);
        Field field2 = new Field("TestField2", false);
        ContainsRegexConstraint constraint1 = new ContainsRegexConstraint(field1, Pattern.compile("[abc]"));
        ContainsRegexConstraint constraint2 = new ContainsRegexConstraint(field2, Pattern.compile("[abc]"));
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = new Field("TestField", false);
        Field field2 = new Field("TestField", false);
        ContainsRegexConstraint constraint1 = new ContainsRegexConstraint(field1, Pattern.compile("[abc]"));
        ContainsRegexConstraint constraint2 = new ContainsRegexConstraint(field2, Pattern.compile("[abcd]"));
        Assert.assertNotEquals(constraint1, constraint2);
    }

}
