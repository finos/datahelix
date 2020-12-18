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

package com.scottlogic.datahelix.generator.core.generation;

import com.scottlogic.datahelix.generator.common.RandomNumberGenerator;
import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpec;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecFactory;
import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.NullAppendingValueSource;
import com.scottlogic.datahelix.generator.core.generation.fieldvaluesources.NullOnlySource;
import com.scottlogic.datahelix.generator.core.restrictions.linear.Limit;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictionsFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

import static com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictionsFactory.createNumericRestrictions;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class FieldSpecGetFieldValueSourceTests {
    @Test
    public void shouldReturnNullSourceOnlyWithMustBeNullRestrictions() {
        FieldSpec fieldSpecMustBeNull = FieldSpecFactory.nullOnly();

        FieldValueSource<?> sources = fieldSpecMustBeNull.getFieldValueSource();

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithNoRestrictions() {
        FieldSpec fieldSpecWithNoRestrictions = FieldSpecFactory.fromType(FieldType.STRING);

        FieldValueSource<?> sources = fieldSpecWithNoRestrictions.getFieldValueSource();

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithInSetRestrictionsAndNullNotDisallowed() {
        FieldSpec fieldSpecInSetAndNullNotDisallowed = FieldSpecFactory.fromAllowedList(new HashSet<>(Arrays.asList(15, 25)));

        FieldValueSource<?> sources = fieldSpecInSetAndNullNotDisallowed.getFieldValueSource();

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithTypedNumericRestrictionsAndNullNotDisallowed() {
        LinearRestrictions<BigDecimal> numericRestrictions = createNumericRestrictions(
            new Limit<>(new BigDecimal(10), false),
            new Limit<>(new BigDecimal(30), false));
        FieldSpec fieldSpecWithTypedNumericRestrictionsAndNullNotDisallowed = FieldSpecFactory.fromRestriction(numericRestrictions);

        FieldValueSource<?> sources = fieldSpecWithTypedNumericRestrictionsAndNullNotDisallowed.getFieldValueSource();

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithTypedDateTimeRestrictionsAndNullNotDisallowed() {
        LinearRestrictions<OffsetDateTime> dateTimeRestrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            new Limit<>(OffsetDateTime.MIN, false),
            new Limit<>(OffsetDateTime.MAX, false)
        );
        FieldSpec fieldSpecInSetWithTypedDateTimeRestrictionsAndNullNotDisallowed = FieldSpecFactory.fromRestriction(dateTimeRestrictions);

        FieldValueSource<?> sources = fieldSpecInSetWithTypedDateTimeRestrictionsAndNullNotDisallowed.getFieldValueSource();

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithDecimalValues_generatesDecimalValues() {
        FieldSpec fieldSpec = FieldSpecFactory.fromRestriction(
            createNumericRestrictions(
                new Limit<>(new BigDecimal("15.00000000000000000001"), false),
                new Limit<>(new BigDecimal("15.00000000000000000010"), false))
        ).withNotNull();

        final FieldValueSource<?> result = fieldSpec.getFieldValueSource();

        Iterator<?> allValuesIterator = result.generateAllValues().iterator();
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (allValuesIterator.hasNext()) {
            valuesFromResult.add((BigDecimal) allValuesIterator.next());
        }

        final List<BigDecimal> expectedValues = Arrays.asList(
            new BigDecimal("15.00000000000000000002"),
            new BigDecimal("15.00000000000000000003"),
            new BigDecimal("15.00000000000000000004"),
            new BigDecimal("15.00000000000000000005"),
            new BigDecimal("15.00000000000000000006"),
            new BigDecimal("15.00000000000000000007"),
            new BigDecimal("15.00000000000000000008"),
            new BigDecimal("15.00000000000000000009")
            );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNegativeMinAndPositiveMax_generatesExpectedNegativeToPositiveValues() {
        FieldSpec fieldSpec = FieldSpecFactory.fromRestriction(
            createNumericRestrictions(
                new Limit<>(new BigDecimal("-3E-20"), false),
                new Limit<>(new BigDecimal("3E-20"), false))
        ).withNotNull();

        final FieldValueSource<?> result = fieldSpec.getFieldValueSource();

        Iterator<?> allValuesIterator = result.generateAllValues().iterator();
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (allValuesIterator.hasNext()) {
            valuesFromResult.add((BigDecimal) allValuesIterator.next());
        }

        final List<BigDecimal> expectedValues = Arrays.asList(
            new BigDecimal("-2E-20"),
            new BigDecimal("-1E-20"),
            new BigDecimal("0E-20"),
            new BigDecimal("1E-20"),
            new BigDecimal("2E-20")
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    private void AssertLastSourceIsNullOnlySource(FieldValueSource<?> source) {
        if (source instanceof NullOnlySource){
            return;
        }
        NullAppendingValueSource<?> combi = (NullAppendingValueSource<?>) source;

        final int randomValueThatWillYieldNull = 1;
        RandomNumberGenerator rng = new RandomNumberGenerator() {
            @Override
            public int nextInt() {
                return randomValueThatWillYieldNull;
            }

            @Override
            public int nextInt(int bound) {
                return randomValueThatWillYieldNull;
            }

            @Override
            public long nextLong(long lowerInclusive, long upperInclusive) {
                return randomValueThatWillYieldNull;
            }

            @Override
            public double nextDouble(double lowerInclusive, double upperExclusive) {
                return randomValueThatWillYieldNull;
            }

            @Override
            public BigDecimal nextBigDecimal(BigDecimal lowerInclusive, BigDecimal upperExclusive) {
                return BigDecimal.valueOf(randomValueThatWillYieldNull);
            }
        };

        Iterator<?> shouldBeNullValueIterator = combi.generateRandomValues(rng).iterator();
        Assert.assertThat(shouldBeNullValueIterator.hasNext(), is(true));
        Assert.assertThat(shouldBeNullValueIterator.next(), is(nullValue()));
    }
}
