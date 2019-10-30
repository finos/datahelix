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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.profile.dtos.SchemaDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SchemaVersionGetter {
    public String getSchemaVersionOfJson(Path filePath) throws IOException {
        byte[] encoded = Files.readAllBytes(filePath);
        String profileJson = new String(encoded, StandardCharsets.UTF_8);

        return this.getSchemaVersionOfJson(profileJson);
    }

    private String getSchemaVersionOfJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        SchemaDto schemaDto = mapper.readerFor(SchemaDto.class).readValue(json);
        return schemaDto.schemaVersion;
    }
}

