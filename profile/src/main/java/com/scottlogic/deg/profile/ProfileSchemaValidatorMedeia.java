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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.common.ValidationException;
import com.worldturner.medeia.api.SchemaSource;
import com.worldturner.medeia.api.StringSchemaSource;
import com.worldturner.medeia.api.ValidationFailedException;
import com.worldturner.medeia.api.jackson.MedeiaJacksonApi;
import com.worldturner.medeia.schema.validation.SchemaValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileSchemaValidatorMedeia implements ProfileSchemaValidator {

    private static MedeiaJacksonApi api = new MedeiaJacksonApi();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void validateProfile(String profile, String schema) {
        List<String> errorMessages = new ArrayList<>();
        try {
            SchemaSource source = new StringSchemaSource(schema);
            SchemaValidator validator = api.loadSchema(source);

            JsonParser unvalidatedParser = objectMapper.getFactory().createParser(profile);
            JsonParser validatedParser = api.decorateJsonParser(validator, unvalidatedParser);
            api.parseAll(validatedParser);
        } catch (ValidationFailedException | IOException e) {
            errorMessages.add("Exception validating profile:" + e);
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
