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
import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpec;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecFactory;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;
import com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictionsFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class InMapIndexRelationTest {
    private static InMapIndexRelation testInstance;

    @BeforeAll
    static void before() {
        Field f1 = createField("field1");
        Field f2 = createField("field1");
        List<Object> values = Arrays.asList("foo", "bar");

        testInstance = new InMapIndexRelation(f1, f2, DistributedList.uniform(values));
    }

    @Test
    void reduceToRelatedFieldSpec_whenAllValid_returnCompleteWhiteList() {
        FieldSpec parameter = FieldSpecFactory.fromType(FieldType.STRING);

        FieldSpec expected = FieldSpecFactory.fromList(DistributedList.uniform(Arrays.asList(0, 1))).withNotNull();
        FieldSpec actual = testInstance.createModifierFromOtherFieldSpec(parameter);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    void reduceToRelatedFieldSpec_whenSomeValid_returnReducedWhiteList() {
        FieldSpec parameter = FieldSpecFactory.fromRestriction(StringRestrictionsFactory.forStringContaining(Pattern.compile("^f.*"), false));

        FieldSpec expected = FieldSpecFactory.fromList(DistributedList.uniform(Collections.singletonList(0))).withNotNull();
        FieldSpec actual = testInstance.createModifierFromOtherFieldSpec(parameter);

        assertThat(actual, sameBeanAs(expected));
    }
}