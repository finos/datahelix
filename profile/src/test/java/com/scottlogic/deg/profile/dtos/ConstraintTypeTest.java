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

package com.scottlogic.deg.profile.dtos;

import com.scottlogic.deg.profile.dtos.constraints.ConstraintType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;

class ConstraintTypeTest{

    @Test
    void fromText() {
        String greaterThanString = ConstraintType.GREATER_THAN_OR_EQUAL_TO.propertyName;
        ConstraintType greaterThanOrEqualTo = ConstraintType.fromPropertyName(greaterThanString);

        Assert.assertThat(greaterThanOrEqualTo, is(ConstraintType.GREATER_THAN_OR_EQUAL_TO));
    }

    @Test
    void fromTextLowerCase() {
        ConstraintType greaterThanOrEqualTo = ConstraintType.fromPropertyName("shorterthan");

        Assert.assertThat(greaterThanOrEqualTo, is(ConstraintType.SHORTER_THAN));
    }
}