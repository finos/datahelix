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

import com.google.common.collect.Iterators;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.generation.fieldvaluesources.CannedValuesFieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullOnlySource;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.NumericRestrictions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Pattern;

import static com.scottlogic.deg.common.profile.Types.*;
import static com.scottlogic.deg.generator.restrictions.linear.NumericRestrictions.NUMERIC_MAX_LIMIT;
import static com.scottlogic.deg.generator.restrictions.linear.NumericRestrictions.NUMERIC_MIN_LIMIT;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class StandardFieldValueSourceEvaluatorTests {

    @Test
    public void shouldReturnNullSourceOnlyWithMustBeNullRestrictions() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpec fieldSpecMustBeNull = FieldSpec.nullOnlyFromType(STRING);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecMustBeNull);

        Assert.assertThat(sources, hasSize(1));
        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void returnsNullSourceOnlyWithSetRestrictionWithEmptyWhitelist() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpec fieldSpecMustBeNull = FieldSpec.fromType(STRING)
            .withWhitelist((new DistributedSet<>(Collections.emptySet())));

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecMustBeNull);

        Assert.assertThat(sources, hasSize(1));
        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithNoRestrictions() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpec fieldSpecWithNoRestrictions = FieldSpec.fromType(STRING);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecWithNoRestrictions);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithInSetRestrictionsAndNullNotDisallowed() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpec fieldSpecInSetAndNullNotDisallowed = FieldSpec.fromType(NUMERIC)
            .withWhitelist(DistributedSet.uniform(new HashSet<>(Arrays.asList(15, 25))));

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecInSetAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithTypedNumericRestrictionsAndNullNotDisallowed() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        NumericRestrictions numericRestrictions = new NumericRestrictions(
            new Limit<>(new BigDecimal(10), false),
            new Limit<>(new BigDecimal(30), false));
        FieldSpec fieldSpecWithTypedNumericRestrictionsAndNullNotDisallowed = FieldSpec.fromType(NUMERIC)
            .withNumericRestrictions(numericRestrictions);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecWithTypedNumericRestrictionsAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithTypedStringRestrictionsAndNullNotDisallowed() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        StringRestrictions stringRestrictions = matchesRegex("/[ab]{2}/", false);
        FieldSpec fieldSpecInSetWithTypedStringRestrictionsAndNullNotDisallowedd = FieldSpec.fromType(NUMERIC)
            .withStringRestrictions(stringRestrictions);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecInSetWithTypedStringRestrictionsAndNullNotDisallowedd);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithTypedDateTimeRestrictionsAndNullNotDisallowed() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        DateTimeRestrictions datetimeRestrictions = new DateTimeRestrictions(
            new Limit<>(OffsetDateTime.MIN, false),
            new Limit<>(OffsetDateTime.MAX, false)
        );
        FieldSpec fieldSpecInSetWithTypedDateTimeRestrictionsAndNullNotDisallowed = FieldSpec.fromType(DATETIME)
            .withDateTimeRestrictions(datetimeRestrictions);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecInSetWithTypedDateTimeRestrictionsAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithValueTooLargeForInteger_generatesExpectedValues() {
        FieldSpec fieldSpec = FieldSpec.fromType(NUMERIC).withNumericRestrictions(
            new NumericRestrictions(new Limit<>(new BigDecimal(0), false),
                new Limit<>(new BigDecimal("1E+18"), false))
        ).withNotNull(

        );
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        Assert.assertEquals(1, result.size());
        Iterator interestingValuesIterator = result.get(0).generateInterestingValues().iterator();
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
        FieldSpec fieldSpec = FieldSpec.fromType(NUMERIC).withNumericRestrictions(
            new NumericRestrictions(new Limit<>(new BigDecimal("15.00000000000000000001"), false),
            new Limit<>(new BigDecimal("15.00000000000000000010"), false))
        ).withNotNull();
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        Assert.assertEquals(1, result.size());
        Iterator allValuesIterator = result.get(0).generateAllValues().iterator();
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
        NumericRestrictions restrictions = new NumericRestrictions(
            new Limit<>(new BigDecimal("15"), false),
            new Limit<>(new BigDecimal("16"), false),
            2);
        FieldSpec fieldSpec = FieldSpec.fromType(NUMERIC).withNumericRestrictions(
            restrictions
        ).withNotNull();
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        Assert.assertEquals(1, result.size());
        Iterator interestingValuesIterator = result.get(0).generateInterestingValues().iterator();
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
        FieldSpec fieldSpec = FieldSpec.fromType(NUMERIC).withNumericRestrictions(
            new NumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT)
        ).withNotNull();
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        Assert.assertEquals(1, result.size());
        Iterator interestingValuesIterator = result.get(0).generateInterestingValues().iterator();
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (interestingValuesIterator.hasNext()) {
            valuesFromResult.add(new BigDecimal(interestingValuesIterator.next().toString()));
        }

        final List<BigDecimal> expectedValues = Arrays.asList(
            new BigDecimal("-1E+20"),
            new BigDecimal("0E-20"),
            new BigDecimal("1E+20")
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionWithNullMinAndMaxIsDecimal_generatesDecimalValues() {
        FieldSpec fieldSpec = FieldSpec.fromType(NUMERIC).withNumericRestrictions(
            new NumericRestrictions(NUMERIC_MIN_LIMIT, new Limit<>(new BigDecimal("150.5"), false))
        ).withNotNull();
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        Assert.assertEquals(1, result.size());
        Iterator interestingValuesIterator = result.get(0).generateInterestingValues().iterator();
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (interestingValuesIterator.hasNext()) {
            valuesFromResult.add((BigDecimal) interestingValuesIterator.next());
        }

        Assert.assertTrue(valuesFromResult.size() > 0);
        Assert.assertTrue(valuesFromResult.stream().allMatch(Objects::nonNull));
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNegativeMinAndPositiveMax_generatesExpectedNegativeToPositiveValues() {
        FieldSpec fieldSpec = FieldSpec.fromType(NUMERIC).withNumericRestrictions(
            new NumericRestrictions(
                new Limit<>(new BigDecimal("-3E-20"), false),
                new Limit<>(new BigDecimal("3E-20"), false))
        ).withNotNull();
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        Assert.assertEquals(1, result.size());
        Iterator allValuesIterator = result.get(0).generateAllValues().iterator();
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

    private void AssertLastSourceIsNullOnlySource(List<FieldValueSource> sources) {
        int lastSourceIndex = sources.size() - 1;
        Assert.assertTrue(sources.get(lastSourceIndex) instanceof NullOnlySource);
        Assert.assertNull(Iterators.get(sources.get(lastSourceIndex).generateAllValues().iterator(), 0));
    }

    private static StringRestrictions matchesRegex(String regex, boolean negate){
        return new StringRestrictionsFactory().forStringMatching(Pattern.compile(regex), negate);
    }
}
