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

import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularity;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullAppendingValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullOnlySource;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.restrictions.linear.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.scottlogic.deg.common.profile.FieldType.*;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createNumericRestrictions;
import static com.scottlogic.deg.generator.utils.Defaults.NUMERIC_MAX_LIMIT;
import static com.scottlogic.deg.generator.utils.Defaults.NUMERIC_MIN_LIMIT;

public class FieldValueSourceEvaluatorTests {

    @Test
    public void shouldReturnNullSourceOnlyWithMustBeNullRestrictions() {
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();
        FieldSpec fieldSpecMustBeNull = FieldSpecFactory.nullOnly();

        FieldValueSource sources = evaluator.getFieldValueSources(STRING, fieldSpecMustBeNull);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void returnsNullSourceOnlyWithSetRestrictionWithEmptyWhitelist() {
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();
        FieldSpec fieldSpecMustBeNull = FieldSpecFactory.fromList((new DistributedList<>(Collections.emptyList())));

        FieldValueSource sources = evaluator.getFieldValueSources(STRING, fieldSpecMustBeNull);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithNoRestrictions() {
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();
        FieldSpec fieldSpecWithNoRestrictions = FieldSpecFactory.fromType(STRING);

        FieldValueSource sources = evaluator.getFieldValueSources(STRING, fieldSpecWithNoRestrictions);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithInSetRestrictionsAndNullNotDisallowed() {
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();
        FieldSpec fieldSpecInSetAndNullNotDisallowed = FieldSpecFactory.fromList(DistributedList.uniform(new HashSet<>(Arrays.asList(15, 25))));

        FieldValueSource sources = evaluator.getFieldValueSources(NUMERIC, fieldSpecInSetAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithTypedNumericRestrictionsAndNullNotDisallowed() {
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();
        LinearRestrictions<BigDecimal> numericRestrictions = createNumericRestrictions(
            new Limit<>(new BigDecimal(10), false),
            new Limit<>(new BigDecimal(30), false));
        FieldSpec fieldSpecWithTypedNumericRestrictionsAndNullNotDisallowed = FieldSpecFactory.fromRestriction(numericRestrictions);

        FieldValueSource sources = evaluator.getFieldValueSources(NUMERIC, fieldSpecWithTypedNumericRestrictionsAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithTypedDateTimeRestrictionsAndNullNotDisallowed() {
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();
        LinearRestrictions<OffsetDateTime> dateTimeRestrictions = LinearRestrictionsFactory.createDateTimeRestrictions(
            new Limit<>(OffsetDateTime.MIN, false),
            new Limit<>(OffsetDateTime.MAX, false)
        );
        FieldSpec fieldSpecInSetWithTypedDateTimeRestrictionsAndNullNotDisallowed = FieldSpecFactory.fromRestriction(dateTimeRestrictions);

        FieldValueSource sources = evaluator.getFieldValueSources(DATETIME, fieldSpecInSetWithTypedDateTimeRestrictionsAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithValueTooLargeForInteger_generatesExpectedValues() {
        FieldSpec fieldSpec = FieldSpecFactory.fromRestriction(
            createNumericRestrictions(new Limit<>(new BigDecimal(0), false),
                new Limit<>(new BigDecimal("1E+18"), false))
        ).withNotNull(

        );
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();

        final FieldValueSource result = evaluator.getFieldValueSources(NUMERIC, fieldSpec);

        Iterator interestingValuesIterator = result.generateInterestingValues().iterator();
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (interestingValuesIterator.hasNext()) {
            valuesFromResult.add((BigDecimal) interestingValuesIterator.next());
        }

        final List<BigDecimal> expectedValues = Arrays.asList(
            new BigDecimal("1E-20"),
            new BigDecimal("999999999999999999.99999999999999999999")
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithDecimalValues_generatesDecimalValues() {
        FieldSpec fieldSpec = FieldSpecFactory.fromRestriction(
            createNumericRestrictions(
                new Limit<>(new BigDecimal("15.00000000000000000001"), false),
                new Limit<>(new BigDecimal("15.00000000000000000010"), false))
        ).withNotNull();
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();

        final FieldValueSource result = evaluator.getFieldValueSources(NUMERIC, fieldSpec);

        Iterator allValuesIterator = result.generateAllValues().iterator();
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
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithNoScaleAndGranularityHasRestrictionOfTwo_generatesValuesWithTwoDecimalPlaces() {
        LinearRestrictions<BigDecimal> restrictions = createNumericRestrictions(
            new Limit<>(new BigDecimal("15"), false),
            new Limit<>(new BigDecimal("16"), false),
            new NumericGranularity(2));
        FieldSpec fieldSpec = FieldSpecFactory.fromRestriction(restrictions).withNotNull();
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();

        final FieldValueSource result = evaluator.getFieldValueSources(NUMERIC, fieldSpec);

        Iterator interestingValuesIterator = result.generateInterestingValues().iterator();
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (interestingValuesIterator.hasNext()) {
            valuesFromResult.add((BigDecimal) interestingValuesIterator.next());
        }

        final List<BigDecimal> expectedValues = Arrays.asList(
            new BigDecimal("15.01"),
            new BigDecimal("15.99")
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithMinAndMaxNull_generatesBoundaryValues() {
        FieldSpec fieldSpec = FieldSpecFactory.fromRestriction(
            LinearRestrictionsFactory.createNumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT)
        ).withNotNull();
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();

        final FieldValueSource result = evaluator.getFieldValueSources(NUMERIC, fieldSpec);

        Iterator interestingValuesIterator = result.generateInterestingValues().iterator();
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (interestingValuesIterator.hasNext()) {
            valuesFromResult.add(new BigDecimal(interestingValuesIterator.next().toString()));
        }

        final List<BigDecimal> expectedValues = Arrays.asList(
            new BigDecimal("-1E+20"),
            new BigDecimal("1E+20")
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionWithNullMinAndMaxIsDecimal_generatesDecimalValues() {
        FieldSpec fieldSpec = FieldSpecFactory.fromRestriction(
            createNumericRestrictions(NUMERIC_MIN_LIMIT, new Limit<>(new BigDecimal("150.5"), false))
        ).withNotNull();
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();

        final FieldValueSource result = evaluator.getFieldValueSources(NUMERIC, fieldSpec);

        Iterator interestingValuesIterator = result.generateInterestingValues().iterator();
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (interestingValuesIterator.hasNext()) {
            valuesFromResult.add((BigDecimal) interestingValuesIterator.next());
        }

        Assert.assertTrue(valuesFromResult.size() > 0);
        Assert.assertTrue(valuesFromResult.stream().allMatch(Objects::nonNull));
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNegativeMinAndPositiveMax_generatesExpectedNegativeToPositiveValues() {
        FieldSpec fieldSpec = FieldSpecFactory.fromRestriction(
            createNumericRestrictions(
                new Limit<>(new BigDecimal("-3E-20"), false),
                new Limit<>(new BigDecimal("3E-20"), false))
        ).withNotNull();
        FieldValueSourceEvaluator evaluator = new FieldValueSourceEvaluator();

        final FieldValueSource result = evaluator.getFieldValueSources(NUMERIC, fieldSpec);

        Iterator allValuesIterator = result.generateAllValues().iterator();
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

    private void AssertLastSourceIsNullOnlySource(FieldValueSource source) {
        if (source instanceof NullOnlySource){
            return;
        }
        NullAppendingValueSource combi = (NullAppendingValueSource) source;
        List<Object> sources = (List<Object>) combi.generateInterestingValues().collect(Collectors.toList());
        int lastSourceIndex = sources.size() - 1;
        Assert.assertTrue(sources.get(sources.size()-1) == null);
    }

    private static StringRestrictions matchesRegex(String regex, boolean negate){
        return new StringRestrictionsFactory().forStringMatching(Pattern.compile(regex), negate);
    }
}
