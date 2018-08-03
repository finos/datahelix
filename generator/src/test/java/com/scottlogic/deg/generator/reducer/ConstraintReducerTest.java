package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.generator.restrictions.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

public class ConstraintReducerTest {
    @Test
    public void shouldProduceCorrectFieldSpecsForExample() {
        // ARRANGE
        final Field quantityField = new Field("quantity");
        final Field countryField = new Field("country");
        final Field cityField = new Field("city");

        ProfileFields fieldList = new ProfileFields(
            Arrays.asList(quantityField, countryField, cityField));

        final Set<Object> countryAmong = new HashSet<>(Arrays.asList("UK", "US"));

        final List<IConstraint> constraints = Arrays.asList(
                new IsGreaterThanConstantConstraint(quantityField, 0),
                new NotConstraint(new IsGreaterThanConstantConstraint(quantityField, 5)),
                new IsInSetConstraint(countryField, countryAmong),
                new IsOfTypeConstraint(cityField, IsOfTypeConstraint.Types.String));

        // ACT
        final RowSpec reducedConstraints = new ConstraintReducer().reduceConstraintsToRowSpec(
            fieldList,
            constraints);

        // ASSERT
        FieldSpec quantityFieldSpec = reducedConstraints.getSpecForField(quantityField);
        Assert.assertNull(quantityFieldSpec.getSetRestrictions());
        Assert.assertNull(quantityFieldSpec.getStringRestrictions());
        Assert.assertNull(quantityFieldSpec.getNullRestrictions());
        Assert.assertNull(quantityFieldSpec.getTypeRestrictions());
        Assert.assertNotNull(quantityFieldSpec.getNumericRestrictions());
        Assert.assertEquals(BigDecimal.ZERO, quantityFieldSpec.getNumericRestrictions().min.getLimit());
        Assert.assertFalse(quantityFieldSpec.getNumericRestrictions().min.isInclusive());
        Assert.assertEquals(BigDecimal.valueOf(5), quantityFieldSpec.getNumericRestrictions().max.getLimit());
        Assert.assertTrue(quantityFieldSpec.getNumericRestrictions().max.isInclusive());

        FieldSpec countryFieldSpec = reducedConstraints.getSpecForField(countryField);
        Assert.assertNull(countryFieldSpec.getStringRestrictions());
        Assert.assertNull(countryFieldSpec.getNullRestrictions());
        Assert.assertNull(countryFieldSpec.getTypeRestrictions());
        Assert.assertNull(countryFieldSpec.getNumericRestrictions());
        Assert.assertNotNull(countryFieldSpec.getSetRestrictions());
        Assert.assertNull(countryFieldSpec.getSetRestrictions().blacklist);
        Assert.assertNotNull(countryFieldSpec.getSetRestrictions().whitelist);
        Assert.assertEquals(2, countryFieldSpec.getSetRestrictions().whitelist.size());
        Assert.assertTrue(countryFieldSpec.getSetRestrictions().whitelist.contains("UK"));
        Assert.assertTrue(countryFieldSpec.getSetRestrictions().whitelist.contains("US"));

        FieldSpec cityFieldSpec = reducedConstraints.getSpecForField(cityField);
        Assert.assertNull(cityFieldSpec.getSetRestrictions());
        Assert.assertNull(cityFieldSpec.getStringRestrictions());
        Assert.assertNull(cityFieldSpec.getNullRestrictions());
        Assert.assertNull(cityFieldSpec.getNumericRestrictions());
        Assert.assertNotNull(cityFieldSpec.getTypeRestrictions());
        Assert.assertEquals(IsOfTypeConstraint.Types.String, cityFieldSpec.getTypeRestrictions().type);
    }

