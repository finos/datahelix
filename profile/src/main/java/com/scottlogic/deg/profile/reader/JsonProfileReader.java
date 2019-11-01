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
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.profile.creation.dtos.ProfileDTO;
import com.scottlogic.deg.profile.creation.commands.CreateProfile;
import com.scottlogic.deg.profile.creation.serialisation.ProfileDeserialiser;

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
    private final CommandBus commandBus;

    @Inject
    public JsonProfileReader(@Named("config:profileFile") File profileFile, CommandBus commandBus) {
        this.profileFile = profileFile;
        this.commandBus = commandBus;
    }

    public Profile read() throws IOException {
        byte[] encoded = Files.readAllBytes(profileFile.toPath());
        String profileJson = new String(encoded, StandardCharsets.UTF_8);
        return read(profileJson);
    }

    public Profile read(String profileJson) {
        ProfileDTO profileDTO = new ProfileDeserialiser().deserialise(profileJson);
        CommandResult<Profile> createProfileResult = commandBus.send(new CreateProfile(profileDTO));
        if(!createProfileResult.isSuccess) throw new ValidationException(createProfileResult.errors);
        return createProfileResult.value;
    }
}
