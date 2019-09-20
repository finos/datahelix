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

package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.ConstraintNodeBuilder;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.*;
import static org.hamcrest.Matchers.*;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class ConstraintReducerTest {

    private final ConstraintReducer constraintReducer = new ConstraintReducer(
        new FieldSpecFactory(new StringRestrictionsFactory()),
        new FieldSpecMerger()
    );

    private static ConstraintNode nodeFromConstraints(Collection<AtomicConstraint> constraints) {
        return new ConstraintNodeBuilder().addAtomicConstraints(constraints).build();
    }

    @Test
    void shouldProduceCorrectFieldSpecsForExample() {
        // ARRANGE
        final Field quantityField = createField("quantity");
        final Field countryField = createField("country");
        final Field cityField = createField("city");

        ProfileFields fieldList = new ProfileFields(
            Arrays.asList(quantityField, countryField, cityField));

        final DistributedSet<Object> countryAmong = new FrequencyDistributedSet<>(Stream.of("UK", "US")
            .map(string -> new WeightedElement<>((Object) string, 1.0F))
            .collect(Collectors.toSet()));

        final List<AtomicConstraint> constraints = Arrays.asList(
            new IsGreaterThanConstantConstraint(quantityField, 0),
            new IsGreaterThanConstantConstraint(quantityField, 5).negate(),
            new IsInSetConstraint(countryField, countryAmong),
            new IsOfTypeConstraint(cityField, STRING));

        // ACT
        final RowSpec reducedConstraints = constraintReducer.reduceConstraintsToRowSpec(
            fieldList,
            nodeFromConstraints(constraints)).get();

        // ASSERT
        FieldSpec quantityFieldSpec = reducedConstraints.getSpecForField(quantityField);
        Assert.assertThat("Quantity fieldspec has no set restrictions", quantityFieldSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Quantity fieldspec has no string restrictions", quantityFieldSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Quantity fieldspec has no null restrictions", quantityFieldSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has a Numeric type constraint",
            quantityFieldSpec.getTypeRestrictions(),
            contains(NUMERIC));
        Assert.assertThat("Quantity fieldspec has no datetime restrictions",
            quantityFieldSpec.getDateTimeRestrictions(), Is.is(IsNull.nullValue()));
        Assert.assertThat("Quantity fieldspec has numeric restrictions", quantityFieldSpec.getNumericRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Quantity fieldspec has correct lower bound limit",
            quantityFieldSpec.getNumericRestrictions().min.getLimit(), Is.is(BigDecimal.ZERO));
        Assert.assertThat("Quantity fieldspec has exclusive lower bound",
            quantityFieldSpec.getNumericRestrictions().min.isInclusive(), Is.is(false));
        Assert.assertThat("Quantity fieldspec has correct upper bound limit",
            quantityFieldSpec.getNumericRestrictions().max.getLimit(), Is.is(BigDecimal.valueOf(5)));
        Assert.assertThat("Quantity fieldspec has inclusive upper bound",
            quantityFieldSpec.getNumericRestrictions().max.isInclusive(), Is.is(true));

        FieldSpec countryFieldSpec = reducedConstraints.getSpecForField(countryField);
        Assert.assertThat("Country fieldspec has no null restrictions", countryFieldSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Country fieldspec set restrictions have whitelist",
            countryFieldSpec.getWhitelist().set(), notNullValue());
        Assert.assertThat("Country fieldspec set restrictions whitelist has correct size",
            countryFieldSpec.getWhitelist().set().size(), Is.is(2));
        Assert.assertThat("Country fieldspec set restrictions whitelist contains 'UK'",
            countryFieldSpec.getWhitelist().set().contains("UK"), Is.is(true));
        Assert.assertThat("Country fieldspec set restrictions whitelist contains 'US'",
            countryFieldSpec.getWhitelist().set().contains("US"), Is.is(true));

        FieldSpec cityFieldSpec = reducedConstraints.getSpecForField(cityField);
        Assert.assertThat("City fieldspec has no set restrictions", cityFieldSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("City fieldspec has no string restrictions", cityFieldSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("City fieldspec has no null restrictions", cityFieldSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("City fieldspec has no datetime restrictions", cityFieldSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("City fieldspec has no numeric restrictions", cityFieldSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("City fieldspec has type restrictions", cityFieldSpec.getTypeRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat(
            cityFieldSpec.getTypeRestrictions(),
            contains(STRING));
    }

    @Test
    void shouldReduceIsGreaterThanConstantConstraint() {
        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsGreaterThanConstantConstraint(field, 5));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a numeric type constraint", outputSpec.getTypeRestrictions(),
            contains(NUMERIC));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no upper bound", outputSpec.getNumericRestrictions().max,
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have lower bound", outputSpec.getNumericRestrictions().min,
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restriction lower bound is correct",
            outputSpec.getNumericRestrictions().min.getLimit(), Is.is(BigDecimal.valueOf(5)));
        Assert.assertThat("Fieldspec numeric restriction lower bound is exclusive",
            outputSpec.getNumericRestrictions().min.isInclusive(), Is.is(false));
    }

    @Test
    void shouldReduceNegatedIsGreaterThanConstantConstraint() {
        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsGreaterThanConstantConstraint(field, 5).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a Numeric type constraint",
            outputSpec.getTypeRestrictions(),
            contains(NUMERIC));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no lower bound",
            outputSpec.getNumericRestrictions().min, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have upper bound",
            outputSpec.getNumericRestrictions().max, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions upper bound limit is correct",
            outputSpec.getNumericRestrictions().max.getLimit(), Is.is(BigDecimal.valueOf(5)));
        Assert.assertThat("Fieldspec numeric resitrctions upper bound is inclusive",
            outputSpec.getNumericRestrictions().max.isInclusive(), Is.is(true));
    }

    @Test
    void shouldReduceIsGreaterThanOrEqualToConstantConstraint() {
        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsGreaterThanOrEqualToConstantConstraint(field, 5));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a numeric type restriction", outputSpec.getTypeRestrictions(),
            contains(NUMERIC));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no upper bound", outputSpec.getNumericRestrictions().max,
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have lower bound", outputSpec.getNumericRestrictions().min,
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restriction lower bound is correct",
            outputSpec.getNumericRestrictions().min.getLimit(), Is.is(BigDecimal.valueOf(5)));
        Assert.assertThat("Fieldspec numeric restriction lower bound is inclusive",
            outputSpec.getNumericRestrictions().min.isInclusive(), Is.is(true));
    }

    @Test
    void shouldReduceNegatedIsGreaterThanOrEqualToConstantConstraint() {
        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsGreaterThanOrEqualToConstantConstraint(field, 5).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a Numeric type constraint",
            outputSpec.getTypeRestrictions(),
            contains(NUMERIC));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no lower bound",
            outputSpec.getNumericRestrictions().min, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have upper bound",
            outputSpec.getNumericRestrictions().max, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions upper bound limit is correct",
            outputSpec.getNumericRestrictions().max.getLimit(), Is.is(BigDecimal.valueOf(5)));
        Assert.assertThat("Fieldspec numeric restrictions upper bound is exclusive",
            outputSpec.getNumericRestrictions().max.isInclusive(), Is.is(false));
    }

    @Test
    void shouldReduceIsLessThanConstantConstraint() {
        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsLessThanConstantConstraint(field, 5));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a Numeric type constraint",
            outputSpec.getTypeRestrictions(),
            contains(NUMERIC));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no lower bound",
            outputSpec.getNumericRestrictions().min, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have upper bound",
            outputSpec.getNumericRestrictions().max, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions upper bound limit is correct",
            outputSpec.getNumericRestrictions().max.getLimit(), Is.is(BigDecimal.valueOf(5)));
        Assert.assertThat("Fieldspec numeric restrictions upper bound is exclusive",
            outputSpec.getNumericRestrictions().max.isInclusive(), Is.is(false));
    }

    @Test
    void shouldReduceNegatedIsLessThanConstantConstraint() {
        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsLessThanConstantConstraint(field, 5).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a numeric type constraint", outputSpec.getTypeRestrictions(),
            contains(NUMERIC));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no upper bound", outputSpec.getNumericRestrictions().max,
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have lower bound", outputSpec.getNumericRestrictions().min,
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restriction lower bound is correct",
            outputSpec.getNumericRestrictions().min.getLimit(), Is.is(BigDecimal.valueOf(5)));
        Assert.assertThat("Fieldspec numeric restriction lower bound is inclusive",
            outputSpec.getNumericRestrictions().min.isInclusive(), Is.is(true));
    }

    @Test
    void shouldReduceIsLessThanOrEqualToConstantConstraint() {
        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsLessThanOrEqualToConstantConstraint(field, 5));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a Numeric type constraint",
            outputSpec.getTypeRestrictions(),
            contains(NUMERIC));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no lower bound",
            outputSpec.getNumericRestrictions().min, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have upper bound",
            outputSpec.getNumericRestrictions().max, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions upper bound limit is correct",
            outputSpec.getNumericRestrictions().max.getLimit(), Is.is(BigDecimal.valueOf(5)));
        Assert.assertThat("Fieldspec numeric restrictions upper bound is inclusive",
            outputSpec.getNumericRestrictions().max.isInclusive(), Is.is(true));
    }

    @Test
    void shouldReduceNegatedIsLessThanOrEqualToConstantConstraint() {
        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsLessThanOrEqualToConstantConstraint(field, 5).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a numeric type constraint", outputSpec.getTypeRestrictions(),
            contains(NUMERIC));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no upper bound", outputSpec.getNumericRestrictions().max,
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have lower bound", outputSpec.getNumericRestrictions().min,
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restriction lower bound is correct",
            outputSpec.getNumericRestrictions().min.getLimit(), Is.is(BigDecimal.valueOf(5)));
        Assert.assertThat("Fieldspec numeric restriction lower bound is exclusive",
            outputSpec.getNumericRestrictions().min.isInclusive(), Is.is(false));
    }

    @Test
    void shouldreduceIsAfterConstantDateTimeConstraint() {
        final Field field = createField("test0");
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsAfterConstantDateTimeConstraint(field, testTimestamp));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a datetime type constraint", outputSpec.getTypeRestrictions(),
            contains(DATETIME));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no upper bound",
            outputSpec.getDateTimeRestrictions().max, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have lower bound",
            outputSpec.getDateTimeRestrictions().min, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct lower bound limit",
            outputSpec.getDateTimeRestrictions().min.getLimit(), Is.is(testTimestamp));
        Assert.assertThat("Fieldspec datetime restrictions have exclusive lower bound",
            outputSpec.getDateTimeRestrictions().min.isInclusive(), Is.is(false));
    }

    @Test
    void shouldreduceNegatedIsAfterConstantDateTimeConstraint() {
        final Field field = createField("test0");
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16,0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsAfterConstantDateTimeConstraint(field, testTimestamp).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a datetime type constraint", outputSpec.getTypeRestrictions(),
            contains(DATETIME));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no lower bound",
            outputSpec.getDateTimeRestrictions().min, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have upper bound",
            outputSpec.getDateTimeRestrictions().max, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct upper bound limit",
            outputSpec.getDateTimeRestrictions().max.getLimit(), Is.is(testTimestamp));
        Assert.assertThat("Fieldspec datetime restrictions have inclusive upper bound",
            outputSpec.getDateTimeRestrictions().max.isInclusive(), Is.is(true));
    }

    @Test
    void shouldreduceIsAfterOrEqualToConstantDateTimeConstraint() {
        final Field field = createField("test0");
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsAfterOrEqualToConstantDateTimeConstraint(field, testTimestamp));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no type restrictions", outputSpec.getTypeRestrictions(),
            contains(DATETIME));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no upper bound",
            outputSpec.getDateTimeRestrictions().max, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have lower bound",
            outputSpec.getDateTimeRestrictions().min, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct lower bound limit",
            outputSpec.getDateTimeRestrictions().min.getLimit(), Is.is(testTimestamp));
        Assert.assertThat("Fieldspec datetime restrictions have inclusive lower bound",
            outputSpec.getDateTimeRestrictions().min.isInclusive(), Is.is(true));
    }

    @Test
    void shouldreduceNegatedIsAfterOrEqualToConstantDateTimeConstraint() {
        final Field field = createField("test0");
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsAfterOrEqualToConstantDateTimeConstraint(field, testTimestamp).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a DateTime type constraint",
            outputSpec.getTypeRestrictions(),
            contains(DATETIME));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no lower bound",
            outputSpec.getDateTimeRestrictions().min, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have upper bound",
            outputSpec.getDateTimeRestrictions().max, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct upper bound limit",
            outputSpec.getDateTimeRestrictions().max.getLimit(), Is.is(testTimestamp));
        Assert.assertThat("Fieldspec datetime restrictions have exclusive upper bound",
            outputSpec.getDateTimeRestrictions().max.isInclusive(), Is.is(false));
    }

    @Test
    void shouldreduceIsBeforeConstantDateTimeConstraint() {
        final Field field = createField("test0");
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsBeforeConstantDateTimeConstraint(field, testTimestamp));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a DateTime type constraint",
            outputSpec.getTypeRestrictions(),
            contains(DATETIME));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no lower bound",
            outputSpec.getDateTimeRestrictions().min, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have upper bound",
            outputSpec.getDateTimeRestrictions().max, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct upper bound limit",
            outputSpec.getDateTimeRestrictions().max.getLimit(), Is.is(testTimestamp));
        Assert.assertThat("Fieldspec datetime restrictions have exclusive upper bound",
            outputSpec.getDateTimeRestrictions().max.isInclusive(), Is.is(false));
    }

    @Test
    void shouldreduceNegatedIsBeforeConstantDateTimeConstraint() {
        final Field field = createField("test0");
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsBeforeConstantDateTimeConstraint(field, testTimestamp).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a datetime type constraint", outputSpec.getTypeRestrictions(),
            contains(DATETIME));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no upper bound",
            outputSpec.getDateTimeRestrictions().max, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have lower bound",
            outputSpec.getDateTimeRestrictions().min, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct lower bound limit",
            outputSpec.getDateTimeRestrictions().min.getLimit(), Is.is(testTimestamp));
        Assert.assertThat("Fieldspec datetime restrictions have inclusive lower bound",
            outputSpec.getDateTimeRestrictions().min.isInclusive(), Is.is(true));
    }

    @Test
    void shouldreduceIsBeforeOrEqualToConstantDateTimeConstraint() {
        final Field field = createField("test0");
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsBeforeOrEqualToConstantDateTimeConstraint(field, testTimestamp));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a DateTime type constraint",
            outputSpec.getTypeRestrictions(),
            contains(DATETIME));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no lower bound",
            outputSpec.getDateTimeRestrictions().min, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have upper bound",
            outputSpec.getDateTimeRestrictions().max, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct upper bound limit",
            outputSpec.getDateTimeRestrictions().max.getLimit(), Is.is(testTimestamp));
        Assert.assertThat("Fieldspec datetime restrictions have inclusive upper bound",
            outputSpec.getDateTimeRestrictions().max.isInclusive(), Is.is(true));
    }

    @Test
    void shouldreduceNegatedIsBeforeorEqualToConstantDateTimeConstraint() {
        final Field field = createField("test0");
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsBeforeOrEqualToConstantDateTimeConstraint(field, testTimestamp).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a datetime type constraint", outputSpec.getTypeRestrictions(),
            contains(DATETIME));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no upper bound",
            outputSpec.getDateTimeRestrictions().max, Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have lower bound",
            outputSpec.getDateTimeRestrictions().min, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct lower bound limit",
            outputSpec.getDateTimeRestrictions().min.getLimit(), Is.is(testTimestamp));
        Assert.assertThat("Fieldspec datetime restrictions have exclusive lower bound",
            outputSpec.getDateTimeRestrictions().min.isInclusive(), Is.is(false));
    }

    @Test
    void shouldMergeAndReduceIsAfterConstantDateTimeConstraintWithIsBeforeConstantDateTimeConstraint() {
        final Field field = createField("test0");
        final OffsetDateTime startTimestamp = OffsetDateTime.of(2013, 11, 19, 10, 43, 12, 0, ZoneOffset.UTC);
        final OffsetDateTime endTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 8, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Arrays.asList(
            new IsAfterConstantDateTimeConstraint(field, startTimestamp),
            new IsBeforeConstantDateTimeConstraint(field, endTimestamp));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a DateTime type constraint",
            outputSpec.getTypeRestrictions(),
            contains(DATETIME));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have lower bound",
            outputSpec.getDateTimeRestrictions().min, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct lower bound limit",
            outputSpec.getDateTimeRestrictions().min.getLimit(), Is.is(startTimestamp));
        Assert.assertThat("Fieldspect datetime restrictions have exclusive lower bound",
            outputSpec.getDateTimeRestrictions().min.isInclusive(), Is.is(false));
        Assert.assertThat("Fieldspec datetime restrictions have upper bound",
            outputSpec.getDateTimeRestrictions().max, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct upper bound limit",
            outputSpec.getDateTimeRestrictions().max.getLimit(), Is.is(endTimestamp));
        Assert.assertThat("Fieldspec datetime restrictions have exclusive upper bound",
            outputSpec.getDateTimeRestrictions().max.isInclusive(), Is.is(false));
    }

    @Test
    void shouldReduceMatchesRegexConstraint() {
        final Field field = createField("test0");
        String pattern = ".*\\..*";
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new MatchesRegexConstraint(field, Pattern.compile(pattern)));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a String type constraint",
            outputSpec.getTypeRestrictions(),
            contains(STRING));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.notNullValue()));
    }

    @Test
    void shouldReduceStringLongerThanConstraint() {
        final Field field = createField("test0");

        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsStringLongerThanConstraint(field, 5)
        );

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a String type constraint",
            outputSpec.getTypeRestrictions(),
            contains(STRING));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.notNullValue()));
    }

    @Test
    void shouldReduceStringShorterThanConstraint() {
        final Field field = createField("test0");

        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new IsStringShorterThanConstraint(field, 5)
        );

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has a string constrint", outputSpec.getTypeRestrictions(),
            contains(STRING));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.notNullValue()));
    }

    @Test
    void shouldReduceStringHasLengthConstraint() {
        final Field field = createField("test0");

        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        List<AtomicConstraint> constraints = Collections.singletonList(
            new StringHasLengthConstraint(field, 5)
        );

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec has a String type constraint",
            outputSpec.getTypeRestrictions(),
            contains(STRING));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has no numeric restrictions", outputSpec.getNumericRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no datetime restrictions", outputSpec.getDateTimeRestrictions(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has string restrictions", outputSpec.getStringRestrictions(),
            Is.is(IsNull.notNullValue()));
    }

    @Test
    void whenHasNumericRestrictions_shouldFilterSet() {

        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));

        List<AtomicConstraint> constraints = Arrays.asList(
            new IsOfTypeConstraint(field, NUMERIC),
            new IsInSetConstraint(field, new FrequencyDistributedSet<>(Stream.of(1, "lorem", 5, "ipsum", 2)
            .map(element -> new WeightedElement<Object>(element, 1.0F))
            .collect(Collectors.toSet())))
        );

        Optional<RowSpec> testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints));

        FieldSpec spec = testOutput.get().getSpecForField(field);

        Assert.assertThat(spec.getWhitelist().set(), containsInAnyOrder(1, 5, 2));
    }

    @Test
    void whenHasStringRestrictions_shouldOnlyFilterStringsInSet() {

        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));

        OffsetDateTime datetimeValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        List<AtomicConstraint> constraints = Arrays.asList(
            new MatchesRegexConstraint(field, Pattern.compile("(lorem|ipsum)")),
            new IsInSetConstraint(field, new FrequencyDistributedSet<>(Stream.of(1, "lorem", 5, "ipsum", 2, "foo", datetimeValue)
            .map(element -> new WeightedElement<Object>(element, 1.0F))
            .collect(Collectors.toSet())))
        );

        Optional<RowSpec> testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints));

        FieldSpec spec = testOutput.get().getSpecForField(field);

        Assert.assertThat(spec.getWhitelist().set(), containsInAnyOrder("lorem", "ipsum"));
    }

    @Test
    void whenHasNumericRestrictions_shouldOnlyFilterNumericValuesInSet() {

        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));

        OffsetDateTime datetimeValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        List<AtomicConstraint> constraints = Arrays.asList(
            new IsGreaterThanOrEqualToConstantConstraint(field, 2),
            new IsInSetConstraint(field, new FrequencyDistributedSet<>(Stream.of(1, "lorem", 5, "ipsum", 2, datetimeValue)
            .map(element -> new WeightedElement<Object>(element, 1.0F))
            .collect(Collectors.toSet())))
        );

        Optional<RowSpec> testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints));

        FieldSpec spec = testOutput.get().getSpecForField(field);

        Assert.assertThat(spec.getWhitelist().set(), containsInAnyOrder( 5, 2));
    }

    @Test
    void whenHasDateTimeRestrictions_shouldOnlyFilterDateTimeValuesInSet() {

        final Field field = createField("test0");
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));

        OffsetDateTime datetimeValue = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        OffsetDateTime oneHourLaterDateTimeValue = datetimeValue.plusHours(1);
        List<AtomicConstraint> constraints = Arrays.asList(
            new IsAfterConstantDateTimeConstraint(field, datetimeValue),
            new IsInSetConstraint(field, new FrequencyDistributedSet<>(Stream.of(1, "lorem", 5, "ipsum", 2, datetimeValue, oneHourLaterDateTimeValue)
            .map(element -> new WeightedElement<Object>(element, 1.0F))
            .collect(Collectors.toSet())))

        );

        Optional<RowSpec> testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints));

        FieldSpec spec = testOutput.get().getSpecForField(field);

        Assert.assertThat(spec.getWhitelist().set(), containsInAnyOrder(oneHourLaterDateTimeValue));
    }
}
