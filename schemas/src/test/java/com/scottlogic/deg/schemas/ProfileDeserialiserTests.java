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
            "    { \"name\": \"time\" }," +
            "    { \"name\": \"country\" }," +
            "    { \"name\": \"tariff\" }," +
            "    { \"name\": \"low_price\" }," +
            "    { \"name\": \"high_price\" }" +
            "  ]," +
            "  \"rules\": [" +
            "    { \"field\": \"id\", \"type\": \"isOfType\", \"value\": \"temporal\" }," +
            "    { \"field\": \"id\", \"type\": \"not isNull\" }," +

            "    { \"field\": \"low_price\", \"type\": \"isOfType\", \"value\": \"numeric\" }," +
            "    { \"field\": \"low_price\", \"type\": \"not isNull\" }," +
            "    { \"field\": \"low_price\", \"type\": \"isGreaterThanOrEqual\", \"value\": \"0\" }," +

            "    {" +
            "      \"rule\": \"Some rule\"," +
            "      \"constraints\": [" +
            "        { \"field\": \"country\", \"type\": \"isInSet\", \"values\": [ \"USA\", \"GB\", \"FRANCE\" ] }" +
            "      ]" +
            "    }," +

            "    {" +
            "      \"type\": \"if\"," +
            "      \"condition\": {" +
            "        \"type\": \"or\"," +
            "        \"constraints\": [" +
            "          { \"field\": \"type\", \"type\": \"isEqualTo\", \"value\": \"USA\" }," +
            "          { \"field\": \"type\", \"type\": \"isNull\" }" +
            "        ]" +
            "      }," +
            "      \"then\": { \"field\": \"tariff\", \"type\": \"isNull\" }," +
            "      \"else\": { \"field\": \"tariff\", \"type\": \"not isNull\" }" +
            "    }" +
            "  ]" +
            "}";

        // Act
        final BaseProfile profile = new ProfileDeserialiser().deserialise(json, V3ProfileDTO.SchemaVersion);

        // Assert
        assertEquals("v3", profile.schemaVersion);
    }
}
