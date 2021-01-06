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

package com.scottlogic.datahelix.generator.profile.serialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.ProfileDTO;
import com.scottlogic.datahelix.generator.profile.services.FieldService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static com.scottlogic.datahelix.generator.profile.creation.FieldDTOBuilder.fieldDTO;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;


public class FieldDeserialiserTests
{
    @Test
    public void shouldDeserialiseIntegerTypeWithoutException() throws IOException
    {
        // Arrange
        final String json = "{ \"name\": \"id\", \"type\": \"integer\" }";

        // Act
        FieldDTO actual = deserialiseJsonString(json);

        // Assert
        FieldDTO expected = fieldDTO("id", StandardSpecificFieldType.INTEGER).build();

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseStringTypeWithoutException() throws IOException
    {
        // Arrange
        final String json = "{ \"name\": \"country\", \"type\": \"string\" }\"";

        // Act
        FieldDTO actual = deserialiseJsonString(json);

        // Assert
        FieldDTO expected = fieldDTO("country", StandardSpecificFieldType.STRING).build();

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseDecimalTypeWithoutException() throws IOException
    {
        // Arrange
        final String json = "{ \"name\": \"tariff\", \"type\": \"decimal\" }\"";

        // Act
        FieldDTO actual = deserialiseJsonString(json);

        // Assert
        FieldDTO expected = fieldDTO("tariff", StandardSpecificFieldType.DECIMAL).build();

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseDateTimeTypeWithoutException() throws IOException
    {
        // Arrange
        final String json = "{ \"name\": \"time\", \"type\": \"datetime\" }\"";

        // Act
        FieldDTO actual = deserialiseJsonString(json);

        // Assert
        FieldDTO expected = fieldDTO("time", StandardSpecificFieldType.DATETIME).build();

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseFirstNameTypeWithoutException() throws IOException
    {
        // Arrange
        final String json = "{ \"name\": \"first_name\", \"type\": \"firstname\" }\"";

        // Act
        FieldDTO actual = deserialiseJsonString(json);

        // Assert
        FieldDTO expected = fieldDTO("first_name", StandardSpecificFieldType.FIRST_NAME).build();

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseLastNameTypeWithoutException() throws IOException
    {
        // Arrange
        final String json = "{ \"name\": \"last_name\", \"type\": \"lastname\" }\"";

        // Act
        FieldDTO actual = deserialiseJsonString(json);

        // Assert
        FieldDTO expected = fieldDTO("last_name", StandardSpecificFieldType.LAST_NAME).build();

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseFullNameTypeWithoutException() throws IOException
    {
        // Arrange
        final String json = "{ \"name\": \"full_name\", \"type\": \"fullname\" }\"";

        // Act
        FieldDTO actual = deserialiseJsonString(json);

        // Assert
        FieldDTO expected = fieldDTO("full_name", StandardSpecificFieldType.FULL_NAME).build();

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseFieldAndThrowInvalidFieldNameException()
    {
        // Arrange
        final String json = "{ \"name\": \"id\", \"tpe\": \"integer\" }";
        // Assert
        Assertions.assertThrows(UnrecognizedPropertyException.class, () -> deserialiseJsonString(json));

    }

    @Test
    public void shouldDeserialiseFieldAndThrowInvalidTypeValueException() throws IOException
    {
        // Arrange
        final String json = "{ \"name\": \"id\", \"type\": \"intger\" }";
        FieldDTO fieldDTO = deserialiseJsonString(json);

        FieldService service = new FieldService();
        ProfileDTO dto = new ProfileDTO();
        dto.fields = Arrays.asList(fieldDTO);
        dto.constraints = Collections.emptyList();

        Assertions.assertThrows(IllegalStateException.class, () -> service.createFields(dto));

    }

    private FieldDTO deserialiseJsonString(String json) throws IOException
    {
        return new ObjectMapper().readerFor(FieldDTO.class).readValue(json);
    }
}
