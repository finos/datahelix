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

import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;

class AtomicConstraintTypeTest {

    @Test
    void fromText() {
        String greaterThanString = AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT.getText();
        AtomicConstraintType greaterThanOrEqualTo = AtomicConstraintType.fromText(greaterThanString);

        Assert.assertThat(greaterThanOrEqualTo, is(AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT));
    }

    @Test
    void fromTextLowerCase() {
        AtomicConstraintType greaterThanOrEqualTo = AtomicConstraintType.fromText("shorterthan");

        Assert.assertThat(greaterThanOrEqualTo, is(AtomicConstraintType.IS_STRING_SHORTER_THAN));
    }
}