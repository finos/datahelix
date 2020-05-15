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

package com.scottlogic.datahelix.generator.profile.reader;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.commands.CommandBus;
import com.scottlogic.datahelix.generator.common.commands.CommandResult;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.profile.commands.CreateProfile;
import com.scottlogic.datahelix.generator.profile.dtos.RelationalProfileDTO;
import com.scottlogic.datahelix.generator.profile.serialisation.ProfileDeserialiser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * JsonProfileReader is responsible for reading and validating a profile from a path to a profile JSON file.
 * It returns a Profile object for consumption by a generator
 */
public class JsonProfileReader implements ProfileReader {
    private final CommandBus commandBus;
    private final ProfileDeserialiser profileDeserialiser;

    @Inject
    public JsonProfileReader(
        CommandBus commandBus,
        ProfileDeserialiser profileDeserialiser) {
        this.commandBus = commandBus;
        this.profileDeserialiser = profileDeserialiser;
    }

    public Profile read(File profileFile) throws IOException {
        return createFromDto(
            Paths.get(profileFile.getParent()),
            profileDeserialiser.deserialise(profileFile));
    }

    public Profile read(Path profileDirectory, String profileJson) {
        RelationalProfileDTO profileDTO = profileDeserialiser.deserialise(profileDirectory, profileJson);
        return createFromDto(profileDirectory, profileDTO);
    }

    private Profile createFromDto(Path profileDirectory, RelationalProfileDTO profileDTO) {
        CommandResult<Profile> createProfileResult = commandBus.send(new CreateProfile(profileDirectory, profileDTO));

        if (!createProfileResult.isSuccess){
            throw new ValidationException(createProfileResult.errors);
        }

        return createProfileResult.value;
    }
}
