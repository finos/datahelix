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

package com.scottlogic.datahelix.generator.profile.reader;


import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.profile.DateTimeGranularity;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.common.profile.NumericGranularity;
import com.scottlogic.datahelix.generator.common.util.FileUtils;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.*;
import com.scottlogic.datahelix.generator.core.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.datahelix.generator.custom.CustomGeneratorList;
import com.scottlogic.datahelix.generator.profile.custom.CustomConstraint;
import com.scottlogic.datahelix.generator.profile.custom.CustomConstraintFactory;
import com.scottlogic.datahelix.generator.profile.services.ConstraintService;
import com.scottlogic.datahelix.generator.profile.services.FieldService;
import com.scottlogic.datahelix.generator.profile.services.NameRetrievalService;
import com.scottlogic.datahelix.generator.profile.validators.ConfigValidator;
import com.scottlogic.datahelix.generator.profile.validators.CreateProfileValidator;
import com.scottlogic.datahelix.generator.profile.validators.profile.ProfileValidator;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

import static com.scottlogic.datahelix.generator.common.util.Defaults.DEFAULT_DATE_FORMATTING;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;


public class JsonProfileReaderTests {
    private DistributedList<Object> inSetReaderReturnValue = DistributedList.singleton("test");
    private DistributedList<String> fromFileReaderReturnValue = DistributedList.singleton("test");

    private class MockFromFileReader extends FileReader {
        MockFromFileReader() {
            super(null);
        }

        @Override
        public DistributedList<Object> setFromFile(String file)
        {
            return inSetReaderReturnValue;
        }

        @Override
        public DistributedList<String> listFromMapFile(String file, String Key)
        {
            return fromFileReaderReturnValue;
        }
    }

    private String json;

    private JsonProfileReader jsonProfileReader = new JsonProfileReader(
        null,
        new ConfigValidator(new FileUtils()),
        new MockFromFileReader(),
        new ProfileCommandBus(
            new FieldService(),
                new ConstraintService(
                    new CustomConstraintFactory(new CustomGeneratorList()),
                    new NameRetrievalService(new CsvInputStreamReaderFactory(""))),
                new CustomConstraintFactory(new CustomGeneratorList()),
            new CreateProfileValidator(new ProfileValidator(null))));

    private void givenJson(String json) {
        this.json = json;
    }

    private Profile getResultingProfile() {
        return jsonProfileReader.read(json);
    }
    
    private void expectValidationException(String message) {
        Throwable exception = Assertions.assertThrows(ValidationException.class, this::getResultingProfile);
        Assertions.assertEquals(message, exception.getMessage());
    }

    @SafeVarargs
    private final void expectConstraints(Consumer<Constraint>... constraintAssertions) {
        expectMany(this.getResultingProfile().getConstraints(), constraintAssertions);
    }

    private <T> Consumer<Constraint> typedConstraint(Class<T> constraintType, Consumer<T> asserter) {
        return constraint -> {
            Assert.assertThat(constraint, instanceOf(constraintType));

            //noinspection unchecked
            asserter.accept((T) constraint);
        };
    }

    private Consumer<Field> fieldWithName(String expectedName) {
        return field -> Assert.assertThat(field.getName(), equalTo(expectedName));
    }

    @SafeVarargs
    private final void expectFields(Consumer<Field>... fieldAssertions) {
        expectMany(this.getResultingProfile().getFields(), fieldAssertions);
    }

