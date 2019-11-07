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
import com.scottlogic.deg.common.profile.SpecificFieldType;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class EqualToDateRelationTest {

    private final Field a = new Field("a", SpecificFieldType.DATETIME, false ,"", false, false);
    private final Field b = new Field("b", SpecificFieldType.DATETIME, false, "", false, false);
    private final FieldSpecRelations equalToDateRelations = new EqualToRelation(a, b);

    @Test
    public void testReduceToFieldSpec_withNotNull_reducesToSpec() {
        OffsetDateTime value = OffsetDateTime.of(2000,
            1,
            1,
            0,
            0,
            0,
            0,
            ZoneOffset.UTC);
        DataBagValue generatedValue = new DataBagValue(value);

        FieldSpec result = equalToDateRelations.createModifierFromOtherValue(generatedValue);

        FieldSpec expected = FieldSpecFactory.fromList(DistributedList.singleton(value));
        assertThat(result, sameBeanAs(expected));
    }

    @Test
    public void testReduceToFieldSpec_withNull_reducesToSpec() {
        OffsetDateTime value = null;
        DataBagValue generatedValue = new DataBagValue(value);

        FieldSpec result = equalToDateRelations.createModifierFromOtherValue(generatedValue);

        FieldSpec expected = FieldSpecFactory.nullOnly();
        assertThat(result, sameBeanAs(expected));
    }

}