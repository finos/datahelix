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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.profile.dtos.ProfileDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.datahelix.generator.profile.validators.ConfigValidator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProfileDeserialiser
{
    private final ConfigValidator configValidator;
    private final ConstraintDeserializerFactory constraintDeserializerFactory;

    @Inject
    public ProfileDeserialiser(ConfigValidator configValidator, ConstraintDeserializerFactory constraintDeserializerFactory) {
        this.configValidator = configValidator;
        this.constraintDeserializerFactory = constraintDeserializerFactory;
    }

    public ProfileDTO deserialise(File profileFile) throws IOException {
        configValidator.validate(profileFile);
        byte[] encoded = Files.readAllBytes(profileFile.toPath());
        String profileJson = new String(encoded, StandardCharsets.UTF_8);
        return deserialise(profileFile.getParentFile().toPath(), profileJson);
    }

    public ProfileDTO deserialise(Path profileDirectory, String json) {
        if (profileDirectory == null) {
            throw new IllegalArgumentException("profileDirectory must be supplied");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.WRAP_EXCEPTIONS);
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS); //TODO: Remove this, only here for the PoC

        SimpleModule module = new SimpleModule();
        module.addDeserializer(ConstraintDTO.class, constraintDeserializerFactory.createDeserialiser(profileDirectory));
        mapper.registerModule(module);

        try {
            return mapper
                .readerFor(ProfileDTO.class)
                .readValue(json);
        } catch (Exception e) {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));

            throw new ValidationException("Error loading profile\n" + e.getMessage() + "\n" + stackTrace.toString());
        }
    }
}

