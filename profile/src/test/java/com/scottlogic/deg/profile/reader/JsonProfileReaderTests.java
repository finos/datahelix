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

package com.scottlogic.deg.profile.reader;


import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.Types;
import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularity;
import com.scottlogic.deg.profile.reader.atomic.AtomicConstraintValueReader;
import com.scottlogic.deg.profile.reader.atomic.FromFileReader;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNull.nullValue;




public class JsonProfileReaderTests {

    private DistributedList<Object> fromFileReaderReturnValue = DistributedList.singleton("test");

    private class MockFromFileReader extends FromFileReader {

        MockFromFileReader() {
            super("");
        }

        @Override
        public DistributedList<Object> setFromFile(String file) {
            return fromFileReaderReturnValue;
        }

        @Override
        public DistributedList<Object> listFromMapFile(String file, String Key) {
            return fromFileReaderReturnValue;
        }

    }

    private final String schemaVersion = "\"0.7\"";
    private String json;

    private JsonProfileReader jsonProfileReader = new JsonProfileReader(
        null,
        new MainConstraintReader(
            new AtomicConstraintValueReader(new MockFromFileReader())));



    private void givenJson(String json) {
        this.json = json;
    }

    private Profile getResultingProfile() throws IOException {
        return jsonProfileReader.read(json);
    }
    
    private void expectInvalidProfileException(String message) {
        Throwable exception = Assertions.assertThrows(InvalidProfileException.class, this::getResultingProfile);
        Assertions.assertEquals(message, exception.getMessage());
    }

    private void expectRules(Consumer<Rule>... ruleAssertions) throws IOException {
        expectMany(this.getResultingProfile().getRules(), ruleAssertions);
    }

    private Consumer<Rule> ruleWithDescription(String expectedDescription) {
        return rule -> Assert.assertThat(rule.getRuleInformation().getDescription(), equalTo(expectedDescription));
    }

    private Consumer<Rule> ruleWithConstraints(Consumer<Constraint>... constraintAsserters) {
        return rule -> expectMany(rule.getConstraints(), constraintAsserters);
    }

    private <T> Consumer<Constraint> typedConstraint(Class<T> constraintType, Consumer<T> asserter) {
        return constraint -> {
            Assert.assertThat(constraint, instanceOf(constraintType));

            asserter.accept((T) constraint);
        };
    }

    private Consumer<Field> fieldWithName(String expectedName) {
        return field -> Assert.assertThat(field.name, equalTo(expectedName));
    }

    private void expectFields(Consumer<Field>... fieldAssertions) throws IOException {
        expectMany(this.getResultingProfile().getFields(), fieldAssertions);
    }

    /**
     * Given a set I1, I2, I3... and some consumers A1, A2, A3..., run A1(I1), A2(I2), A3(I3)...
     * This lets us make assertions about each entry in a sequence
     */
    private <T> void expectMany(
            Iterable<T> assertionTargets,
            Consumer<T>... perItemAssertions) {

        Iterator<T> aIterator = assertionTargets.iterator();
        Iterator<Consumer<T>> bIterator = Arrays.asList(perItemAssertions).iterator();

        while (aIterator.hasNext() && bIterator.hasNext()) {
            bIterator.next().accept(aIterator.next());
        }

        if (aIterator.hasNext() || bIterator.hasNext())
            Assert.fail("Sequences had different numbers of elements");
    }


