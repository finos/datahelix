package com.scottlogic.deg.schemas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.scottlogic.deg.schemas.common.BaseProfile;
import com.scottlogic.deg.schemas.common.ProfileDeserialiser;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ProfileDeserialiserTests {
    @Test
    public void shouldDeserialiseWithoutException() throws IOException {
        // Arrange
        final String json =
            "{\n" +
            "  \"schemaVersion\" : \"v2\",\n" +
            "  \"fields\" : [ {\n" +
            "    \"name\" : \"price\",\n" +
            "    \"format\" : null,\n" +
            "    \"constraints\" : [ {\n" +
            "      \"type\" : \"NumericRange\",\n" +
            "      \"min\" : 2,\n" +
            "      \"max\" : 91\n" +
            "    } ],\n" +
            "    \"trends\" : null\n" +
            "  } ]\n" +
            "}";

        // Act
        final BaseProfile profile = new ProfileDeserialiser().deserialise(json);

        // Assert
        assertEquals("v2", profile.schemaVersion);
    }
}
