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
    import com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions;
    import com.scottlogic.deg.generator.restrictions.linear.Limit;
    import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;
    import org.junit.jupiter.api.Test;

    import java.time.OffsetDateTime;
    import java.time.ZoneOffset;

    import static com.scottlogic.deg.generator.utils.Defaults.DATETIME_MAX_LIMIT;
    import static com.scottlogic.deg.common.profile.Types.DATETIME;
    import static org.junit.jupiter.api.Assertions.*;
    import static com.scottlogic.deg.common.profile.FieldBuilder.createField;
    
class BeforeDateRelationTest {

    @Test
    public void reduceToRelatedFieldSpec_comparingTwoFields_givesAnEarlierFieldSpec() {
        Field first = createField("first");
        Field second = createField("second");

        FieldSpecRelations relation = new BeforeDateRelation(first, second, true);

        Limit<OffsetDateTime> lower = new Limit<>(
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

        Limit<OffsetDateTime> upper = new Limit<>(
            OffsetDateTime.of(
                2006,
                6,
                3
                ,0,
                0,
                0,
                0,
                ZoneOffset.UTC),
            true);

        DateTimeRestrictions inRestrictions = LinearRestrictionsFactory.createDateTimeRestrictions(lower, upper);

        FieldSpec inSpec = FieldSpec.fromType(DATETIME).withDateTimeRestrictions(inRestrictions);

        FieldSpec reducedSpec = relation.reduceToRelatedFieldSpec(inSpec);

        DateTimeRestrictions expectedRestrictions = LinearRestrictionsFactory.createDateTimeRestrictions(lower, DATETIME_MAX_LIMIT);
        FieldSpec expectedSpec = FieldSpec.fromType(DATETIME).withDateTimeRestrictions(expectedRestrictions);

        assertEquals(expectedSpec, reducedSpec);
    }

}
