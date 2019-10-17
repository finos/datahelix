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

import com.scottlogic.deg.profile.dto.ProfileDTO;
import com.scottlogic.deg.profile.serialisation.ProfileDeserialiser;
import com.sun.tools.javac.comp.Todo;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldDeserialiserTests {
    @Test
    public void shouldDeserialiseFieldsWithoutException() throws IOException {
        // Arrange
        final String json =
            "  \"rules\": [" +
            "    { \"field\": \"id\", \"ofType\": \"integer\" }" +
            "    { \"field\": \"country\", \"ofType\": \"string\" }" +
            "    { \"field\": \"tariff\", \"ofType\": \"decimal\" }" +
            "    { \"field\": \"time\", \"ofType\": \"datetime\" }," +
            "    { \"field\": \"first_name\", \"ofType\": \"firstname\" }," +
            "    { \"field\": \"last_name\", \"ofType\": \"lastname\" }," +
            "    { \"field\": \"full_name\", \"ofType\": \"fullname\" }," +
            "  ]";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
       //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseFieldAndThrowInvalidTypeException() throws IOException {
        // Arrange
        final String json =
            "  \"rules\": [" +
            "    { \"field\": \"id\", \"ofTye\": \"integer\" }," +
            "  ],";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseFieldAndThrowInvalidValueException() throws IOException {
        // Arrange
        final String json =
            "  \"rules\": [" +
            "    { \"field\": \"id\", \"ofType\": \"invalidType\" }," +
            "]";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }

    @Test
    public void shouldDeserialiseFieldAndThrowBlankValueException() throws IOException {
        // Arrange
        final String json =
            "  \"rules\": [" +
            "    { \"field\": \"id\", \"ofType\": \"\" }," +
            "]";

        // Act
        //ToDo final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        //ToDo  assertEquals("0.1", profile.schemaVersion);
    }
}
