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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.scottlogic.deg.profile.creation.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.InvalidConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.NotConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.EqualToConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.IsNullConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.numeric.GreaterThanConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.numeric.LessThanConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.ConditionalConstraintDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

public class GrammaticalConstraintDeserialiserTests {
    @Test
    public void shouldDeserialiseAnyOfWithoutException() throws IOException {
        // Arrange
        final String json =
            "{ \"anyOf\": [" +
            "    { \"field\": \"foo\", \"equalTo\": \"0\" }," +
            "    { \"field\": \"foo\", \"isNull\": \"true\" }" +
            "  ]" +
            "}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        EqualToConstraintDTO expectedEqualsTo = new EqualToConstraintDTO();
        expectedEqualsTo.field = "foo";
        expectedEqualsTo.value = "0";
        IsNullConstraintDTO expectedNull = new IsNullConstraintDTO();
        expectedNull.field = "foo";
        expectedNull.isNull = true;

        AnyOfConstraintDTO expected = new AnyOfConstraintDTO();
        expected.constraints = Arrays.asList(expectedEqualsTo, expectedNull);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseAnyOfAndThrowInvalidFieldException() throws IOException {
        // Arrange
        final String json =
            "{ \"anyOf\": [" +
            "    { \"fild\": \"foo\", \"equalTo\": \"0\" }," +
            "    { \"field\": \"foo\", \"isNull\": \"true\" }" +
            "  ]" +
            "}";

        try {
            deserialiseJsonString(json);
            Assert.fail("should have thrown an exception");
        } catch (UnrecognizedPropertyException e)
        {
            String expectedMessage = "Unrecognized field \"fild\" (class com.scottlogic.deg.profile.creation.dtos.constraints.atomic.EqualToConstraintDTO), not marked as ignorable (2 known properties: \"field\", \"equalTo\"])\n at [Source: UNKNOWN; line: -1, column: -1] (through reference chain: com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.AnyOfConstraintDTO[\"anyOf\"]->java.util.ArrayList[0]->com.scottlogic.deg.profile.creation.dtos.constraints.atomic.EqualToConstraintDTO[\"fild\"])";
            assertThat(e.getMessage(), sameBeanAs(expectedMessage));
        }
    }

    @Test
    public void shouldDeserialiseAnyOfAndReturnInvalidConstraint() throws IOException {
        // Arrange
        final String json =
            "{ \"ayOf\": [" +
            "    { \"field\": \"foo\", \"equalTo\": \"0\" }," +
            "    { \"field\": \"foo\", \"isNull\": \"true\" }" +
            "  ]" +
            "}";

        ConstraintDTO constraintDTO = deserialiseJsonString(json);
        Assert.assertTrue(constraintDTO instanceof InvalidConstraintDTO);
    }

    @Test
    public void shouldDeserialiseAllOfWithoutException() throws IOException {
        // Arrange
        final String json =
            "{ \"allOf\": [" +
            "    { \"field\": \"foo\", \"greaterThan\": \"0\" }," +
            "    { \"field\": \"foo\", \"lessThan\": \"100\" }" +
            "  ]" +
            "}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        GreaterThanConstraintDTO expectedGreaterThan = new GreaterThanConstraintDTO();
        expectedGreaterThan.field = "foo";
        expectedGreaterThan.value = 0;
        LessThanConstraintDTO expectedLessThan = new LessThanConstraintDTO();
        expectedLessThan.field = "foo";
        expectedLessThan.value = 100;

        AllOfConstraintDTO expected = new AllOfConstraintDTO();
        expected.constraints = Arrays.asList(expectedGreaterThan, expectedLessThan);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseNotWithoutException() throws IOException {
        // Arrange
        final String json = "{ \"not\": { \"field\": \"foo\", \"isNull\": \"true\" } }";

        // Act
       ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        IsNullConstraintDTO expectedNull = new IsNullConstraintDTO();
        expectedNull.field = "foo";
        expectedNull.isNull = true;

        NotConstraintDTO expected = new NotConstraintDTO();
        expected.constraint = expectedNull;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseIfWithoutException() throws IOException {
        // Arrange
        final String json =
            "{ \"if\":{ \"field\": \"foo\", \"lessThan\": \"100\" }," +
            " \"then\":{ \"field\": \"bar\", \"greaterThan\": \"0\" }," +
            " \"else\":{ \"field\": \"bar\", \"equalTo\": \"500\" }}";

        // Act
        ConstraintDTO actual = deserialiseJsonString(json);

        // Assert
        LessThanConstraintDTO expectedLessThan = new LessThanConstraintDTO();
        expectedLessThan.field = "foo";
        expectedLessThan.value = 100;
        GreaterThanConstraintDTO expectedGreaterThan = new GreaterThanConstraintDTO();
        expectedGreaterThan.field = "bar";
        expectedGreaterThan.value = 0;
        EqualToConstraintDTO expectedEqualTo = new EqualToConstraintDTO();
        expectedEqualTo.field = "bar";
        expectedEqualTo.value = "500";

        ConditionalConstraintDTO expected= new ConditionalConstraintDTO();

        expected.ifConstraint = expectedLessThan;
        expected.thenConstraint = expectedGreaterThan;
        expected.elseConstraint = expectedEqualTo;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldDeserialiseIfAndThrowMissingColonException() throws IOException {
        // Arrange
        final String json =
            "{ \"if\"{ \"field\": \"foo\", \"lessThan\": \"100\" }," +
                " \"then\"{ \"field\": \"bar\", \"greaterThan\": \"0\" }," +
                " \"else\"{ \"field\": \"bar\", \"equalTo\": \"500\" }}";

        try {
            deserialiseJsonString(json);
            Assert.fail("should have thrown an exception");
        } catch (JsonParseException e) {
            String expectedMessage = "Unexpected character ('{' (code 123)): was expecting a colon to separate field name and value\n at [Source: (String)\"{ \"if\"{ \"field\": \"foo\", \"lessThan\": \"100\" }, \"then\"{ \"field\": \"bar\", \"greaterThan\": \"0\" }, \"else\"{ \"field\": \"bar\", \"equalTo\": \"500\" }}\"; line: 1, column: 8]";
            assertThat(e.getMessage(), sameBeanAs(expectedMessage));
        }
    }

    private ConstraintDTO deserialiseJsonString(String json) throws IOException {
        return new ObjectMapper().readerFor(ConstraintDTO.class).readValue(json);
    }
}
