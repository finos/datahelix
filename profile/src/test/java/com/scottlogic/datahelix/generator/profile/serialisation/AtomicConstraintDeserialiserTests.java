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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.scottlogic.datahelix.generator.common.whitelist.WeightedElement;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.EqualToConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.GranularToConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.InSetConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.IsNullConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.integer.LongerThanConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.integer.OfLengthConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.integer.ShorterThanConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.numeric.*;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.temporal.AfterConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.temporal.AfterOrAtConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.temporal.BeforeConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.temporal.BeforeOrAtConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.textual.ContainsRegexConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.textual.MatchesRegexConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.*;
import com.scottlogic.datahelix.generator.profile.reader.FileReader;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

public class AtomicConstraintDeserialiserTests {
    @Rule
    public ExpectedException expectValidationException = ExpectedException.none();

    @Test
    public void shouldDeserialiseEqualToWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"type\", \"equalTo\": \"X_092\" }";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        EqualToConstraintDTO expected = new EqualToConstraintDTO();
        expected.field = "type";
        expected.value = "X_092";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseEqualToAndThrowInvalidConstraintValueException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"type\", \"equalTo\": }";

        try {
            deserialiseJsonString(json);
            Assert.fail("should have thrown an exception");
        } catch (JsonParseException e) {
            String expectedMessage = "Unexpected character ('}' (code 125)): expected a valid value (JSON String, Number, Array, Object or token 'null', 'true' or 'false')\n at [Source: (String)\"{\"field\": \"type\", \"equalTo\": }\"; line: 1, column: 31]";
            assertThat(e.getMessage(), sameBeanAs(expectedMessage));
        }
    }

    @Test
    public void shouldDeserialiseInSetWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"type\", \"inSet\": [ \"X_092\",\"0001-01-01T00:00:00.000Z\" ]}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        InSetConstraintDTO expected = new InSetConstraintDTO();
        expected.field = "type";
        expected.values = Arrays.asList("X_092", "0001-01-01T00:00:00.000Z");

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseInSetCsvFileWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"country\", \"inSet\": \"countries.csv\" }";
        // Act
        ConstraintDTO actual = deserialiseJsonString(new TestFileReader(), json);

        // Assert
        InSetConstraintDTO expected = new InSetConstraintDTO();
        expected.field = "country";
        expected.values = Collections.singletonList(new WeightedElement<>("test", 1.0));

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseWeightedInSetCsvFile() throws IOException {
        // Arrange
        final String json = "{\"field\": \"country\", \"inSet\": \"countries.csv\" }";
        // Act
        ConstraintDTO actual = deserialiseJsonString(new TestFileReader(true), json);

        // Assert
        InSetConstraintDTO expected = new InSetConstraintDTO();
        expected.field = "country";
        expected.values = Arrays.asList(
            new WeightedElement<>("test1", 20.0),
            new WeightedElement<>("test2", 80.0)
        );

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseInMapWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"country\", \"inMap\": \"countries.csv\", \"key\": \"Country\" }";

        // Act
        ConstraintDTO actual = deserialiseJsonString(new TestFileReader(), json);

        // Assert
        InMapConstraintDTO expected = new InMapConstraintDTO();
        expected.field = "country";
        expected.otherField = "countries.csv";
        expected.values = Collections.singletonList("test");

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseNullWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"country\",\"isNull\": \"true\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        IsNullConstraintDTO expected = new IsNullConstraintDTO();
        expected.field = "country";
        expected.isNull = true;

        assertThat(actual, sameBeanAs(expected));
    }

    //string
    @Test
    public void shouldDeserialiseMatchingRegexWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"name\", \"matchingRegex\": \"[a-z]{0, 10}\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        MatchesRegexConstraintDTO expected = new MatchesRegexConstraintDTO();
        expected.field = "name";
        expected.value = "[a-z]{0, 10}";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseContainingRegexWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"field\": \"name\", \"containingRegex\": \"[a-z]{0, 10}\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        ContainsRegexConstraintDTO expected = new ContainsRegexConstraintDTO();
        expected.field = "name";
        expected.value = "[a-z]{0, 10}";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseOfLengthWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"field\": \"name\", \"ofLength\": \"5\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        OfLengthConstraintDTO expected = new OfLengthConstraintDTO();
        expected.field = "name";
        expected.value = 5;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseLongerThanWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"field\": \"name\", \"longerThan\": \"3\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        LongerThanConstraintDTO expected = new LongerThanConstraintDTO();
        expected.field = "name";
        expected.value = 3;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseShorterThanWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"name\", \"shorterThan\": \"3\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        ShorterThanConstraintDTO expected = new ShorterThanConstraintDTO();
        expected.field = "name";
        expected.value = 3;

        assertThat(actual, sameBeanAs(expected));
    }

    //Numeric
    @Test
    public void shouldDeserialiseGreaterThanWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"greaterThan\": \"0\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GreaterThanConstraintDTO expected = new GreaterThanConstraintDTO();
        expected.field = "price";
        expected.value = 0;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseGreaterThanOrEqualToWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"greaterThanOrEqualTo\": \"0\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GreaterThanOrEqualToConstraintDTO expected = new GreaterThanOrEqualToConstraintDTO();
        expected.field = "price";
        expected.value = 0;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseGreaterThanOrEqualToFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"greaterThanOrEqualToField\": \"discountPrice\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GreaterThanOrEqualToFieldConstraintDTO expected = new GreaterThanOrEqualToFieldConstraintDTO();
        expected.field = "price";
        expected.otherField = "discountPrice";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseGreaterThanOrEqualToFieldWithOffsetWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"greaterThanOrEqualToField\": \"discountPrice\", \"offset\": 3, \"offsetUnit\": \"days\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GreaterThanOrEqualToFieldConstraintDTO expected = new GreaterThanOrEqualToFieldConstraintDTO();
        expected.field = "price";
        expected.otherField = "discountPrice";
        expected.offset = 3;
        expected.offsetUnit = "days";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseLessThanWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"field\": \"price\", \"lessThan\": \"0\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        LessThanConstraintDTO expected = new LessThanConstraintDTO();
        expected.field = "price";
        expected.value = 0;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseLessThanOrEqualToWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"lessThanOrEqualTo\": \"0\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        LessThanOrEqualToConstraintDTO expected = new LessThanOrEqualToConstraintDTO();
        expected.field = "price";
        expected.value = 0;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseLessThanOrEqualToFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"lessThanOrEqualToField\": \"discountPrice\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        LessThanOrEqualToFieldConstraintDTO expected = new LessThanOrEqualToFieldConstraintDTO();
        expected.field = "price";
        expected.otherField = "discountPrice";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseNumericGranularToWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"price\", \"granularTo\": \"0.1\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GranularToConstraintDTO expected = new GranularToConstraintDTO();
        expected.field = "price";
        expected.value = "0.1";

        assertThat(actual, sameBeanAs(expected));
    }

    //Datetime

    @Test
    public void shouldDeserialiseAfterWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"field\": \"date\", \"after\": \"2018-09-01T00:00:00.000\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        AfterConstraintDTO expected = new AfterConstraintDTO();
        expected.field = "date";
        expected.value = "2018-09-01T00:00:00.000";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseAfterOrAtWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"date\", \"afterOrAt\": \"2018-09-01T00:00:00.000\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        AfterOrAtConstraintDTO expected = new AfterOrAtConstraintDTO();
        expected.field = "date";
        expected.value = "2018-09-01T00:00:00.000";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseBeforeWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"date\", \"before\": \"2018-09-01T00:00:00.000\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        BeforeConstraintDTO expected = new BeforeConstraintDTO();
        expected.field = "date";
        expected.value = "2018-09-01T00:00:00.000";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseBeforeOrAtWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"date\", \"beforeOrAt\": \"2018-09-01T00:00:00.000\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        BeforeOrAtConstraintDTO expected = new BeforeOrAtConstraintDTO();
        expected.field = "date";
        expected.value = "2018-09-01T00:00:00.000";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseDatetimeGranularToWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"date\", \"granularTo\": \"days\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GranularToConstraintDTO expected = new GranularToConstraintDTO();
        expected.field = "date";
        expected.value = "days";

        assertThat(actual, sameBeanAs(expected));
    }

    //Dependant fields

    @Test
    public void shouldDeserialiseAfterFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"laterDateField\", \"afterField\":\"previousDateField\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        AfterFieldConstraintDTO expected = new AfterFieldConstraintDTO();
        expected.field = "laterDateField";
        expected.otherField = "previousDateField";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseAfterOrAtFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"laterDateField\", \"afterOrAtField\":\"previousDateField\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        AfterOrAtFieldConstraintDTO expected = new AfterOrAtFieldConstraintDTO();
        expected.field = "laterDateField";
        expected.otherField = "previousDateField";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseBeforeFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"laterDateField\", \"beforeField\":\"previousDateField\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        BeforeFieldConstraintDTO expected = new BeforeFieldConstraintDTO();
        expected.field = "laterDateField";
        expected.otherField = "previousDateField";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseBeforeOrAtFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"laterDateField\", \"beforeOrAtField\":\"previousDateField\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        BeforeOrAtFieldConstraintDTO expected = new BeforeOrAtFieldConstraintDTO();
        expected.field = "laterDateField";
        expected.otherField = "previousDateField";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseEqualToFieldWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"laterDateField\", \"equalToField\":\"previousDateField\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        EqualToFieldConstraintDTO expected = new EqualToFieldConstraintDTO();
        expected.field = "laterDateField";
        expected.otherField = "previousDateField";

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseEqualToWithOffsetWithoutException() throws IOException {
        // Arrange
        final String json = "{\"field\": \"threeDaysAfterField\", \"equalToField\":\"previousDateField\", \"offset\": 3, \"offsetUnit\": \"days\"}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        EqualToFieldConstraintDTO expected = new EqualToFieldConstraintDTO();
        expected.field = "threeDaysAfterField";
        expected.otherField = "previousDateField";
        expected.offset = 3;
        expected.offsetUnit = "days";

        assertThat(actual, sameBeanAs(expected));
    }

    private ConstraintDTO deserialiseJsonString(String json) throws IOException {
        return deserialiseJsonString(null, json);
    }

    private ConstraintDTO deserialiseJsonString(FileReader fileReader, String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(ConstraintDTO.class, new ConstraintDeserializer(fileReader, Paths.get("test")));
        mapper.registerModule(module);

        return mapper
            .readerFor(ConstraintDTO.class)
            .readValue(json);
    }
}