    /**
     * Given a set I1, I2, I3... and some consumers A1, A2, A3..., run A1(I1), A2(I2), A3(I3)...
     * This lets us make assertions about each entry in a sequence
     */
    @SafeVarargs
    private final <T> void expectMany(
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
    public void shouldDeserialiseSingleField() {
        givenJson(
                "{" +
                        "    \"fields\": [ { \"name\": \"f1\", \"type\": \"string\" } ]," +
                        "    \"constraints\": []" +
                        "}");

        expectFields(fieldWithName("f1"));
    }

    @Test
    public void shouldDeserialiseMultipleFields() {
        givenJson(
                "{" +
                        "    \"fields\": [ " +
                        "       { \"name\": \"f1\", \"type\": \"string\" }," +
                        "       { \"name\": \"f2\", \"type\": \"string\" } ]," +
                        "    \"constraints\": []" +
                        "}");

        expectFields(
                fieldWithName("f1"),
                fieldWithName("f2"));
    }

    @Test
    public void shouldNotThrowIsNullWithValueNull() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\" } ]," +
                "    \"constraints\": [" +
                "               { \"field\": \"foo\", \"isNull\": true }" +
                "    ]" +
                "}");

        Assertions.assertDoesNotThrow(this::getResultingProfile);
    }

    @Test
    public void shouldNotThrowIsNullWithValuesNull() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\" } ]," +
                "    \"constraints\": [" +
                "               { \"field\": \"foo\", \"isNull\": true }" +
                "    ]" +
                "}");

        Assertions.assertDoesNotThrow(this::getResultingProfile);
    }

    @Test
    public void shouldDeserialiseIsOfTypeConstraint() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\", \"nullable\": true } ]," +
                "    \"constraints\": []" +
                "}");

        expectConstraints();
        expectFields(
            field -> {
                Assert.assertThat(field.getName(), equalTo("foo"));
                Assert.assertEquals(FieldType.STRING, field.getType());
            });
    }

    @Test
    public void shouldDeserialiseIsOfTypeConstraint_whenInteger() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"integer\", \"nullable\": true } ]," +
                "    \"constraints\": []" +
                "}");


        expectConstraints(typedConstraint(GranularToNumericConstraint.class,
                    c -> Assert.assertThat(
                            c.granularity,
                            equalTo(new NumericGranularity(0)))
                    ));
    }

    @Test
    public void shouldDeserialiseIsEqualToConstraint() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\", \"nullable\": true } ]," +
                "    \"constraints\": [" +
                "        { \"field\": \"foo\",  \"equalTo\": \"equal\" }" +
                "    ]" +
                "}");



        expectConstraints(typedConstraint(EqualToConstraint.class,
            c -> Assert.assertThat( c.value, equalTo("equal"))));

    }

    @Test
    public void shouldDeserialiseFormatConstraint() {
        givenJson(
                "{" +
                        "    \"fields\": [ { " +
                        "           \"name\": \"foo\"," +
                        "           \"formatting\": \"%.5s\"," +
                        "           \"type\": \"string\"" +
                        "    } ]," +
                        "    \"constraints\": []" +
                        "}");

        expectFields(
            field -> {
                Assert.assertThat(field.getName(), equalTo("foo"));
                Assert.assertThat(field.getFormatting(), equalTo("%.5s"));
            }
        );
    }

    @Test
    public void shouldDeserialiseIsOfLengthConstraint() {
        givenJson(
                "{" +
                        "    \"fields\": [ { \"name\": \"id\", \"type\": \"string\" , \"nullable\": true} ]," +
                        "    \"constraints\": [" +
                        "        { \"field\": \"id\",  \"ofLength\": 5 }" +
                        "    ]" +
                        "}");

        expectConstraints(typedConstraint(OfLengthConstraint.class,
                    c -> Assert.assertThat(c.referenceValue, equalTo(5))));
    }

    @Test
    public void shouldDeserialiseNotWrapper() {
        // Arrange
        givenJson(
                "{" +
                        "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\", \"nullable\": true } ]," +
                        "    \"constraints\": [" +
                        "        { \"not\": { \"field\": \"foo\",  \"equalTo\": \"string\" } }" +
                        "    ]" +
                        "}");

        expectConstraints(typedConstraint(NotEqualToConstraint.class,
            c ->  Assert.assertThat(c.value, equalTo("string"))));
    }

    @Test
    public void shouldDeserialiseOrConstraint() {
        givenJson(
                "{" +
                        "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\", \"nullable\": true } ]," +
                        "    \"constraints\": [" +
                        "          {" +
                        "            \"anyOf\": [" +
                        "              { \"field\": \"foo\",  \"equalTo\": 1 }," +
                        "              { \"field\": \"foo\", \"isNull\": true }" +
                        "            ]" +
                        "          }" +
                        "   ]" +
                        "}");

        expectConstraints(typedConstraint(OrConstraint.class,
                    c -> Assert.assertThat(c.subConstraints.size(),equalTo(2))));
    }

    @Test
    public void shouldDeserialiseAndConstraint() {
        givenJson(
                "{" +
                        "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\", \"nullable\": true } ]," +
                        "    \"constraints\": [" +
                        "          {" +
                        "           \"allOf\": [" +
                        "             { \"field\": \"foo\",  \"equalTo\": 1 }," +
                        "             { \"field\": \"foo\", \"isNull\": true }" +
                        "            ]" +
                        "          }" +
                        "    ]" +
                        "}");

        expectConstraints(typedConstraint(AndConstraint.class,
                    c -> Assert.assertThat(c.getSubConstraints().size(),equalTo(2))));
    }

    @Test
    public void shouldDeserialiseIfConstraint() {
        givenJson(
                "{" +
                        "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\", \"nullable\": true } ]," +
                        "    \"constraints\": [" +
                        "          {" +
                        "            \"if\": { \"field\": \"foo\",  \"equalTo\": \"string\" }," +
                        "            \"then\": { \"field\": \"foo\",  \"inSet\": [ \"str!\" ] }," +
                        "            \"else\": { \"field\": \"foo\",  \"longerThan\": 3 }" +
                        "          }" +
                        "   ]" +
                        "}");

        expectConstraints(typedConstraint( ConditionalConstraint.class,
                                c -> {
                                    Assert.assertThat(
                                            c.condition,
                                            instanceOf(EqualToConstraint.class));

                                    Assert.assertThat(
                                            c.whenConditionIsTrue,
                                            instanceOf(InSetConstraint.class));

                                    Assert.assertThat(
                                            c.whenConditionIsFalse,
                                            instanceOf(LongerThanConstraint.class));
                                }));
    }

    @Test
    public void shouldDeserialiseIfConstraintWithoutElse() {
        givenJson(
                "{" +
                        "    \"fields\": [ { \"name\": \"foo\", \"type\": \"string\", \"nullable\": true } ]," +
                        "    \"constraints\": [" +
                        "          {" +
                        "            \"if\": { \"field\": \"foo\",  \"equalTo\": \"string\" }," +
                        "            \"then\": { \"field\": \"foo\",  \"equalTo\": \"str!\" }" +
                        "          }" +
                        "    ]" +
                        "}");

        expectConstraints(typedConstraint(ConditionalConstraint.class,
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
                                }));
    }

    @Test
    public void shouldDeserialiseOneAsNumericGranularToConstraint() {
        givenJson(
            "{" +
            "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\", \"nullable\": true } ]," +
            "    \"constraints\": [" +
            "        { \"field\": \"foo\",  \"granularTo\": 1 }" +
            "    ]" +
            "}");

        expectConstraints(typedConstraint(GranularToNumericConstraint.class,
                    c -> Assert.assertThat(c.granularity,equalTo(new NumericGranularity(0)))));
    }

    @Test
    public void shouldDeserialiseTenthAsNumericGranularToConstraint() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\", \"nullable\": true } ]," +
                "    \"constraints\": [" +
                "        { \"field\": \"foo\",  \"granularTo\": 0.1 }" +
                "    ]" +
                "}");

        expectConstraints(typedConstraint(GranularToNumericConstraint.class,
                    c -> Assert.assertThat(c.granularity,equalTo(new NumericGranularity(1)))));
    }

    @Test
    public void shouldDisregardTrailingZeroesInNumericGranularities() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\", \"nullable\": true } ]," +
                "    \"constraints\": [" +
                "        { \"field\": \"foo\",  \"granularTo\": 0.100000000 }" +
                "    ]" +
                "}");

        expectConstraints(typedConstraint(GranularToNumericConstraint.class,
                    c -> Assert.assertThat(c.granularity,equalTo(new NumericGranularity(1)))));
    }

    @Test
    public void shouldAllowValidISO8601DateTime() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\", \"nullable\": true } ]," +
                "    \"constraints\": [" +
                "        { \"field\": \"foo\",  \"afterOrAt\": \"2019-01-01T00:00:00.000\" }," +
                "        { \"field\": \"foo\",  \"before\": \"2019-01-03T00:00:00.000\" }" +
                "    ]" +
                "}");

        expectConstraints(
            typedConstraint(AfterOrAtConstraint.class,
                isAfter ->  Assert.assertEquals(OffsetDateTime.parse("2019-01-01T00:00:00.000Z"), isAfter.referenceValue)),
            typedConstraint(BeforeConstraint.class,
                isBefore -> Assert.assertEquals(OffsetDateTime.parse("2019-01-03T00:00:00.000Z"), isBefore.referenceValue))
        );
    }

    @Test
    public void shouldRejectGreaterThanOneNumericGranularityConstraint() {
        givenJson(
            "{" +
            "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\" } ]," +
            "    \"constraints\": [" +
            "        { \"field\": \"foo\",  \"granularTo\": 2 }" +
            "    ]" +
            "}");

        expectValidationException("Numeric granularity must be <= 1");
    }

    @Test
    public void shouldRejectNonPowerOfTenNumericGranularityConstraint() {
        givenJson(
            "{" +
            "    \"fields\": [ { \"name\": \"foo\", \"type\": \"decimal\" } ]," +
            "   \"constraints\": [" +
            "        { \"field\": \"foo\",  \"granularTo\": 0.15 }" +
            "    ]" +
            "}");

        expectValidationException("Numeric granularity must be fractional power of ten");
    }

    @Test
    public void shouldRejectEqualToWithNullValue() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"constraints\": [" +
                "        { \"field\": \"foo\",  \"equalTo\": null }" +
                "    ]" +
                "}");

        expectValidationException("Values must be specified | Field: foo | Constraint: equalTo");
    }

    @Test
    public void shouldRejectLessThanWithNullValue() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"constraints\": [" +
                "        { \"field\": \"foo\",  \"lessThan\": null }" +
                "    ]" +
                "}");

        expectValidationException("Number must be specified | Field: foo | Constraint: lessThan");
    }

    @Test
    public void shouldRejectInSetWithANullValue() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"constraints\": [" +
                "        { \"field\": \"foo\",  \"inSet\": [ null ] }" +
                "    ]" +
                "}");

        expectValidationException("Values must be specified | Field: foo | Constraint: inSet");
    }

    @Test
    public void shouldRejectInSetSetToNull() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"constraints\": [" +
                "        { \"field\": \"foo\",  \"inSet\": null }" +
                "    ]" +
                "}");

        expectValidationException("In set values must be specified | Field: foo | Constraint: inSet");
    }

    @Test
    public void shouldRejectIsConstraintSetToNullForNot() {
        givenJson(
            "{" +
                "    \"fields\": [ { \"name\": \"foo\", \"type\": \"datetime\" } ]," +
                "    \"constraints\": [" +
                "        { \"not\": { \"field\": \"foo\", \"is\": null } }" +
                "    ]" +
                "}");

        expectValidationException("Invalid json: {\"field\":\"foo\",\"is\":null}");
    }

    @Test
    public void unique_setsFieldPropertyToTrue_whenSetToTrue() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "           \"name\": \"foo\"," +
                "           \"type\": \"integer\"," +
                "           \"unique\": true" +
                "    } ]," +
                "    \"constraints\": []" +
                "}");

        expectFields(
            field -> {
                Assert.assertThat(field.getName(), equalTo("foo"));
                Assert.assertTrue(field.isUnique());
            }
        );
    }

    @Test
    public void unique_setsFieldPropertyToFalse_whenOmitted() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "           \"name\": \"foo\"," +
                "           \"type\": \"integer\"" +
                "    } ]," +
                "    \"constraints\": []" +
                "}");
        expectFields(
            field -> {
                Assert.assertThat(field.getName(), equalTo("foo"));
                Assert.assertFalse(field.isUnique());
            }
        );
    }

    @Test
    public void unique_setsFieldPropertyToFalse_whenSetToFalse() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "           \"name\": \"foo\"," +
                "           \"type\": \"integer\"," +
                "           \"unique\": false" +
                "    } ]," +
                "    \"constraints\": []" +
                "}");

        expectFields(
            field -> {
                Assert.assertThat(field.getName(), equalTo("foo"));
                Assert.assertFalse(field.isUnique());
            }
        );
    }

    @Test
    public void nullable_addsConstraintForField_whenSetToFalse() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"decimal\"," +
                "       \"nullable\": false" +
                "    } ]," +
                "    \"constraints\": []" +
                "}");

        expectConstraints(
            typedConstraint(NotNullConstraint.class,
                    c -> Assert.assertEquals("foo", c.getField().getName())));
    }

    @Test
    public void nullable_DoesNotAddConstraintForField_whenSetToTrue() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"decimal\"," +
                "       \"nullable\": true" +
                "    } ]," +
                "    \"constraints\": []" +
                "}");

        expectConstraints();
    }

    @Test
    public void nullable_addsConstraintForFields_whenSetToFalse() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"decimal\"," +
                "       \"nullable\": false" +
                "    }, { " +
                "       \"name\": \"bar\" ," +
                "       \"type\": \"decimal\"," +
                "       \"nullable\": false" +
                "    }]," +
                "    \"constraints\": []" +
                "}");

        expectConstraints(
            typedConstraint(NotNullConstraint.class,
                c ->  Assert.assertEquals("foo", c.getField().getName())),
            typedConstraint(NotNullConstraint.class,
                c -> Assert.assertEquals("bar", c.getField().getName()))
            );
    }

    @Test
    public void nullable_addsConstraintForFields_whenOneSetToFalse() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"decimal\"," +
                "       \"nullable\": true" +
                "    }, { " +
                "       \"name\": \"bar\" ," +
                "       \"type\": \"decimal\"," +
                "       \"nullable\": false" +
                "    }]," +
                "    \"constraints\": []" +
                "}");

        expectConstraints(
            typedConstraint(NotNullConstraint.class,
                    c -> Assert.assertEquals("bar", c.getField().getName())));
    }

    @Test
    public void type_setsFieldTypeProperty_whenSetInFieldDefinition() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"decimal\" ," +
                "       \"nullable\": \"true\"" +
                "    }, { " +
                "       \"name\": \"bar\" ," +
                "       \"type\": \"string\" ," +
                "       \"nullable\": \"true\"" +
                "    }]," +
                "    \"constraints\": []" +
                "}");

        expectFields(
            field -> Assert.assertThat(field.getType(), equalTo(FieldType.NUMERIC)),
            field ->  Assert.assertThat(field.getType(), equalTo(FieldType.STRING))
        );
        expectConstraints();
    }

    @Test
    void parser_createsInternalField_whenProfileHasAnInMapConstraint() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"string\"" +
                "    }, { " +
                "       \"name\": \"bar\" ," +
                "       \"type\": \"string\"" +
                "    }]," +
                "    \"constraints\": [" +
                "           { \"field\": \"foo\", \"inMap\": \"foobar.csv\", \"key\": \"Foo\" }," +
                "           { \"field\": \"bar\", \"inMap\": \"foobar.csv\", \"key\": \"Bar\"}" +
                "    ]" +
                "}");

         expectFields(
            field -> {
                Assert.assertEquals("foo", field.getName());
                Assert.assertFalse(field.isInternal());
            },
            field -> {
                Assert.assertEquals("bar", field.getName());
                Assert.assertFalse(field.isInternal());
            },
            field -> {
                Assert.assertEquals("foobar.csv", field.getName());
                Assert.assertTrue(field.isInternal());
            }
        );
    }

    @Test
    void parser_createsInternalField_whenProfileHasANestedInMapConstraint() {
        givenJson(
            "{" +
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
                "    \"constraints\": [" +
                "                {" +
                "                    \"if\":   { \"field\": \"other\", \"matchingRegex\": \"^[O].*\" }," +
                "                    \"then\": {" +
                "                        \"if\":   { \"field\": \"other\", \"matchingRegex\": \"^[O].*\" }," +
                "                        \"then\": { \"allOf\": [" +
                "                            { \"field\": \"foo\", \"inMap\": \"foobar.csv\", \"key\": \"Foo\"}," +
                "                            { \"field\": \"bar\", \"inMap\": \"foobar.csv\", \"key\": \"Bar\"}" +
                "                        ]}" +
                "                    }" +
                "                }" +
                "    ]" +
                "}");

        expectFields(
            field -> {
                Assert.assertEquals("foo", field.getName());
                Assert.assertFalse(field.isInternal());
            },
            field -> {
                Assert.assertEquals("bar", field.getName());
                Assert.assertFalse(field.isInternal());
            },
            field -> {
                Assert.assertEquals("other", field.getName());
                Assert.assertFalse(field.isInternal());
            },
            field -> {
                Assert.assertEquals("foobar.csv", field.getName());
                Assert.assertTrue(field.isInternal());
                Assert.assertEquals(FieldType.NUMERIC, field.getType());
            }
        );
    }

    @Test
    public void formatting_withDateType_shouldSetCorrectGranularity() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"date\"," +
                "       \"nullable\": \"true\"" +
                "    }]," +
                "    \"constraints\": []" +
                "}");

        expectConstraints(typedConstraint(GranularToDateConstraint.class,
                    c -> Assert.assertThat(c.granularity, equalTo(new DateTimeGranularity(ChronoUnit.DAYS)))));
    }

    @Test
    public void formatting_withDateType_shouldSetCorrectFormatting() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"date\"" +
                "    }]," +
                "    \"constraints\": []" +
                "}");

        expectFields(field -> Assert.assertEquals(DEFAULT_DATE_FORMATTING,field.getFormatting()) );
    }

    @Test
    public void formatting_withDateTypeAndFormatting_shouldSetCorrectFormatting() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"date\"," +
                "       \"formatting\": \"%tD\"" +
                "    }]," +
                "    \"constraints\": []" +
                "}");

        expectFields(field -> Assert.assertEquals("%tD",field.getFormatting()));
    }

    @Test
    public void addsConstraintForGenerator() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"string\"," +
                "       \"generator\": \"lorem ipsum\"," +
                "       \"nullable\": true" +
                "    }]," +
                "    \"constraints\": []" +
                "}");

        expectConstraints(typedConstraint(CustomConstraint.class,
                    c -> Assert.assertEquals("foo", c.getField().getName())));

    }


    @Test
    public void exceptionWhenGeneratorDoesNotExist() {
        givenJson(
            "{" +
                "    \"fields\": [ { " +
                "       \"name\": \"foo\" ," +
                "       \"type\": \"string\"," +
                "       \"generator\": \"INCORRECT\"," +
                "       \"nullable\": true" +
                "    }]," +
                "    \"constraints\": []" +
                "}");

        expectValidationException("Custom generator INCORRECT does not exist it needs to be created and added to the CustomGeneratorList class");
    }
}
