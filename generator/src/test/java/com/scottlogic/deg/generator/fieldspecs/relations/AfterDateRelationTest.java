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
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.restrictions.DateTimeLimit;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class AfterDateRelationTest {

    @Test
    public void reduceToRelatedFieldSpec_comparingTwoFields_givesALaterFieldSpec() {
        Field first = createField("first");
        Field second = createField("second");

        FieldSpecRelations relation = new AfterDateRelation(first, second, true);

        DateTimeLimit lower = new DateTimeLimit(
            OffsetDateTime.of(
                2005,
                6,
                3,
                0,
                0,
                0,
                0,
                ZoneOffset.UTC),
            true);

        DateTimeLimit upper = new DateTimeLimit(
            OffsetDateTime.of(
                2006,
                6,
                3,
                0,
                0,
                0,
                0,
                ZoneOffset.UTC),
            true);

        DateTimeRestrictions inRestrictions = new DateTimeRestrictions();
        inRestrictions.min = lower;
        inRestrictions.max = upper;

        FieldSpec inSpec = FieldSpec.Empty.withDateTimeRestrictions(inRestrictions);

        FieldSpec reducedSpec = relation.reduceToRelatedFieldSpec(inSpec);

        DateTimeRestrictions expectedRestrictions = new DateTimeRestrictions();
        expectedRestrictions.max = upper;
        FieldSpec expectedSpec = FieldSpec.Empty.withDateTimeRestrictions(expectedRestrictions);

        assertEquals(expectedSpec, reducedSpec);
    }

}