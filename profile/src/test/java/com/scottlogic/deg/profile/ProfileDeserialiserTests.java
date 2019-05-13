package com.scottlogic.deg.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.scottlogic.deg.profile.common.BaseProfile;
import com.scottlogic.deg.profile.common.ProfileDeserialiser;
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
        final BaseProfile profile = new ProfileDeserialiser().deserialise(json, ProfileDTO.SchemaVersion);

        // Assert
        assertEquals("0.1", profile.schemaVersion);
    }
}
