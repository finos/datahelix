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

package com.scottlogic.deg.profile.reader;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.commands.CommandBus;
import com.scottlogic.datahelix.generator.common.commands.CommandResult;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.deg.profile.dtos.ProfileDTO;
import com.scottlogic.deg.profile.commands.CreateProfile;
import com.scottlogic.deg.profile.serialisation.ProfileDeserialiser;
import com.scottlogic.deg.profile.validators.ConfigValidator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * JsonProfileReader is responsible for reading and validating a profile from a path to a profile JSON file.
 * It returns a Profile object for consumption by a generator
 */
public class JsonProfileReader implements ProfileReader {
    private final File profileFile;
    private final ConfigValidator configValidator;
    private final FileReader fileReader;
    private final CommandBus commandBus;

    @Inject
    public JsonProfileReader(@Named("config:profileFile") File profileFile, ConfigValidator configValidator, FileReader fileReader, CommandBus commandBus) {
        this.profileFile = profileFile;
        this.configValidator = configValidator;
        this.fileReader = fileReader;
        this.commandBus = commandBus;
    }

    public Profile read() throws IOException {
        configValidator.validate(profileFile);
        byte[] encoded = Files.readAllBytes(profileFile.toPath());
        String profileJson = new String(encoded, StandardCharsets.UTF_8);
        return read(profileJson);
    }

    public Profile read(String profileJson) {
        ProfileDTO profileDTO = ProfileDeserialiser.deserialise(profileJson, fileReader);
        CommandResult<Profile> createProfileResult = commandBus.send(new CreateProfile(profileDTO));
        if(!createProfileResult.isSuccess) throw new ValidationException(createProfileResult.errors);
        return createProfileResult.value;
    }
}
