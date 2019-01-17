package com.scottlogic.deg.generator.generation;

import com.google.common.collect.Iterators;
import com.scottlogic.deg.generator.constraints.StringConstraintsCollection;
import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import com.scottlogic.deg.generator.generation.field_value_sources.CannedValuesFieldValueSource;
import com.scottlogic.deg.generator.generation.field_value_sources.FieldValueSource;
import com.scottlogic.deg.generator.restrictions.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    private void AssertLastSourceIsNullOnlySource(List<FieldValueSource> sources) {
        int lastSourceIndex = sources.size() - 1;
        Assert.assertTrue(sources.get(lastSourceIndex) instanceof CannedValuesFieldValueSource);
        Assert.assertTrue(sources.get(lastSourceIndex).getValueCount() == 1);
        Assert.assertTrue(Iterators.get(sources.get(lastSourceIndex).generateAllValues().iterator(), 0) == null);
    }
}
