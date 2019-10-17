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

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GeneralConstraintDeserialiserTests {
    @Test
    public void shouldDeserialiseEqualToWithoutException() throws IOException {
        // Arrange
        final String json =
            "   { \"field\": \"type\", \"equalTo\": \"X_092\" }";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
       //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseEqualToAndThrowInvalidFieldException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"\", \"equalTo\": \"X_092\" }";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseEqualToAndThrowInvalidConstraintException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"type\", \"equilTo\": \"X_092\" },";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseEqualToAndThrowInvalidConstraintValueException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"type\", \"equalTo\": \"\" }";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseInSetWithoutException() throws IOException {
        // Arrange
        final String json =
            "    { \"field\": \"type\", \"inSet\": [ \"X_092\",\"2001-02-03T04:05:06.007\" ]}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
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


}
