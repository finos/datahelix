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

package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.orchestrator.cucumber.testframework.steps.DateValueStep;
import com.scottlogic.deg.orchestrator.cucumber.testframework.steps.TimeValueStep;
import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.deg.orchestrator.cucumber.testframework.steps.DateTimeValueStep;
import org.junit.Assert;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class GeneratorTestUtilities {
    private static final ObjectMapper mapper = createMapper();

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        return mapper;
    }

    public static Object parseInput(String input) throws JsonParseException {
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        } else if (input.matches(DateTimeValueStep.DATETIME_REGEX) || input.matches(DateValueStep.DATE_REGEX)) {
            return input;
        } else if (input.matches(TimeValueStep.TIME_REGEX)) {
            return input;
        } else if (input.equals("null")) {
            return null;
        } else if (input.matches("[+-]?(\\d+(\\.\\d+)?)")) {
            return parseNumber(input);
        } else if (input.equals("true") || input.equals("false")) {
            return input.equals("true");
        }

        throw new ValidationException(String.format("Unable to determine correct type for `%s`.\nEnsure strings are wrapped in double-quotes.", input));
    }

    public static Object parseNumber(String input) throws JsonParseException {
        try {
            return mapper.readerFor(Number.class).readValue(input);
        } catch (JsonParseException e) {
            throw e;
        } catch (IOException e) {
            Assert.fail("Unexpected IO exception " + e.toString());
            return "<unexpected IO exception>";
        }
    }

    public static Object parseExpected(String input) throws JsonParseException {
        if (input.matches(DateTimeValueStep.DATETIME_REGEX)) {
            return getOffsetDateTime(input);
        }
        return parseInput(input);
    }

    public static OffsetDateTime getOffsetDateTime(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return OffsetDateTime.parse(input, formatter);
    }
}
