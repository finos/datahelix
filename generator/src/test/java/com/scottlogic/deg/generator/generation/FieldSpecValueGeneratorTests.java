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

package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.scottlogic.deg.generator.config.detail.DataGenerationType.INTERESTING;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class FieldSpecValueGeneratorTests {

    @Test
    void generate_fieldSpecMustContainRestrictionNullAndSetRestrictionsHasValues_returnsDataBagsWithValuesInSetRestrictions() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNotNull()
            .withWhitelist(
                (FrequencyDistributedSet.uniform(
                    new HashSet<>(
                        Arrays.asList(10, 20, 30)))));
        FieldSpecValueGenerator fieldSpecFulfiller = new FieldSpecValueGenerator(
            INTERESTING,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<DataBagValue> result = fieldSpecFulfiller.generate(fieldSpec).collect(Collectors.toSet());

        Set<DataBagValue> expectedDataBags = fieldSpec.getWhitelist().set()
            .stream()
            .map(value -> new DataBagValue(value, null))
            .collect(Collectors.toSet());

        assertThat(result, sameBeanAs(expectedDataBags));
    }

    @Test
    void generate_fieldSpecMustContainRestrictionNullAndNumericRestrictionApplied_returnsExpectedDataBagsForNumericRestriction() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withNumericRestrictions(
                new NumericRestrictions() {{
                    min = new NumericLimit<>(new BigDecimal(10), false);
                    max = new NumericLimit<>(new BigDecimal(30), false);
                }})
            .withTypeRestrictions(
                new TypeRestrictions(
                    Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
                )
            );
        FieldSpecValueGenerator fieldSpecFulfiller = new FieldSpecValueGenerator(
            INTERESTING,
            new StandardFieldValueSourceEvaluator(),
            new JavaUtilRandomNumberGenerator());

        final Set<DataBagValue> result =
            fieldSpecFulfiller.generate(fieldSpec).collect(Collectors.toSet());

        Set<DataBagValue> expectedDataBags = new HashSet<>(
            Arrays.asList(
                new DataBagValue(
                    new BigDecimal("10.00000000000000000001"),
                    null
                ),
                new DataBagValue(
                    new BigDecimal("10.00000000000000000002"),
                    null
                ),
                new DataBagValue(
                    new BigDecimal("29.99999999999999999998"),
                    null
                ),
                new DataBagValue(
                    new BigDecimal("29.99999999999999999999"),
                    null
                ),
                new DataBagValue(null, null)
            )
        );

        assertThat(result, sameBeanAs(expectedDataBags));
    }
}
