package com.scottlogic.deg.schemas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.scottlogic.deg.schemas.common.BaseProfile;
import com.scottlogic.deg.schemas.common.ProfileDeserialiser;
import com.scottlogic.deg.schemas.v3.V3ProfileDTO;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ProfileDeserialiserTests {
    @Test
    public void shouldDeserialiseV3ProfileWithoutException() throws IOException {
        // Arrange
        final String json = "{" +
            "  \"schemaVersion\" : \"v3\"," +
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
            "    { \"field\": \"id\", \"is\": \"ofType\", \"value\": \"temporal\" }," +
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
            "      \"then_\": { \"field\": \"tariff\", \"is\": \"null\" }," +
            "      \"else\": { \"not\": { \"field\": \"tariff\", \"is\": \"null\" } }" +
            "    }" +
            "  ]" +
            "}";

        // Act
        final BaseProfile profile = new ProfileDeserialiser().deserialise(json, V3ProfileDTO.SchemaVersion);

        // Assert
        assertEquals("v3", profile.schemaVersion);
    }
}
