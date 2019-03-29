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
import java.time.OffsetDateTime;
import java.util.*;

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
    public void shouldReturnNullSourceLastWithTypedDateTimeRestrictionsAndNullNotDisallowed() {
        StandardFieldValueSourceEvaluator evaluator = new StandardFieldValueSourceEvaluator();
        FieldSpecSource fieldSpecSource = FieldSpecSource.Empty;
        DateTimeRestrictions datetimeRestrictions = new DateTimeRestrictions() {{
            min = new DateTimeLimit(OffsetDateTime.MIN, false);
            max = new DateTimeLimit(OffsetDateTime.MAX, false);
        }};
        TypeRestrictions typeRestrictions = new DataTypeRestrictions(Collections.singletonList(
            IsOfTypeConstraint.Types.DATETIME
        ));
        FieldSpec fieldSpecInSetWithTypedDateTimeRestrictionsAndNullNotDisallowed = FieldSpec.Empty
            .withDateTimeRestrictions(datetimeRestrictions, fieldSpecSource)
            .withTypeRestrictions(typeRestrictions, fieldSpecSource);

        List<FieldValueSource> sources = evaluator.getFieldValueSources(fieldSpecInSetWithTypedDateTimeRestrictionsAndNullNotDisallowed);

        AssertLastSourceIsNullOnlySource(sources);
    }

    @Test
    public void shouldReturnNullSourceLast_WithMustContainNullRestrictionsAndNullAllowed() {
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
            new BigDecimal("1E-20"),
            new BigDecimal("2E-20"),
            new BigDecimal("999999999999999999.99999999999999999998"),
            new BigDecimal("999999999999999999.99999999999999999999")
        );
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithDecimalValues_generatesDecimalValues() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions() {{
                min = new NumericLimit<>(new BigDecimal("15.00000000000000000001"), false);
                max = new NumericLimit<>(new BigDecimal("15.00000000000000000010"), false);
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
    void getFieldValueSources_fieldSpecContainsNumericRestrictionsWithMinAndMaxNull_generatesBoundaryValues() {
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
        List<BigDecimal> valuesFromResult = new ArrayList<>();
        while (interestingValuesIterator.hasNext()) {
            valuesFromResult.add(new BigDecimal(interestingValuesIterator.next().toString()));
        }

        final List<BigDecimal> expectedValues = Arrays.asList(
            new BigDecimal("-100000000000000000000.00000000000000000000"),
            new BigDecimal("-99999999999999999999.99999999999999999999"),
            new BigDecimal("0E-20"),
            new BigDecimal("99999999999999999999.99999999999999999999"),
            new BigDecimal("100000000000000000000.00000000000000000000")
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
    void getFieldValueSources_fieldSpecContainsNumericRestrictionWithHighGranularity_generates20DecimalGranularity() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions() {{
                min = new NumericLimit<>(new BigDecimal("1.1E-30"), false);
                max = new NumericLimit<>(new BigDecimal("1.5E-20"), false);
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

        List<BigDecimal> expectedValues = Collections.singletonList(new BigDecimal("1E-20"));
        Assert.assertEquals(expectedValues, valuesFromResult);
    }

    @Test
    void getFieldValueSources_fieldSpecContainsNegativeMinAndPositiveMax_generatesExpectedNegativeToPositiveValues() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions() {{
                min = new NumericLimit<>(new BigDecimal("-3E-20"), false);
                max = new NumericLimit<>(new BigDecimal("3E-20"), false);
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
        Assert.assertTrue(sources.get(lastSourceIndex) instanceof CannedValuesFieldValueSource);
        Assert.assertTrue(sources.get(lastSourceIndex).getValueCount() == 1);
        Assert.assertTrue(Iterators.get(sources.get(lastSourceIndex).generateAllValues().iterator(), 0) == null);
    }
}
