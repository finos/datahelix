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

public class GrammaticalConstraintDeserialiserTests {
    @Test
    public void shouldDeserialiseAnyOfWithoutException() throws IOException {
        // Arrange
        final String json =
            "{ \"anyOf\": [" +
            "    { \"field\": \"foo\", \"equalTo\": \"0\" }," +
            "    { \"field\": \"foo\", \"isNull\": \"true\" }" +
            "  ]," +
            "}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
       //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseAnyOfAndThrowInvalidFieldException() throws IOException {
        // Arrange
        final String json =
            "{ \"anyOf\": [" +
            "    { \"field\": \"\", \"equalTo\": \"0\" }," +
            "    { \"field\": \"\", \"isNull\": \"true\" }" +
            "  ]," +
            "}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseAnyOfAndThrowInvalidConstraintException() throws IOException {
        // Arrange
        final String json =
            "{ \"ayOf\": [" +
            "    { \"field\": \"foo\", \"equalTo\": \"0\" }," +
            "    { \"field\": \"foo\", \"isNull\": \"true\" }" +
            "  ]," +
            "}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseAnyOfAndThrowInvalidConstraintValueException() throws IOException {
        // Arrange
        final String json =
            "{ \"anyOf\": [" +
            "    { \"field\": \"foo\", \"equalTo\": \"0\" }," +
            "    { \"field\": \"foo\", \"isNull\": \"tue\" }" +
            "  ]," +
            "}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseAllOfWithoutException() throws IOException {
        // Arrange
        final String json =
            "{ \"allOf\": [" +
            "    { \"field\": \"foo\", \"greaterThan\": \"0\" }," +
            "    { \"field\": \"foo\", \"lessThan\": \"100\" } }" +
            "  ]," +
            "}";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseIfWithoutException() throws IOException {
        // Arrange
        final String json =
            "  \"rules\": [" +
            " \"if\"{ \"field\": \"foo\", \"lessThan\": \"100\" }," +
            " \"then\"{ \"field\": \"bar\", \"greaterThan\": \"0\" }," +
            " \"else\"{ \"field\": \"bar\", \"equalTo\": \"N/A\" } }" +
            "  ]";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }
}
