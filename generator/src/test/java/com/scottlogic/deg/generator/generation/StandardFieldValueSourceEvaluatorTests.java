package com.scottlogic.deg.generator.generation;

import com.google.common.collect.Iterators;
import com.scottlogic.deg.generator.constraints.StringConstraintsCollection;
import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.CannedValuesFieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.restrictions.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasItems;

public class StandardFieldValueSourceEvaluatorTests {

    @Test
    public void shouldReturnNullSourceOnlyWithMustBeNullRestrictions() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpecSource fieldSpecSource = FieldSpecSource.Empty;
        NullRestrictions nullRestrictions = new NullRestrictions(Nullness.MUST_BE_NULL);
        FieldSpec fieldSpecMustBeNull = FieldSpec.Empty
            .withNullRestrictions(nullRestrictions, fieldSpecSource);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecMustBeNull);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithNoRestrictions() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpec fieldSpecWithNoRestrictions = FieldSpec.Empty;

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecWithNoRestrictions);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithInSetRestrictionsAndNullNotDisallowed() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpecSource fieldSpecSource = FieldSpecSource.Empty;
        SetRestrictions setRestrictions = new SetRestrictions(new HashSet<>(Arrays.asList(15, 25)), null);
        FieldSpec fieldSpecInSetAndNullNotDisallowed = FieldSpec.Empty
            .withSetRestrictions(setRestrictions, fieldSpecSource);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecInSetAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithTypedNumericRestrictionsAndNullNotDisallowed() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpecSource fieldSpecSource = FieldSpecSource.Empty;
        NumericRestrictions numericRestrictions = new NumericRestrictions() {{
            min = new NumericLimit<>(new BigDecimal(10), false);
            max = new NumericLimit<>(new BigDecimal(30), false);
        }};
        TypeRestrictions typeRestrictions = new DataTypeRestrictions(Collections.singletonList(
            IsOfTypeConstraint.Types.NUMERIC
        ));
        FieldSpec fieldSpecWithTypedNumericRestrictionsAndNullNotDisallowed = FieldSpec.Empty
            .withNumericRestrictions(numericRestrictions, fieldSpecSource)
            .withTypeRestrictions(typeRestrictions, fieldSpecSource);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecWithTypedNumericRestrictionsAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithTypedStringRestrictionsAndNullNotDisallowed() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpecSource fieldSpecSource = FieldSpecSource.Empty;
        StringRestrictions stringRestrictions =
            new StringRestrictions(new StringConstraintsCollection(Collections.emptySet())) {{
                stringGenerator = new RegexStringGenerator("/[ab]{2}/", true);
            }};
        TypeRestrictions typeRestrictions = new DataTypeRestrictions(Collections.singletonList(
            IsOfTypeConstraint.Types.STRING
        ));
        FieldSpec fieldSpecInSetWithTypedStringRestrictionsAndNullNotDisallowedd = FieldSpec.Empty
            .withStringRestrictions(stringRestrictions, fieldSpecSource)
            .withTypeRestrictions(typeRestrictions, fieldSpecSource);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecInSetWithTypedStringRestrictionsAndNullNotDisallowedd);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithTypedTemporalRestrictionsAndNullNotDisallowed() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpecSource fieldSpecSource = FieldSpecSource.Empty;
        DateTimeRestrictions temporalRestrictions = new DateTimeRestrictions() {{
            min = new DateTimeLimit(LocalDateTime.MIN, false);
            max = new DateTimeLimit(LocalDateTime.MAX, false);
        }};
        TypeRestrictions typeRestrictions = new DataTypeRestrictions(Collections.singletonList(
            IsOfTypeConstraint.Types.TEMPORAL
        ));
        FieldSpec fieldSpecInSetWithTypedTemporalRestrictionsAndNullNotDisallowed = FieldSpec.Empty
            .withDateTimeRestrictions(temporalRestrictions, fieldSpecSource)
            .withTypeRestrictions(typeRestrictions, fieldSpecSource);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecInSetWithTypedTemporalRestrictionsAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithMustContainNullRestrictionsAndNullNotDisallowed() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpecSource fieldSpecSource = FieldSpecSource.Empty;

        NullRestrictions nullRestrictions = new NullRestrictions(Nullness.MUST_BE_NULL);
        FieldSpec fieldSpecMustBeNull = FieldSpec.Empty
            .withNullRestrictions(nullRestrictions, fieldSpecSource);

        MustContainRestriction mustContainRestriction = new MustContainRestriction(new HashSet<>(Collections.singletonList(fieldSpecMustBeNull)));

        FieldSpec fieldSpecWithMustContainNullRestrictionsAndNullNotDisallowed = FieldSpec.Empty
            .withMustContainRestriction(mustContainRestriction);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecWithMustContainNullRestrictionsAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLastWithMustContainNullAndNumericRestrictionsAndNullNotDisallowed() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpecSource fieldSpecSource = FieldSpecSource.Empty;

        NullRestrictions nullRestrictions = new NullRestrictions(Nullness.MUST_BE_NULL);
        NumericRestrictions numericRestrictions = new NumericRestrictions() {{
            min = new NumericLimit<>(new BigDecimal(10), false);
            max = new NumericLimit<>(new BigDecimal(30), false);
        }};
        FieldSpec fieldSpecMustBeNull = FieldSpec.Empty
            .withNullRestrictions(nullRestrictions, fieldSpecSource)
            .withNumericRestrictions(numericRestrictions, fieldSpecSource);

        MustContainRestriction mustContainRestriction = new MustContainRestriction(new HashSet<>(Collections.singletonList(fieldSpecMustBeNull)));

        FieldSpec fieldSpecWithMustContainNullAndNumericRestrictionsAndNullNotDisallowed = FieldSpec.Empty
            .withMustContainRestriction(mustContainRestriction);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecWithMustContainNullAndNumericRestrictionsAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithIntegerValues_generatesExpectedIntegerValues() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions() {{
                min = new NumericLimit<>(new BigDecimal(0), false);
                max = new NumericLimit<>(new BigDecimal(10), false);
            }},
            FieldSpecSource.Empty
        ).withTypeRestrictions(
            new DataTypeRestrictions(
                Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
            ),
            FieldSpecSource.Empty
        ).withNullRestrictions(
            new NullRestrictions(Nullness.MUST_NOT_BE_NULL),
            FieldSpecSource.Empty
        );
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        Assert.assertEquals(1, result.size());
        Iterator allValuesIterator = result.get(0).generateAllValues().iterator();
        List<Integer> valuesFromResult = new ArrayList<>();
        while (allValuesIterator.hasNext()) {
            valuesFromResult.add((Integer) allValuesIterator.next());
        }

        final List<Integer> expectedValues = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithValueTooLargeForInteger_generatesExpectedValues() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions() {{
                min = new NumericLimit<>(new BigDecimal(0), false);
                max = new NumericLimit<>(new BigDecimal("1E+18"), false);
            }},
            FieldSpecSource.Empty
        ).withTypeRestrictions(
            new DataTypeRestrictions(
                Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
            ),
            FieldSpecSource.Empty
        ).withNullRestrictions(
            new NullRestrictions(Nullness.MUST_NOT_BE_NULL),
            FieldSpecSource.Empty
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
            new BigDecimal("1"),
            new BigDecimal("2"),
            new BigDecimal("999999999999999998"),
            new BigDecimal("999999999999999999")
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithDecimalValues_generatesDecimalValues() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions() {{
                min = new NumericLimit<>(new BigDecimal(15.5), false);
                max = new NumericLimit<>(new BigDecimal(16.5), false);
            }},
            FieldSpecSource.Empty
        ).withTypeRestrictions(
            new DataTypeRestrictions(
                Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
            ),
            FieldSpecSource.Empty
        ).withNullRestrictions(
            new NullRestrictions(Nullness.MUST_NOT_BE_NULL),
            FieldSpecSource.Empty
        );
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        Assert.assertEquals(1, result.size());
        Iterator allValuesIterator = result.get(0).generateAllValues().iterator();
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (allValuesIterator.hasNext()) {
            valuesFromResult.add((BigDecimal) allValuesIterator.next());
        }

        final List<BigDecimal> expectedValues = Arrays.asList(
            new BigDecimal("15.6"),
            new BigDecimal("15.7"),
            new BigDecimal("15.8"),
            new BigDecimal("15.9"),
            new BigDecimal("16.0"),
            new BigDecimal("16.1"),
            new BigDecimal("16.2"),
            new BigDecimal("16.3"),
            new BigDecimal("16.4")
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWhereMinAndMaxHaveDifferingScales_generatesDecimalValuesWithHighestScale() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions() {{
                min = new NumericLimit<>(new BigDecimal("15.92"), false);
                max = new NumericLimit<>(new BigDecimal("16.1"), false);
            }},
            FieldSpecSource.Empty
        ).withTypeRestrictions(
            new DataTypeRestrictions(
                Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
            ),
            FieldSpecSource.Empty
        ).withNullRestrictions(
            new NullRestrictions(Nullness.MUST_NOT_BE_NULL),
            FieldSpecSource.Empty
        );
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        Assert.assertEquals(1, result.size());
        Iterator allValuesIterator = result.get(0).generateAllValues().iterator();
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (allValuesIterator.hasNext()) {
            valuesFromResult.add((BigDecimal) allValuesIterator.next());
        }

        final List<BigDecimal> expectedValues = Arrays.asList(
            new BigDecimal("15.93"),
            new BigDecimal("15.94"),
            new BigDecimal("15.95"),
            new BigDecimal("15.96"),
            new BigDecimal("15.97"),
            new BigDecimal("15.98"),
            new BigDecimal("15.99"),
            new BigDecimal("16.00"),
            new BigDecimal("16.01"),
            new BigDecimal("16.02"),
            new BigDecimal("16.03"),
            new BigDecimal("16.04"),
            new BigDecimal("16.05"),
            new BigDecimal("16.06"),
            new BigDecimal("16.07"),
            new BigDecimal("16.08"),
            new BigDecimal("16.09")
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithNoScaleAndGranularityHasRestrictionOfTwo_generatesValuesWithTwoDecimalPlaces() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions() {{
                min = new NumericLimit<>(new BigDecimal("15"), false);
                max = new NumericLimit<>(new BigDecimal("16"), false);
            }},
            FieldSpecSource.Empty
        ).withTypeRestrictions(
            new DataTypeRestrictions(
                Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
            ),
            FieldSpecSource.Empty
        ).withNullRestrictions(
            new NullRestrictions(Nullness.MUST_NOT_BE_NULL),
            FieldSpecSource.Empty
        ).withGranularityRestrictions(
            new GranularityRestrictions(
                new ParsedGranularity(new BigDecimal(new BigInteger("0"), 2, new MathContext(1)))
            ),
            FieldSpecSource.Empty
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
            new BigDecimal("15.01"),
            new BigDecimal("15.02"),
            new BigDecimal("15.98"),
            new BigDecimal("15.99")
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithMinAndMaxNull_generatesBoundaryIntegerValues() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            FieldSpecSource.Empty
        ).withTypeRestrictions(
            new DataTypeRestrictions(
                Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
            ),
            FieldSpecSource.Empty
        ).withNullRestrictions(
            new NullRestrictions(Nullness.MUST_NOT_BE_NULL),
            FieldSpecSource.Empty
        );
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        Assert.assertEquals(1, result.size());
        Iterator interestingValuesIterator = result.get(0).generateInterestingValues().iterator();
        List<Integer> valuesFromResult = new ArrayList<>();
        while (interestingValuesIterator.hasNext()) {
            valuesFromResult.add((Integer) interestingValuesIterator.next());
        }

        final List<Integer> expectedValues = Arrays.asList(
            -2147483648,
            0,
            2147483646
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionWithNullMinAndMaxIsDecimal_generatesDecimalValues() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions() {{
                max = new NumericLimit<>(new BigDecimal("150.5"), false);
            }},
            FieldSpecSource.Empty
        ).withTypeRestrictions(
            new DataTypeRestrictions(
                Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
            ),
            FieldSpecSource.Empty
        ).withNullRestrictions(
            new NullRestrictions(Nullness.MUST_NOT_BE_NULL),
            FieldSpecSource.Empty
        );
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
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions() {{
                min = new NumericLimit<>(new BigDecimal("-0.3"), false);
                max = new NumericLimit<>(new BigDecimal("0.3"), false);
            }},
            FieldSpecSource.Empty
        ).withTypeRestrictions(
            new DataTypeRestrictions(
                Collections.singletonList(IsOfTypeConstraint.Types.NUMERIC)
            ),
            FieldSpecSource.Empty
        ).withNullRestrictions(
            new NullRestrictions(Nullness.MUST_NOT_BE_NULL),
            FieldSpecSource.Empty
        );
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        Assert.assertEquals(1, result.size());
        Iterator allValuesIterator = result.get(0).generateAllValues().iterator();
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (allValuesIterator.hasNext()) {
            valuesFromResult.add((BigDecimal) allValuesIterator.next());
        }

        final List<BigDecimal> expectedValues = Arrays.asList(
            new BigDecimal("-0.2"),
            new BigDecimal("-0.1"),
            new BigDecimal("0.0"),
            new BigDecimal("0.1"),
            new BigDecimal("0.2")
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsSetAndMustContainRestrictionsWithSameValue_shouldEmitValueOnce(){
        FieldSpec fieldSpec = FieldSpec.Empty
            .withSetRestrictions(
                SetRestrictions.fromWhitelist(new HashSet<>(Arrays.asList("foo", "bar"))),
                FieldSpecSource.Empty)
            .withMustContainRestriction(
                new MustContainRestriction(
                    Collections.singleton(FieldSpec.Empty
                        .withSetRestrictions(
                            SetRestrictions.fromWhitelist(Collections.singleton("foo")),
                            FieldSpecSource.Empty))
                )
            );

        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();

        final List<FieldValueSource> result = evaluator.getFieldValueSources(fieldSpec);

        List<String> values = new ArrayList<>();
        result.forEach(valueSource -> valueSource.generateAllValues().forEach(value -> values.add((String)value)));

        Assert.assertThat(values, hasItems("foo", "bar"));
    }

    private void AssertLastSourceIsNullOnlySource(List<FieldValueSource> sources) {
        int lastSourceIndex = sources.size() - 1;
        Assert.assertTrue(sources.get(lastSourceIndex) instanceof CannedValuesFieldValueSource);
        Assert.assertTrue(sources.get(lastSourceIndex).getValueCount() == 1);
        Assert.assertTrue(Iterators.get(sources.get(lastSourceIndex).generateAllValues().iterator(), 0) == null);
    }
}
