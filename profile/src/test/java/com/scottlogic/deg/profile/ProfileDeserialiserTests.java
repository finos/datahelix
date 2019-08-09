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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.scottlogic.deg.profile.serialisation.ProfileDeserialiser;
import com.scottlogic.deg.profile.v0_1.ProfileDTO;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ProfileDeserialiserTests {
    @Test
    public void shouldDeserialisev0_1ProfileWithoutException() throws IOException {
        // Arrange
        final String json = "{" +
            "  \"schemaVersion\" : \"0.1\"," +
            "  \"fields\": [" +
            "    { \"name\": \"id\" }," +
            "    { \"name\": \"type\" }," +
            "    { \"name\": \"time\" }," +
            "    { \"name\": \"country\" }," +
            "    { \"name\": \"tariff\" }," +
            "    { \"name\": \"low_price\" }," +
            "    { \"name\": \"high_price\" }" +
            "  ]," +
            "  \"rules\": [" +
            "    { \"field\": \"id\", \"is\": \"ofType\", \"value\": \"datetime\" }," +
            "    { \"not\": { \"field\": \"id\", \"is\": \"null\" } }," +

            "    { \"field\": \"low_price\", \"is\": \"ofType\", \"value\": \"numeric\" }," +
            "    { \"not\": { \"field\": \"low_price\", \"is\": \"null\" } }," +
            "    { \"field\": \"low_price\", \"is\": \"greaterThanOrEqual\", \"value\": \"0\" }," +

            "    {" +
            "      \"rule\": \"Some rule\"," +
            "      \"constraints\": [" +
            "        { \"field\": \"country\", \"is\": \"inSet\", \"values\": [ \"USA\", \"GB\", \"FRANCE\" ] }" +
            "      ]" +
            "    }," +

            "    {" +
            "      \"if\": {" +
            "        \"anyOf\": [" +
            "          { \"field\": \"type\", \"is\": \"equalTo\", \"value\": \"USA\" }," +
            "          { \"field\": \"type\", \"is\": \"null\" }" +
            "        ]" +
            "      }," +
            "      \"then\": { \"field\": \"tariff\", \"is\": \"null\" }," +
            "      \"else\": { \"not\": { \"field\": \"tariff\", \"is\": \"null\" } }" +
            "    }" +
            "  ]" +
            "}";

        // Act
        final ProfileDTO profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        assertEquals("0.1", profile.schemaVersion);
    }
}
