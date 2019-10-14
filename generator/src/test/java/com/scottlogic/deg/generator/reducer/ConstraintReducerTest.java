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

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.ConstraintNodeBuilder;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.utils.SetUtils;
import org.hamcrest.core.Is;
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

import static com.scottlogic.deg.common.profile.FieldType.*;
import static com.scottlogic.deg.common.util.Defaults.*;
import static org.hamcrest.Matchers.*;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class ConstraintReducerTest {

    private final ConstraintReducer constraintReducer = new ConstraintReducer(
        new FieldSpecMerger()
    );

    private static ConstraintNode nodeFromConstraints(Set<AtomicConstraint> constraints) {
        return new ConstraintNodeBuilder().addAtomicConstraints(constraints).build();
    }

    @Test
    void shouldProduceCorrectFieldSpecsForExample() {
        // ARRANGE
        final Field quantityField = createField("quantity", NUMERIC);
        final Field countryField = createField("country");
        final Field cityField = createField("city");

        ProfileFields fieldList = new ProfileFields(
            Arrays.asList(quantityField, countryField, cityField));

        final DistributedList<Object> countryAmong = new DistributedList<>(Stream.of("UK", "US")
            .map(string -> new WeightedElement<>((Object) string, 1.0F))
            .collect(Collectors.toList()));

        final Set<AtomicConstraint> constraints = SetUtils.setOf(
            new IsGreaterThanOrEqualToConstantConstraint(quantityField, BigDecimal.valueOf(0)),
            new IsGreaterThanConstantConstraint(quantityField, BigDecimal.valueOf(5)).negate(),
            new IsInSetConstraint(countryField, countryAmong));

        // ACT
        final RowSpec reducedConstraints = constraintReducer.reduceConstraintsToRowSpec(
            fieldList,
            nodeFromConstraints(constraints)).get();

        // ASSERT
        FieldSpec quantityFieldSpec = reducedConstraints.getSpecForField(quantityField);
        Assert.assertThat("Quantity fieldspec has no set restrictions", quantityFieldSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Quantity fieldspec has no null restrictions", quantityFieldSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Quantity fieldspec has numeric restrictions", quantityFieldSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Quantity fieldspec has correct lower bound limit",
            ((LinearRestrictions<BigDecimal>) quantityFieldSpec.getRestrictions()).getMin(), Is.is(BigDecimal.ZERO));
        Assert.assertThat("Quantity fieldspec has correct upper bound limit",
            ((LinearRestrictions<BigDecimal>) quantityFieldSpec.getRestrictions()).getMax(), Is.is(BigDecimal.valueOf(5)));

        FieldSpec countryFieldSpec = reducedConstraints.getSpecForField(countryField);
        Assert.assertThat("Country fieldspec has no null restrictions", countryFieldSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Country fieldspec set restrictions have whitelist",
            countryFieldSpec.getWhitelist().list(), notNullValue());
        Assert.assertThat("Country fieldspec set restrictions whitelist has correct size",
            countryFieldSpec.getWhitelist().list().size(), Is.is(2));
        Assert.assertThat("Country fieldspec set restrictions whitelist contains 'UK'",
            countryFieldSpec.getWhitelist().list().contains("UK"), Is.is(true));
        Assert.assertThat("Country fieldspec set restrictions whitelist contains 'US'",
            countryFieldSpec.getWhitelist().list().contains("US"), Is.is(true));

        FieldSpec cityFieldSpec = reducedConstraints.getSpecForField(cityField);
        Assert.assertThat("City fieldspec has no set restrictions", cityFieldSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("City fieldspec has the default string restrictions", cityFieldSpec.getRestrictions(),
            Is.is(FieldSpecFactory.fromType(STRING).getRestrictions()));
        Assert.assertThat("City fieldspec has no null restrictions", cityFieldSpec.isNullable(),
            Is.is(true));
    }

    @Test
    void shouldReduceIsGreaterThanConstantConstraint() {
        final Field field = createField("test0", NUMERIC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsGreaterThanOrEqualToConstantConstraint(field, BigDecimal.valueOf(5)));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(),
            Is.is(equalTo(NUMERIC_MAX)));
        Assert.assertThat("Fieldspec numeric restrictions have lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(),
            Is.is(not(equalTo(NUMERIC_MIN))));
        Assert.assertThat("Fieldspec numeric restriction lower bound is correct",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(BigDecimal.valueOf(5)));
    }

    @Test
    void shouldReduceNegatedIsGreaterThanConstantConstraint() {
        final Field field = createField("test0", NUMERIC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsGreaterThanConstantConstraint(field, BigDecimal.valueOf(5)).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(equalTo(NUMERIC_MIN)));
        Assert.assertThat("Fieldspec numeric restrictions have upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(not(equalTo(NUMERIC_MAX))));
        Assert.assertThat("Fieldspec numeric restrictions upper bound limit is correct",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(BigDecimal.valueOf(5)));
    }

    @Test
    void shouldReduceIsGreaterThanOrEqualToConstantConstraint() {
        final Field field = createField("test0", NUMERIC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsGreaterThanOrEqualToConstantConstraint(field, BigDecimal.valueOf(5)));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(),
            Is.is(NUMERIC_MAX));
        Assert.assertThat("Fieldspec numeric restrictions have lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(),
            Is.is(not(NUMERIC_MIN)));
        Assert.assertThat("Fieldspec numeric restriction lower bound is correct",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(BigDecimal.valueOf(5)));
    }

    @Test
    void shouldReduceNegatedIsGreaterThanOrEqualToConstantConstraint() {
        final Field field = createField("test0", NUMERIC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsGreaterThanConstantConstraint(field, BigDecimal.valueOf(5)).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(NUMERIC_MIN));
        Assert.assertThat("Fieldspec numeric restrictions have upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(not(NUMERIC_MAX)));
        Assert.assertThat("Fieldspec numeric restrictions upper bound limit is correct",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(BigDecimal.valueOf(5)));
    }

    @Test
    void shouldReduceIsLessThanConstantConstraint() {
        final Field field = createField("test0", NUMERIC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsLessThanOrEqualToConstantConstraint(field, BigDecimal.valueOf(5)));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(NUMERIC_MIN));
        Assert.assertThat("Fieldspec numeric restrictions have upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(not(NUMERIC_MAX)));
        Assert.assertThat("Fieldspec numeric restrictions upper bound limit is correct",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(BigDecimal.valueOf(5)));
    }

    @Test
    void shouldReduceNegatedIsLessThanConstantConstraint() {
        final Field field = createField("test0", NUMERIC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsLessThanConstantConstraint(field, BigDecimal.valueOf(5)).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(NUMERIC_MAX));
        Assert.assertThat("Fieldspec numeric restrictions have lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(not(NUMERIC_MIN)));
        Assert.assertThat("Fieldspec numeric restriction lower bound is correct",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(BigDecimal.valueOf(5)));
    }

    @Test
    void shouldReduceIsLessThanOrEqualToConstantConstraint() {
        final Field field = createField("test0", NUMERIC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsLessThanOrEqualToConstantConstraint(field, BigDecimal.valueOf(5)));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(NUMERIC_MIN));
        Assert.assertThat("Fieldspec numeric restrictions have upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(not(NUMERIC_MAX)));
        Assert.assertThat("Fieldspec numeric restrictions upper bound limit is correct",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(BigDecimal.valueOf(5)));
    }

    @Test
    void shouldReduceNegatedIsLessThanOrEqualToConstantConstraint() {
        final Field field = createField("test0", NUMERIC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsLessThanConstantConstraint(field, BigDecimal.valueOf(5)).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has numeric restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec numeric restrictions have no upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(equalTo(NUMERIC_MAX)));
        Assert.assertThat("Fieldspec numeric restrictions have lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(not(equalTo(NUMERIC_MIN))));
        Assert.assertThat("Fieldspec numeric restriction lower bound is correct",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(BigDecimal.valueOf(5)));
    }

    @Test
    void shouldreduceIsAfterConstantDateTimeConstraint() {
        final Field field = createField("test0", DATETIME);
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsAfterOrEqualToConstantDateTimeConstraint(field, testTimestamp));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(equalTo(ISO_MAX_DATE)));
        Assert.assertThat("Fieldspec datetime restrictions have lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(not(equalTo(ISO_MIN_DATE))));
        Assert.assertThat("Fieldspec datetime restrictions have correct lower bound limit",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(testTimestamp));
    }

    @Test
    void shouldreduceNegatedIsAfterConstantDateTimeConstraint() {
        final Field field = createField("test0", DATETIME);
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16,0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsAfterConstantDateTimeConstraint(field, testTimestamp).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(equalTo(ISO_MIN_DATE)));
        Assert.assertThat("Fieldspec datetime restrictions have upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(not(equalTo(ISO_MAX_DATE))));
        Assert.assertThat("Fieldspec datetime restrictions have correct upper bound limit",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(testTimestamp));
    }

    @Test
    void shouldreduceIsAfterOrEqualToConstantDateTimeConstraint() {
        final Field field = createField("test0", DATETIME);
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsAfterOrEqualToConstantDateTimeConstraint(field, testTimestamp));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(equalTo(ISO_MAX_DATE)));
        Assert.assertThat("Fieldspec datetime restrictions have lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(not(equalTo(ISO_MAX_DATE))));
        Assert.assertThat("Fieldspec datetime restrictions have correct lower bound limit",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(testTimestamp));
    }

    @Test
    void shouldreduceNegatedIsAfterOrEqualToConstantDateTimeConstraint() {
        final Field field = createField("test0", DATETIME);
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsAfterConstantDateTimeConstraint(field, testTimestamp).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(equalTo(ISO_MIN_DATE)));
        Assert.assertThat("Fieldspec datetime restrictions have upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(not(equalTo(ISO_MAX_DATE))));
        Assert.assertThat("Fieldspec datetime restrictions have correct upper bound limit",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(testTimestamp));
    }

    @Test
    void shouldreduceIsBeforeConstantDateTimeConstraint() {
        final Field field = createField("test0", DATETIME);
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsBeforeOrEqualToConstantDateTimeConstraint(field, testTimestamp));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(equalTo(ISO_MIN_DATE)));
        Assert.assertThat("Fieldspec datetime restrictions have upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(not(equalTo(ISO_MAX_DATE))));
        Assert.assertThat("Fieldspec datetime restrictions have correct upper bound limit",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(testTimestamp));
    }

    @Test
    void shouldreduceNegatedIsBeforeConstantDateTimeConstraint() {
        final Field field = createField("test0", DATETIME);
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsBeforeConstantDateTimeConstraint(field, testTimestamp).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(equalTo(ISO_MAX_DATE)));
        Assert.assertThat("Fieldspec datetime restrictions have lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(not(equalTo(ISO_MIN_DATE))));
        Assert.assertThat("Fieldspec datetime restrictions have correct lower bound limit",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(testTimestamp));
    }

    @Test
    void shouldreduceIsBeforeOrEqualToConstantDateTimeConstraint() {
        final Field field = createField("test0", DATETIME);
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsBeforeOrEqualToConstantDateTimeConstraint(field, testTimestamp));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is((equalTo(ISO_MIN_DATE))));
        Assert.assertThat("Fieldspec datetime restrictions have upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(not(equalTo(ISO_MAX_DATE))));
        Assert.assertThat("Fieldspec datetime restrictions have correct upper bound limit",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(testTimestamp));
    }

    @Test
    void shouldreduceNegatedIsBeforeorEqualToConstantDateTimeConstraint() {
        final Field field = createField("test0", DATETIME);
        final OffsetDateTime testTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 16, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsBeforeConstantDateTimeConstraint(field, testTimestamp).negate());

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have no upper bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMax(), Is.is(equalTo(ISO_MAX_DATE)));
        Assert.assertThat("Fieldspec datetime restrictions have lower bound",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(not(equalTo(ISO_MIN_DATE))));
        Assert.assertThat("Fieldspec datetime restrictions have correct lower bound limit",
            ((LinearRestrictions) outputSpec.getRestrictions()).getMin(), Is.is(testTimestamp));
    }

    @Test
    void shouldMergeAndReduceIsAfterConstantDateTimeConstraintWithIsBeforeConstantDateTimeConstraint() {
        final Field field = createField("test0", DATETIME);
        final OffsetDateTime startTimestamp = OffsetDateTime.of(2013, 11, 19, 10, 43, 12, 0, ZoneOffset.UTC);
        final OffsetDateTime endTimestamp = OffsetDateTime.of(2018, 2, 4, 23, 25, 8, 0, ZoneOffset.UTC);
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = SetUtils.setOf(
            new IsAfterOrEqualToConstantDateTimeConstraint(field, startTimestamp),
            new IsBeforeOrEqualToConstantDateTimeConstraint(field, endTimestamp));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has datetime restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have lower bound",
            ((LinearRestrictions<OffsetDateTime>) outputSpec.getRestrictions()).getMin(), Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct lower bound limit",
            ((LinearRestrictions<OffsetDateTime>) outputSpec.getRestrictions()).getMin(), Is.is(startTimestamp));
        Assert.assertThat("Fieldspec datetime restrictions have upper bound",
            ((LinearRestrictions<OffsetDateTime>) outputSpec.getRestrictions()).getMax(), Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec datetime restrictions have correct upper bound limit",
            ((LinearRestrictions<OffsetDateTime>) outputSpec.getRestrictions()).getMax(), Is.is(endTimestamp));
    }

    @Test
    void shouldReduceMatchesRegexConstraint() {
        final Field field = createField("test0");
        String pattern = ".*\\..*";
        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new MatchesRegexConstraint(field, Pattern.compile(pattern)));

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has string restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
    }

    @Test
    void shouldReduceStringLongerThanConstraint() {
        final Field field = createField("test0");

        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsStringLongerThanConstraint(field, 5)
        );

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has string restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
    }

    @Test
    void shouldReduceStringShorterThanConstraint() {
        final Field field = createField("test0");

        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new IsStringShorterThanConstraint(field, 5)
        );

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec is not null", outputSpec, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has string restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
    }

    @Test
    void shouldReduceStringHasLengthConstraint() {
        final Field field = createField("test0");

        ProfileFields profileFields = new ProfileFields(Collections.singletonList(field));
        Set<AtomicConstraint> constraints = Collections.singleton(
            new StringHasLengthConstraint(field, 5)
        );

        RowSpec testOutput = constraintReducer.reduceConstraintsToRowSpec(profileFields, nodeFromConstraints(constraints)).get();

        Assert.assertThat("Output is not null", testOutput, Is.is(IsNull.notNullValue()));
        FieldSpec outputSpec = testOutput.getSpecForField(field);
        Assert.assertThat("Fieldspec has no set restrictions", outputSpec.getWhitelist(),
            Is.is(IsNull.nullValue()));
        Assert.assertThat("Fieldspec has no null restrictions", outputSpec.isNullable(),
            Is.is(true));
        Assert.assertThat("Fieldspec has string restrictions", outputSpec.getRestrictions(),
            Is.is(IsNull.notNullValue()));
    }
}
