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
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO.ConstraintDeserializer;
import com.scottlogic.deg.profile.dtos.constraints.EqualToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.InSetConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.InSetOfValuesConstraintDTO;
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
        final String json = "   { \"field\": \"type\", \"equalTo\": \"X_092\" }";

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
        final String json = "    { \"field\": \"\", \"equalTo\": \"X_092\" }";

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
        final String json = "    { \"field\": \"type\", \"equilTo\": \"X_092\" },";

        try {
            deserialiseJsonString(json);
            Assert.fail("should have thrown an exception");
        }
        catch (InvalidProfileException e){
            String expectedMessage = "The constraint json object node for field type doesn't contain any of the expected keywords as properties: {\"field\":\"type\",\"equilTo\":\"X_092\"}";
            assertThat(e.getMessage(), sameBeanAs(expectedMessage));
        }
    }

    @Disabled("#1471 throws a valid json parse exception instead of a validation exception")
    @Test
    public void shouldDeserialiseEqualToAndThrowInvalidConstraintValueException() throws IOException {
        // Arrange
        final String json = "    { \"field\": \"type\", \"equalTo\": }";

        try {
            deserialiseJsonString(json);
            Assert.fail("should have thrown an exception");
        }
        catch (InvalidProfileException e){
            String expectedMessage = "The constraint json object node for field type doesn't contain any of the expected keywords as properties: {\"field\":\"type\",\"equilTo\":\"X_092\"}";
            assertThat(e.getMessage(), sameBeanAs(expectedMessage));
        }
    }

    @Test
    public void shouldDeserialiseInSetWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"field\": \"type\", \"inSet\": [ \"X_092\",\"normal string\" ]}";


        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        InSetOfValuesConstraintDTO expected = new InSetOfValuesConstraintDTO();
        expected.field = "type";
        expected.values = Arrays.asList("X_092", "normal string");

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseInSetCsvFileWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"country\", \"inSet\": \"countries.csv\" }";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseInMapWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"country\", \"inMap\": \"countries.csv\", \"key\": \"Country\" }";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseNullWithoutException() throws IOException {
        // Arrange
        final String json = "{" +
            "    { \"field\": \"country\", \"null\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }


    //string
    @Test
    public void shouldDeserialiseMatchingRegexWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"name\", \"matchingRegex\": \"[a-z]{0, 10}\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseContainingRegexWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"name\", \"containingRegex\": \"[a-z]{0, 10}\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseOfLengthWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"name\", \"ofLength\": \"5\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseLongerThanWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"name\", \"longerThan\": \"3\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseShorterThanWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"name\", \"shorterThan\": \"3\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }


    //Numeric
    @Test
    public void shouldDeserialiseGreaterThanWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"price\", \"greaterThan\": \"0\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseGreaterThanOrEqualToWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"price\", \"greaterThanOrEqualTo\": \"0\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseLessThanWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"price\", \"lessThan\": \"0\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseLessThanOrEqualToWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"price\", \"lessThanOrEqualTo\": \"0\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseNumericGranularToWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"price\", \"granularTo\": \"0.1\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    //Datetime

    @Test
    public void shouldDeserialiseAfterWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"date\", \"after\": \"2018-09-01T00:00:00.000\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }
    @Test
    public void shouldDeserialiseAfterOrAtWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"date\", \"afterOrAt\": \"2018-09-01T00:00:00.000\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseBeforeWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"date\", \"before\": \"2018-09-01T00:00:00.000\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseBeforeOrAtWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"date\", \"beforeOrAt\": \"2018-09-01T00:00:00.000\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseDatetimeGranularToWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"date\", \"granularTo\": \"days\"}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    //Dependant fields

    @Test
    public void shouldDeserialiseOtherFieldWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"laterDateField\", \"after\":\"previousDateField\"}";

       // was { "field": "laterDateField", "is": "after", "otherField": "previousDateField" }

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }
    @Test
    public void shouldDeserialiseOffsetWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"threeDaysAfterField\", \"equalTo\":\"previousDateField\", \"offset\": 3, \"offsetUnit\": \"days\"}";

        // was { "field": "threeDaysAfterField", "is": "equalTo", "otherField": "previousDateField", "offset": 3, "offsetUnit": "days" }

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    private ConstraintDTO deserialiseJsonString(String json) throws IOException {
        return new ObjectMapper().readerFor(ConstraintDTO.class).readValue(json);
    }
}
