package com.scottlogic.deg.schemas;

import com.scottlogic.deg.schemas.common.ProfileSerialiser;
import com.scottlogic.deg.schemas.v2.Field;
import com.scottlogic.deg.schemas.v2.V2Profile;
import com.scottlogic.deg.schemas.v2.constraints.Constraint;
import com.scottlogic.deg.schemas.v2.constraints.NumericRangeConstraint;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileSerialiserTests {
    @Test
    public void shouldSerialiseWithoutException() throws IOException {
        // Arrange
        final V2Profile profile = new V2Profile();

        final Field field = new Field();
        field.name = "price";

        final NumericRangeConstraint constraint = new NumericRangeConstraint();
        constraint.min = 2;
        constraint.max = 91;

        field.constraints = Arrays.asList((Constraint) constraint);
        profile.fields = Arrays.asList(field);

        final String expectedJson =
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
        final String json = new ProfileSerialiser().serialise(profile);

        // Assert
        Assert.assertThat(json, Is.is(expectedJson));
    }
}