    @Test
    public void shouldDeserialiseSingleField() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": " + schemaVersion + "," +
                        "    \"fields\": [ { \"name\": \"f1\", \"type\": \"string\" } ]," +
                        "    \"rules\": []" +
                        "}");

        expectFields(
                fieldWithName("f1"));
    }

    @Test
    public void shouldDeserialiseMultipleFields() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": " + schemaVersion + "," +
                        "    \"fields\": [ " +
                        "       { \"name\": \"f1\", \"type\": \"string\" }," +
                        "       { \"name\": \"f2\", \"type\": \"string\" } ]," +
                        "    \"rules\": []" +
                        "}");

        expectFields(
                fieldWithName("f1"),
                fieldWithName("f2"));
    }

    @Test
    public void shouldGiveDefaultNameToUnnamedRules() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": " + schemaVersion + "," +
                        "    \"fields\": [ { \"name\": \"foo\" , \"type\": \"string\"} ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "            { \"field\": \"foo\", \"is\": \"null\" } " +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
            ruleWithDescription("Unnamed rule"));
        expectFields(
            field -> {
                Assert.assertThat(field.name, equalTo("foo"));
                Assert.assertEquals(field.getType(), Types.STRING);
            });
    }

    @Test
    public void shouldReadNameOfNamedRules() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": " + schemaVersion + "," +
                        "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\" } ]," +
                        "    \"rules\": [" +
                        "        {" +
                        "           \"rule\": \"Too rule for school\"," +
                        "           \"constraints\": [" +
                        "               { \"field\": \"foo\", \"is\": \"null\" }" +
                        "           ]" +
                        "        }" +
                        "    ]" +
                        "}");

        expectRules(
            ruleWithDescription("Too rule for school"));
    }

    @Test
    public void shouldNotThrowIsNullWithValueNull() {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\" } ]," +
                "    \"rules\": [" +
                "        {" +
                "           \"rule\": \"Too rule for school\"," +
                "           \"constraints\": [" +
                "               { \"field\": \"foo\", \"is\": \"null\", \"value\": null }" +
                "           ]" +
                "        }" +
                "    ]" +
                "}");

        Assertions.assertDoesNotThrow(
            () -> getResultingProfile());
    }

    @Test
    public void shouldNotThrowIsNullWithValuesNull() {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\" } ]," +
                "    \"rules\": [" +
                "        {" +
                "           \"rule\": \"Too rule for school\"," +
                "           \"constraints\": [" +
                "               { \"field\": \"foo\", \"is\": \"null\", \"values\": null }" +
                "           ]" +
                "        }" +
                "    ]" +
                "}");

        Assertions.assertDoesNotThrow(
            () -> getResultingProfile());
    }

    @Test
    public void shouldDeserialiseIsOfTypeConstraint() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\" } ]," +
                "    \"rules\": []" +
                "}");

        expectRules();
        expectFields(
            field -> {
                Assert.assertThat(field.name, equalTo("foo"));
                Assert.assertEquals(field.getType(), Types.STRING);
            });
    }

    @Test
    public void shouldDeserialiseIsOfTypeConstraint_whenInteger() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"integer\" } ]," +
                "    \"rules\": []" +
                "}");


        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    IsGranularToNumericConstraint.class,
                    c -> {
                        Assert.assertThat(
                            c.granularity,
                            equalTo(new NumericGranularity(0)));
                    })));
    }

    @Test
    public void shouldDeserialiseIsEqualToConstraint() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"equal\" }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");



        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    EqualToConstraint.class,
                    c -> Assert.assertThat(
                        c.value,
                        equalTo("equal")))));

    }

    @Test
    public void shouldDeserialiseFormatConstraint() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": " + schemaVersion + "," +
                        "    \"fields\": [ { " +
                        "           \"name\": \"foo\"," +
                        "           \"formatting\": \"%.5s\"," +
                        "           \"type\": \"string\"" +
                        "    } ]," +
                        "    \"rules\": []" +
                        "}");

        expectFields(
            field -> {
                Assert.assertThat(field.name, equalTo("foo"));
                Assert.assertThat(field.getFormatting(), equalTo("%.5s"));
            }
        );
    }

    @Test
    public void shouldDeserialiseIsOfLengthConstraint() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": " + schemaVersion + "," +
                        "    \"fields\": [ { \"name\": \"id\", \"type\": \"string\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "        { \"field\": \"id\", \"is\": \"ofLength\", \"value\": 5 }" +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    StringHasLengthConstraint.class,
                    c -> Assert.assertThat(c.referenceValue, equalTo(5)))));
    }

    @Test
    public void shouldDeserialiseNotWrapper() throws IOException {
        // Arrange
        givenJson(
                "{" +
                        "    \"schemaVersion\": " + schemaVersion + "," +
                        "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "        { \"not\": { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"string\" } }" +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                NotEqualToConstraint.class,
                                c -> {
                                    Assert.assertThat(
                                            c.value,
                                            equalTo("string"));
                                })));
    }

    @Test
    public void shouldDeserialiseOrConstraint() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": " + schemaVersion + "," +
                        "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "          {" +
                        "            \"anyOf\": [" +
                        "              { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": 1 }," +
                        "              { \"field\": \"foo\", \"is\": \"null\" }" +
                        "            ]" +
                        "          }" +
                        "        ]" +
                        "      }" +
                        "   ]" +
                        "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    OrConstraint.class,
                    c -> Assert.assertThat(
                        c.subConstraints.size(),
                        equalTo(2)))));
    }

    @Test
    public void shouldDeserialiseAndConstraint() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": " + schemaVersion + "," +
                        "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "          {" +
                        "           \"allOf\": [" +
                        "             { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": 1 }," +
                        "             { \"field\": \"foo\", \"is\": \"null\" }" +
                        "            ]" +
                        "          }" +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    AndConstraint.class,
                    c -> Assert.assertThat(
                        c.getSubConstraints().size(),
                        equalTo(2)))));
    }

    @Test
    public void shouldDeserialiseIfConstraint() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": " + schemaVersion + "," +
                        "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "          {" +
                        "            \"if\": { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"string\" }," +
                        "            \"then\": { \"field\": \"foo\", \"is\": \"inSet\", \"values\": [ \"str!\" ] }," +
                        "            \"else\": { \"field\": \"foo\", \"is\": \"longerThan\", \"value\": 3 }" +
                        "          }" +
                        "        ]" +
                        "      }" +
                        "   ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                ConditionalConstraint.class,
                                c -> {
                                    Assert.assertThat(
                                            c.condition,
                                            instanceOf(EqualToConstraint.class));

                                    Assert.assertThat(
                                            c.whenConditionIsTrue,
                                            instanceOf(IsInSetConstraint.class));

                                    Assert.assertThat(
                                            c.whenConditionIsFalse,
                                            instanceOf(IsStringLongerThanConstraint.class));
                                })));
    }

    @Test
    public void shouldDeserialiseIfConstraintWithoutElse() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": " + schemaVersion + "," +
                        "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "          {" +
                        "            \"if\": { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"string\" }," +
                        "            \"then\": { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"str!\" }" +
                        "          }" +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                ConditionalConstraint.class,
                                c -> {
                                    Assert.assertThat(
                                            c.condition,
                                            instanceOf(EqualToConstraint.class));

                                    Assert.assertThat(
                                            c.whenConditionIsTrue,
                                            instanceOf(EqualToConstraint.class));

                                    Assert.assertThat(
                                            c.whenConditionIsFalse,
                                            nullValue());
                                })));
    }

    @Test
    public void shouldDeserialiseOneAsNumericGranularToConstraint() throws IOException {
        givenJson(
            "{" +
            "    \"schemaVersion\": " + schemaVersion + "," +
            "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\" } ]," +
            "    \"rules\": [" +
            "      {" +
            "        \"constraints\": [" +
            "        { \"field\": \"foo\", \"is\": \"granularTo\", \"value\": 1 }" +
            "        ]" +
            "      }" +
            "    ]" +
            "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    IsGranularToNumericConstraint.class,
                    c -> {
                        Assert.assertThat(
                            c.granularity,
                            equalTo(new NumericGranularity(0)));
                    })));
    }

    @Test
    public void shouldDeserialiseTenthAsNumericGranularToConstraint() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"granularTo\", \"value\": 0.1 }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    IsGranularToNumericConstraint.class,
                    c -> {
                        Assert.assertThat(
                            c.granularity,
                            equalTo(new NumericGranularity(1)));
                    })));
    }

    @Test
    public void shouldDisregardTrailingZeroesInNumericGranularities() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"granularTo\", \"value\": 0.100000000 }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    IsGranularToNumericConstraint.class,
                    c -> {
                        Assert.assertThat(
                            c.granularity,
                            equalTo(new NumericGranularity(1)));
                    })));
    }

    @Test
    public void shouldAllowValidISO8601DateTime() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"afterOrAt\", \"value\": \"2019-01-01T00:00:00.000\" }," +
                "        { \"field\": \"foo\", \"is\": \"before\", \"value\": \"2019-01-03T00:00:00.000\" }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectRules(
            rule -> {
                // This is different because the ordering would switch depending on if the whole file was run or just this test
                IsAfterOrEqualToConstantDateTimeConstraint isAfter = (IsAfterOrEqualToConstantDateTimeConstraint) rule.getConstraints().stream()
                    .filter(f -> f.getClass() == IsAfterOrEqualToConstantDateTimeConstraint.class)
                    .findFirst()
                    .get();
                IsBeforeConstantDateTimeConstraint isBefore = (IsBeforeConstantDateTimeConstraint) rule.getConstraints().stream()
                    .filter(f -> f.getClass() == IsBeforeConstantDateTimeConstraint.class)
                    .findFirst()
                    .get();
                Assert.assertEquals(OffsetDateTime.parse("2019-01-01T00:00:00.000Z"), isAfter.referenceValue);
                Assert.assertEquals(OffsetDateTime.parse("2019-01-03T00:00:00.000Z"), isBefore.referenceValue);
            }
        );
    }

    @Test
    public void shouldRejectGreaterThanOneNumericGranularityConstraint() {
        givenJson(
            "{" +
            "    \"schemaVersion\": " + schemaVersion + "," +
            "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\" } ]," +
            "    \"rules\": [" +
            "      {" +
            "        \"constraints\": [" +
            "        { \"field\": \"foo\", \"is\": \"granularTo\", \"value\": 2 }" +
            "        ]" +
            "      }" +
            "    ]" +
            "}");

        expectInvalidProfileException("Field [foo]: Numeric granularity must be <= 1");
    }

    @Test
    public void shouldRejectNonPowerOfTenNumericGranularityConstraint() {
        givenJson(
            "{" +
            "    \"schemaVersion\": " + schemaVersion + "," +
            "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\" } ]," +
            "    \"rules\": [" +
            "      {" +
            "        \"constraints\": [" +
            "        { \"field\": \"foo\", \"is\": \"granularTo\", \"value\": 0.15 }" +
            "        ]" +
            "      }" +
            "    ]" +
            "}");

        expectInvalidProfileException("Field [foo]: Numeric granularity must be fractional power of ten");
    }

    @Test
    public void shouldRejectNonISO8601DateTime() {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"after\", \"value\": \"2018-01-12\" }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException("Field [foo]: Date string '2018-01-12' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS[Z] between (inclusive) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z");
    }

    @Test
    public void shouldRejectEqualToWithNullValue() {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": null }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException("Field [foo]: Couldn't recognise 'value' property, it must be set to a value");
    }

    @Test
    public void shouldRejectLessThanWithNullValue() {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"lessThan\", \"value\": null }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException("Field [foo]: Couldn't recognise 'value' property, it must be set to a value");
    }

    @Test
    public void shouldRejectInSetWithANullValue() {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"inSet\", \"values\": [ null ] }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException("Field [foo]: Set must not contain null");
    }

    @Test
    public void shouldRejectInSetSetToNull() {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"inSet\", \"values\": null }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException("Field [foo]: Couldn't recognise 'value' property, it must be set to a value");
    }

    @Test
    public void shouldRejectAllOfWithEmptySet() {
        givenJson("{" +
            "    \"schemaVersion\": " + schemaVersion + "," +
            "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
            "    \"rules\": [" +
            "      {" +
            "        \"constraints\": [" +
            "        { \"allOf\": [] }" +
            "        ]" +
            "      }" +
            "    ]" +
            "}");

        expectInvalidProfileException("AllOf must contain at least one constraint.");
    }

    @Test
    public void shouldRejectIsConstraintSetToNull() {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": null }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException("Couldn't recognise 'is' property, it must be set to a value");
    }

    @Test
    public void shouldRejectIsConstraintSetToNullWithRuleAndConstraintFormat() {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"rules\": [" +
                "       {" +
                "        \"rule\": \"fooRule\"," +
                "        \"constraints\": [{ \"field\": \"foo\", \"is\": null }]" +
                "       }" +
                "    ]" +
                "}");

        expectInvalidProfileException("Couldn't recognise 'is' property, it must be set to a value");
    }

    @Test
    public void shouldRejectIsConstraintSetToNullForNot() {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"not\": { \"field\": \"foo\", \"is\": null } }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException("Couldn't recognise 'is' property, it must be set to a value");
    }

    @Test
    public void shouldRejectIsConstraintSetToNullForNotWithRuleAndConstraintFormat() {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"rules\": [" +
                "       {" +
                "        \"rule\": \"fooRule\"," +
                "        \"constraints\": [{ \"not\": { \"field\": \"foo\", \"is\": null } }]" +
                "       }" +
                "    ]" +
                "}");

        expectInvalidProfileException("Couldn't recognise 'is' property, it must be set to a value");
    }

    @Test
    public void unique_setsFieldPropertyToTrue_whenSetToTrue() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { " +
                "           \"name\": \"foo\"," +
                "           \"type\": \"integer\"," +
                "           \"unique\": true" +
                "    } ]," +
                "    \"rules\": []" +
                "}");

        expectFields(
            field -> {
                Assert.assertThat(field.name, equalTo("foo"));
                Assert.assertTrue(field.isUnique());
            }
        );
    }

    @Test
    public void unique_setsFieldPropertyToFalse_whenOmitted() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { " +
                "           \"name\": \"foo\"," +
                "           \"type\": \"integer\"" +
                "    } ]," +
                "    \"rules\": []" +
                "}");
        expectFields(
            field -> {
                Assert.assertThat(field.name, equalTo("foo"));
                Assert.assertFalse(field.isUnique());
            }
        );
    }

    @Test
    public void unique_setsFieldPropertyToFalse_whenSetToFalse() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { " +
                "           \"name\": \"foo\"," +
                "           \"type\": \"integer\"," +
                "           \"unique\": false" +
                "    } ]," +
                "    \"rules\": []" +
                "}");

        expectFields(
            field -> {
                Assert.assertThat(field.name, equalTo("foo"));
                Assert.assertFalse(field.isUnique());
            }
        );
    }

    @Test
    public void nullable_addsConstraintForField_whenSetToFalse() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"integer\"," +
                "       \"nullable\": false" +
                "    } ]," +
                "    \"rules\": []" +
                "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    NotNullConstraint.class,
                    c -> {
                        Assert.assertEquals(
                            c.getField().name,
                            "foo");
                    }
                )
            ),
            ruleWithDescription("type-rules")
        );
    }

    @Test
    public void nullable_DoesNotAddConstraintForField_whenSetToTrue() throws IOException  {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"integer\"," +
                "       \"nullable\": true" +
                "    } ]," +
                "    \"rules\": []" +
                "}");

        expectRules(ruleWithDescription("type-rules"));
    }

    @Test
    public void nullable_DoesNotAddConstraintForField_whenNotSet() throws IOException  {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\"," +
                "       \"type\": \"integer\"" +
                "    } ]," +
                "    \"rules\": []" +
                "}");

        expectRules(ruleWithDescription("type-rules"));
    }

    @Test
    public void nullable_addsConstraintForFields_whenSetToFalse() throws IOException  {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"integer\"," +
                "       \"nullable\": false" +
                "    }, { " +
                "       \"name\": \"bar\" ," +
                "       \"type\": \"integer\"," +
                "       \"nullable\": false" +
                "    }]," +
                "    \"rules\": []" +
                "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    NotNullConstraint.class,
                    c -> {
                        Assert.assertEquals(
                            c.getField().name,
                            "foo");
                    }
                ),
                typedConstraint(
                    NotNullConstraint.class,
                    c -> {
                        Assert.assertEquals(
                            c.getField().name,
                            "bar");
                    }
                )
            ),
            ruleWithDescription("type-rules")
        );
    }

    @Test
    public void nullable_addsConstraintForFields_whenOneSetToFalse() throws IOException  {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"integer\"," +
                "       \"nullable\": true" +
                "    }, { " +
                "       \"name\": \"bar\" ," +
                "       \"type\": \"integer\"," +
                "       \"nullable\": false" +
                "    }]," +
                "    \"rules\": []" +
                "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    NotNullConstraint.class,
                    c -> {
                        Assert.assertEquals(
                            c.getField().name,
                            "bar");
                    }
                )
            ),
            ruleWithDescription("type-rules")
        );
    }

    @Test
    public void type_setsFieldTypeProperty_whenSetInFieldDefinition() throws IOException  {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"decimal\"" +
                "    }, { " +
                "       \"name\": \"bar\" ," +
                "       \"type\": \"string\"" +
                "    }]," +
                "    \"rules\": []" +
                "}");

        expectFields(
            field -> {
                Assert.assertThat(field.getType(), equalTo(Types.NUMERIC));
            },
            field -> {
                Assert.assertThat(field.getType(), equalTo(Types.STRING));
            }
        );
        expectRules();
    }

    @Test
    void parser_createsInternalField_whenProfileHasAnInMapConstraint() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"string\"" +
                "    }, { " +
                "       \"name\": \"bar\" ," +
                "       \"type\": \"string\"" +
                "    }]," +
                "    \"rules\": [" +
                "       {" +
                "        \"rule\": \"fooRule\"," +
                "        \"constraints\": [" +
                "           { \"field\": \"foo\", \"is\": \"inMap\", \"key\": \"Foo\", \"file\": \"foobar.csv\" }," +
                "           { \"field\": \"bar\", \"is\": \"inMap\", \"key\": \"Bar\", \"file\": \"foobar.csv\" }" +
                "          ]" +
                "       }" +
                "    ]" +
                "}");

        expectFields(
            field -> {
                Assert.assertEquals("foo", field.name);
                Assert.assertFalse(field.isInternal());
            },
            field -> {
                Assert.assertEquals("bar", field.name);
                Assert.assertFalse(field.isInternal());
            },
            field -> {
                Assert.assertEquals("foobar.csv", field.name);
                Assert.assertTrue(field.isInternal());
            }
        );
    }

    @Test
    void parser_createsInternalField_whenProfileHasANestedInMapConstraint() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": " + schemaVersion + "," +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"string\"" +
                "    }, { " +
                "       \"name\": \"bar\" ," +
                "       \"type\": \"string\"" +
                "    }, { " +
                "       \"name\": \"other\" ," +
                "       \"type\": \"string\"" +
                "    }]," +
                "    \"rules\": [" +
                "       {" +
                "        \"rule\": \"fooRule\"," +
                "        \"constraints\": [" +
                "                {" +
                "                    \"if\":   { \"field\": \"other\", \"is\": \"matchingRegex\", \"value\": \"^[O].*\" }," +
                "                    \"then\": {" +
                "                        \"if\":   { \"field\": \"other\", \"is\": \"matchingRegex\", \"value\": \"^[O].*\" }," +
                "                        \"then\": { \"allOf\": [" +
                "                            { \"field\": \"foo\", \"is\": \"inMap\", \"key\": \"Foo\", \"file\": \"foobar.csv\" }," +
                "                            { \"field\": \"bar\", \"is\": \"inMap\", \"key\": \"Bar\", \"file\": \"foobar.csv\" }" +
                "                        ]}" +
                "                    }" +
                "                }" +
                "          ]" +
                "       }" +
                "    ]" +
                "}");

        expectFields(
            field -> {
                Assert.assertEquals("foo", field.name);
                Assert.assertFalse(field.isInternal());
            },
            field -> {
                Assert.assertEquals("bar", field.name);
                Assert.assertFalse(field.isInternal());
            },
            field -> {
                Assert.assertEquals("other", field.name);
                Assert.assertFalse(field.isInternal());
            },
            field -> {
                Assert.assertEquals("foobar.csv", field.name);
                Assert.assertTrue(field.isInternal());
                Assert.assertEquals(Types.NUMERIC, field.getType());
            }
        );
    }
}
