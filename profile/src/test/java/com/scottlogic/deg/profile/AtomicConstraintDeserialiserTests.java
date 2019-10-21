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

package com.scottlogic.deg.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsGranularToDateConstraint;
import com.scottlogic.deg.profile.dtos.constraints.*;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO.ConstraintDeserializer;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import java.io.IOException;
import java.util.Arrays;

public class AtomicConstraintDeserialiserTests {

    @Rule
    public ExpectedException expectValidationException = ExpectedException.none();

    @Test
    public void shouldDeserialiseEqualToWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"type\", \"equalTo\": \"X_092\" }";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        EqualToConstraintDTO expected = new EqualToConstraintDTO();
        expected.field = "type";
        expected.value = "X_092";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseEqualToAndThrowInvalidFieldException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"\", \"equalTo\": \"X_092\" }";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        EqualToConstraintDTO expected = new EqualToConstraintDTO();
        expected.field = "";
        expected.value = "X_092";

        assertThat(actual, sameBeanAs(expected));

        //TODO consider where this should be validated, and make a test for that area instead (maybe whole profile deserialisation)

//        assertThrows(
//            ValidationException.class,
//            () -> deserialiseJsonString(json),
//            "invalid field");
    }

    @Test
    public void shouldDeserialiseEqualToAndThrowInvalidConstraintException() throws RuntimeException, IOException {
        // Arrange
        final String json = "{\"field\": \"type\", \"equilTo\": \"X_092\" },";

        try {
            deserialiseJsonString(json);
            Assert.fail("should have thrown an exception");
        } catch (InvalidProfileException e) {
            String expectedMessage = "The constraint json object node for field type doesn't contain any of the expected keywords as properties: {\"field\":\"type\",\"equilTo\":\"X_092\"}";
            assertThat(e.getMessage(), sameBeanAs(expectedMessage));
        }
    }

    @Disabled("#1471 throws a valid json parse exception instead of a validation exception")
    @Test
    public void shouldDeserialiseEqualToAndThrowInvalidConstraintValueException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"type\", \"equalTo\": }";

        try {
            deserialiseJsonString(json);
            Assert.fail("should have thrown an exception");
        } catch (InvalidProfileException e) {
            String expectedMessage = "The constraint json object node for field type doesn't contain any of the expected keywords as properties: {\"field\":\"type\",\"equilTo\":\"X_092\"}";
            assertThat(e.getMessage(), sameBeanAs(expectedMessage));
        }
    }

    @Test
    public void shouldDeserialiseInSetWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"type\", \"inSet\": [ \"X_092\",\"0001-01-01T00:00:00.000Z\" ]}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        InSetOfValuesConstraintDTO expected = new InSetOfValuesConstraintDTO();
        expected.field = "type";
        expected.values = Arrays.asList("X_092", "0001-01-01T00:00:00.000Z");

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseInSetCsvFileWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"country\", \"inSet\": \"countries.csv\" }";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        InSetFromFileConstraintDTO expected = new InSetFromFileConstraintDTO();
        expected.field = "country";
        expected.file = "countries.csv";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseInMapWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"country\", \"inMap\": \"countries.csv\", \"key\": \"Country\" }";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        InMapConstraintDTO expected = new InMapConstraintDTO();
        expected.field = "country";
        expected.file = "countries.csv";
        expected.key = "Country";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseNullWithoutException() throws IOException {
        // Arrange
        final String json = "{\"null\": \"country\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        NullConstraintDTO expected = new NullConstraintDTO();
        expected.field = "country";

        assertThat(actual, sameBeanAs(expected));
    }

    //string
    @Test
    public void shouldDeserialiseMatchingRegexWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"name\", \"matchingRegex\": \"[a-z]{0, 10}\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        MatchesRegexConstraintDTO expected = new MatchesRegexConstraintDTO();
        expected.field = "name";
        expected.value = "[a-z]{0, 10}";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseContainingRegexWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"field\": \"name\", \"containingRegex\": \"[a-z]{0, 10}\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        ContainsRegexConstraintDTO expected = new ContainsRegexConstraintDTO();
        expected.field = "name";
        expected.value = "[a-z]{0, 10}";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseOfLengthWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"field\": \"name\", \"ofLength\": \"5\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        OfLengthConstraintDTO expected = new OfLengthConstraintDTO();
        expected.field = "name";
        expected.value = 5;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseLongerThanWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"field\": \"name\", \"longerThan\": \"3\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        LongerThanConstraintDTO expected = new LongerThanConstraintDTO();
        expected.field = "name";
        expected.value = 3;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseShorterThanWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"name\", \"shorterThan\": \"3\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        ShorterThanConstraintDTO expected = new ShorterThanConstraintDTO();
        expected.field = "name";
        expected.value = 3;

        assertThat(actual, sameBeanAs(expected));
    }

    //Numeric
    @Test
    public void shouldDeserialiseGreaterThanWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"greaterThan\": \"0\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GreaterThanConstraintDTO expected = new GreaterThanConstraintDTO();
        expected.field = "price";
        expected.value = 0;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseGreaterThanOrEqualToWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"greaterThanOrEqualTo\": \"0\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GreaterThanOrEqualToConstraintDTO expected = new GreaterThanOrEqualToConstraintDTO();
        expected.field = "price";
        expected.value = 0;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseGreaterThanOrEqualToFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"greaterThanOrEqualToField\": \"discountPrice\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GreaterThanOrEqualToFieldConstraintDTO expected = new GreaterThanOrEqualToFieldConstraintDTO();
        expected.field = "price";
        expected.otherField = "discountPrice";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseGreaterThanOrEqualToFieldWithOffsetWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"greaterThanOrEqualToField\": \"discountPrice\", \"offset\": 3, \"offsetUnit\": \"days\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GreaterThanOrEqualToFieldConstraintDTO expected = new GreaterThanOrEqualToFieldConstraintDTO();
        expected.field = "price";
        expected.otherField = "discountPrice";
        expected.offset = 3;
        expected.offsetUnit = "days";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseLessThanWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"field\": \"price\", \"lessThan\": \"0\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        LessThanConstraintDTO expected = new LessThanConstraintDTO();
        expected.field = "price";
        expected.value = 0;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseLessThanOrEqualToWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"lessThanOrEqualTo\": \"0\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        LessThanOrEqualToConstraintDTO expected = new LessThanOrEqualToConstraintDTO();
        expected.field = "price";
        expected.value = 0;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseLessThanOrEqualToFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"lessThanOrEqualToField\": \"discountPrice\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        LessThanOrEqualToFieldConstraintDTO expected = new LessThanOrEqualToFieldConstraintDTO();
        expected.field = "price";
        expected.otherField = "discountPrice";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseNumericGranularToWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"granularTo\": \"0.1\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GranularToConstraintDTO expected = new GranularToConstraintDTO();
        expected.field = "price";
        expected.value = "0.1";

        assertThat(actual, sameBeanAs(expected));
    }

    //Datetime

    @Test
    public void shouldDeserialiseAfterWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"field\": \"date\", \"after\": \"2018-09-01T00:00:00.000\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        AfterConstraintDTO expected = new AfterConstraintDTO();
        expected.field = "date";
        expected.value = "2018-09-01T00:00:00.000";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseAfterOrAtWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"date\", \"afterOrAt\": \"2018-09-01T00:00:00.000\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        AfterOrAtConstraintDTO expected = new AfterOrAtConstraintDTO();
        expected.field = "date";
        expected.value = "2018-09-01T00:00:00.000";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseBeforeWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"date\", \"before\": \"2018-09-01T00:00:00.000\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        BeforeConstraintDTO expected = new BeforeConstraintDTO();
        expected.field = "date";
        expected.value = "2018-09-01T00:00:00.000";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseBeforeOrAtWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"date\", \"beforeOrAt\": \"2018-09-01T00:00:00.000\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        BeforeOrAtConstraintDTO expected = new BeforeOrAtConstraintDTO();
        expected.field = "date";
        expected.value = "2018-09-01T00:00:00.000";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseDatetimeGranularToWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"date\", \"granularTo\": \"days\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GranularToConstraintDTO expected = new GranularToConstraintDTO();
        expected.field = "date";
        expected.value = "days";

        assertThat(actual, sameBeanAs(expected));
    }

    //Dependant fields

    @Test
    public void shouldDeserialiseAfterFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"laterDateField\", \"afterField\":\"previousDateField\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        AfterFieldConstraintDTO expected = new AfterFieldConstraintDTO();
        expected.field = "laterDateField";
        expected.otherField = "previousDateField";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseAfterOrAtFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"laterDateField\", \"afterOrAtField\":\"previousDateField\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        AfterOrAtFieldConstraintDTO expected = new AfterOrAtFieldConstraintDTO();
        expected.field = "laterDateField";
        expected.otherField = "previousDateField";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseBeforeFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"laterDateField\", \"beforeField\":\"previousDateField\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        BeforeFieldConstraintDTO expected = new BeforeFieldConstraintDTO();
        expected.field = "laterDateField";
        expected.otherField = "previousDateField";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseBeforeOrAtFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"laterDateField\", \"beforeOrAtField\":\"previousDateField\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        BeforeOrAtFieldConstraintDTO expected = new BeforeOrAtFieldConstraintDTO();
        expected.field = "laterDateField";
        expected.otherField = "previousDateField";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseEqualToFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"laterDateField\", \"equalToField\":\"previousDateField\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        EqualToFieldConstraintDTO expected = new EqualToFieldConstraintDTO();
        expected.field = "laterDateField";
        expected.otherField = "previousDateField";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseEqualToWithOffsetWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"threeDaysAfterField\", \"equalToField\":\"previousDateField\", \"offset\": 3, \"offsetUnit\": \"days\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        EqualToFieldConstraintDTO expected = new EqualToFieldConstraintDTO();
        expected.field = "threeDaysAfterField";
        expected.otherField = "previousDateField";
        expected.offset = 3;
        expected.offsetUnit = "days";

        assertThat(actual, sameBeanAs(expected));
    }

    private ConstraintDTO deserialiseJsonString(String json) throws IOException {
        return new ObjectMapper().readerFor(ConstraintDTO.class).readValue(json);
    }
}
