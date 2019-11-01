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

package com.scottlogic.deg.profile.creation.serialisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.profile.creation.dtos.ProfileDTO;

import java.io.IOException;
import java.io.UncheckedIOException;

public class ProfileSerialiser
{
    public String serialise(ProfileDTO profile)
    {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(profile);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