    @Test
    void shouldReduceIsGreaterThanConstantConstraint() {
        final Field field = new Field("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsGreaterThanConstantConstraint(field, 5));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNotNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions().max);
        Assert.assertNotNull(outputSpec.getNumericRestrictions().min);
        Assert.assertEquals(BigDecimal.valueOf(5), outputSpec.getNumericRestrictions().min.getLimit());
        Assert.assertFalse(outputSpec.getNumericRestrictions().min.isInclusive());
    }

    @Test
    void shouldReduceNegatedIsGreaterThanConstantConstraint() {
        final Field field = new Field("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(new NotConstraint(
                new IsGreaterThanConstantConstraint(field, 5)));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNotNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions().min);
        Assert.assertNotNull(outputSpec.getNumericRestrictions().max);
        Assert.assertEquals(BigDecimal.valueOf(5), outputSpec.getNumericRestrictions().max.getLimit());
        Assert.assertTrue(outputSpec.getNumericRestrictions().max.isInclusive());
    }

    @Test
    void shouldReduceIsGreaterThanOrEqualToConstantConstraint() {
        final Field field = new Field("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsGreaterThanOrEqualToConstantConstraint(field, 5));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNotNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions().max);
        Assert.assertNotNull(outputSpec.getNumericRestrictions().min);
        Assert.assertEquals(BigDecimal.valueOf(5), outputSpec.getNumericRestrictions().min.getLimit());
        Assert.assertTrue(outputSpec.getNumericRestrictions().min.isInclusive());
    }

    @Test
    void shouldReduceNegatedIsGreaterThanOrEqualToConstantConstraint() {
        final Field field = new Field("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(new NotConstraint(
                new IsGreaterThanOrEqualToConstantConstraint(field, 5)));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNotNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions().min);
        Assert.assertNotNull(outputSpec.getNumericRestrictions().max);
        Assert.assertEquals(BigDecimal.valueOf(5), outputSpec.getNumericRestrictions().max.getLimit());
        Assert.assertFalse(outputSpec.getNumericRestrictions().max.isInclusive());
    }

    @Test
    void shouldReduceIsLessThanConstantConstraint() {
        final Field field = new Field("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsLessThanConstantConstraint(field, 5));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions().min);
        Assert.assertNotNull(outputSpec.getNumericRestrictions().max);
        Assert.assertEquals(BigDecimal.valueOf(5), outputSpec.getNumericRestrictions().max.getLimit());
        Assert.assertFalse(outputSpec.getNumericRestrictions().max.isInclusive());
    }

    @Test
    void shouldReduceNegatedIsLessThanConstantConstraint() {
        final Field field = new Field("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(new NotConstraint(
                new IsLessThanConstantConstraint(field, 5)));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNotNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions().max);
        Assert.assertNotNull(outputSpec.getNumericRestrictions().min);
        Assert.assertEquals(BigDecimal.valueOf(5), outputSpec.getNumericRestrictions().min.getLimit());
        Assert.assertTrue(outputSpec.getNumericRestrictions().min.isInclusive());
    }

    @Test
    void shouldReduceIsLessThanOrEqualToConstantConstraint() {
        final Field field = new Field("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsLessThanOrEqualToConstantConstraint(field, 5));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions().min);
        Assert.assertNotNull(outputSpec.getNumericRestrictions().max);
        Assert.assertEquals(BigDecimal.valueOf(5), outputSpec.getNumericRestrictions().max.getLimit());
        Assert.assertTrue(outputSpec.getNumericRestrictions().max.isInclusive());
    }

    @Test
    void shouldReduceNegatedIsLessThanOrEqualToConstantConstraint() {
        final Field field = new Field("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(new NotConstraint(
                new IsLessThanOrEqualToConstantConstraint(field, 5)));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions().max);
        Assert.assertNotNull(outputSpec.getNumericRestrictions().min);
        Assert.assertEquals(BigDecimal.valueOf(5), outputSpec.getNumericRestrictions().min.getLimit());
        Assert.assertFalse(outputSpec.getNumericRestrictions().min.isInclusive());
    }

    @Test
    void shouldreduceIsAfterConstantDateTimeConstraint() {
        final Field field = new Field("test0");
        final LocalDateTime testTimestamp = LocalDateTime.of(2018, 2, 4, 23,25,16);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsAfterConstantDateTimeConstraint(field, testTimestamp));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions().max);
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions().min);
        Assert.assertEquals(testTimestamp, outputSpec.getDateTimeRestrictions().min.getLimit());
        Assert.assertFalse(outputSpec.getDateTimeRestrictions().min.isInclusive());
    }

    @Test
    void shouldreduceNegatedIsAfterConstantDateTimeConstraint() {
        final Field field = new Field("test0");
        final LocalDateTime testTimestamp = LocalDateTime.of(2018, 2, 4, 23,25,16);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsAfterConstantDateTimeConstraint(field, testTimestamp).isFalse());
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions().min);
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions().max);
        Assert.assertEquals(testTimestamp, outputSpec.getDateTimeRestrictions().max.getLimit());
        Assert.assertTrue(outputSpec.getDateTimeRestrictions().max.isInclusive());
    }

    @Test
    void shouldreduceIsAfterOrEqualToConstantDateTimeConstraint() {
        final Field field = new Field("test0");
        final LocalDateTime testTimestamp = LocalDateTime.of(2018, 2, 4, 23,25,16);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsAfterOrEqualToConstantDateTimeConstraint(field, testTimestamp));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions().max);
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions().min);
        Assert.assertEquals(testTimestamp, outputSpec.getDateTimeRestrictions().min.getLimit());
        Assert.assertTrue(outputSpec.getDateTimeRestrictions().min.isInclusive());
    }

    @Test
    void shouldreduceNegatedIsAfterOrEqualToConstantDateTimeConstraint() {
        final Field field = new Field("test0");
        final LocalDateTime testTimestamp = LocalDateTime.of(2018, 2, 4, 23,25,16);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsAfterOrEqualToConstantDateTimeConstraint(field, testTimestamp).isFalse());
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions().min);
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions().max);
        Assert.assertEquals(testTimestamp, outputSpec.getDateTimeRestrictions().max.getLimit());
        Assert.assertFalse(outputSpec.getDateTimeRestrictions().max.isInclusive());
    }

    @Test
    void shouldreduceIsBeforeConstantDateTimeConstraint() {
        final Field field = new Field("test0");
        final LocalDateTime testTimestamp = LocalDateTime.of(2018, 2, 4, 23,25,16);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsBeforeConstantDateTimeConstraint(field, testTimestamp));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions().min);
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions().max);
        Assert.assertEquals(testTimestamp, outputSpec.getDateTimeRestrictions().max.getLimit());
        Assert.assertFalse(outputSpec.getDateTimeRestrictions().max.isInclusive());
    }

    @Test
    void shouldreduceNegatedIsBeforeConstantDateTimeConstraint() {
        final Field field = new Field("test0");
        final LocalDateTime testTimestamp = LocalDateTime.of(2018, 2, 4, 23,25,16);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsBeforeConstantDateTimeConstraint(field, testTimestamp).isFalse());
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions().max);
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions().min);
        Assert.assertEquals(testTimestamp, outputSpec.getDateTimeRestrictions().min.getLimit());
        Assert.assertTrue(outputSpec.getDateTimeRestrictions().min.isInclusive());
    }

    @Test
    void shouldreduceIsBeforeOrEqualToConstantDateTimeConstraint() {
        final Field field = new Field("test0");
        final LocalDateTime testTimestamp = LocalDateTime.of(2018, 2, 4, 23,25,16);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsBeforeOrEqualToConstantDateTimeConstraint(field, testTimestamp));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions().min);
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions().max);
        Assert.assertEquals(testTimestamp, outputSpec.getDateTimeRestrictions().max.getLimit());
        Assert.assertTrue(outputSpec.getDateTimeRestrictions().max.isInclusive());
    }

    @Test
    void shouldreduceNegatedIsBeforeorEqualToConstantDateTimeConstraint() {
        final Field field = new Field("test0");
        final LocalDateTime testTimestamp = LocalDateTime.of(2018, 2, 4, 23,25,16);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new IsBeforeOrEqualToConstantDateTimeConstraint(field, testTimestamp).isFalse());
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions().max);
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions().min);
        Assert.assertEquals(testTimestamp, outputSpec.getDateTimeRestrictions().min.getLimit());
        Assert.assertFalse(outputSpec.getDateTimeRestrictions().min.isInclusive());
    }

    @Test
    void shouldMergeAndReduceIsAfterConstantDateTimeConstraintWithIsBeforeConstantDateTimeConstraint() {
        final Field field = new Field("test0");
        final LocalDateTime startTimestamp = LocalDateTime.of(2013, 11, 19, 10,43,12);
        final LocalDateTime endTimestamp = LocalDateTime.of(2018, 2, 4, 23, 25, 8);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Arrays.asList(
                new IsAfterConstantDateTimeConstraint(field, startTimestamp),
                new IsBeforeConstantDateTimeConstraint(field, endTimestamp));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions().min);
        Assert.assertEquals(startTimestamp, outputSpec.getDateTimeRestrictions().min.getLimit());
        Assert.assertFalse(outputSpec.getDateTimeRestrictions().min.isInclusive());
        Assert.assertNotNull(outputSpec.getDateTimeRestrictions().max);
        Assert.assertEquals(endTimestamp, outputSpec.getDateTimeRestrictions().max.getLimit());
        Assert.assertFalse(outputSpec.getDateTimeRestrictions().max.isInclusive());
    }

    @Test
    void shouldReduceMatchesRegexConstraint() {
        final Field field = new Field("test0");
        String pattern = ".*\\..*";
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<IConstraint> constraints = Collections.singletonList(
                new MatchesRegexConstraint(field, Pattern.compile(pattern)));
        ConstraintReducer testObject = new ConstraintReducer();

        RowSpec testOutput = testObject.reduceConstraintsToRowSpec(profileFields, constraints);

        Assert.assertNotNull(testOutput);
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertNotNull(outputSpec);
        Assert.assertNull(outputSpec.getTypeRestrictions());
        Assert.assertNull(outputSpec.getSetRestrictions());
        Assert.assertNull(outputSpec.getNullRestrictions());
        Assert.assertNull(outputSpec.getNumericRestrictions());
        Assert.assertNull(outputSpec.getDateTimeRestrictions());
        Assert.assertNotNull(outputSpec.getStringRestrictions());
        Assert.assertNotNull(outputSpec.getStringRestrictions().automaton);
    }
}
