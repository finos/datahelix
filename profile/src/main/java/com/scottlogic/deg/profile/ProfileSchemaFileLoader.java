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

import com.scottlogic.deg.common.ValidationException;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to validate a DataHelix Profile JSON file.
 * <p>
 * Checks that the profile JSON file is valid against the DataHelix Profile Schema (datahelix.schema.json)
 */
public class ProfileSchemaFileLoader implements ProfileSchemaLoader {
    private final ProfileSchemaValidator validator;

    public ProfileSchemaFileLoader(ProfileSchemaValidator validator) {
        this.validator = validator;
    }

    @Override
    public void validateProfile(File profileFile, URL schemaUrl) {
        if (schemaUrl == null) {
            throw new ValidationException("Null Schema");
        }

        try {
            String profile = readProfile(profileFile);
            String schema = readSchema(schemaUrl);
            validator.validateProfile(profile, schema);
        } catch (IOException e) {
            throw new ValidationException(e.getClass() + " when looking for schema with URL " + schemaUrl);
        }
    }

    private String readProfile(File profileFile) throws IOException {
        byte[] data = Files.readAllBytes(profileFile.toPath());
        return readAllLines(data).stream().collect(Collectors.joining(System.lineSeparator()));
    }

    private String readSchema(URL schemaUrl) throws IOException {
        String schema;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(schemaUrl.openStream()))) {
            schema = br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        return schema;
    }

    private List<String> readAllLines(byte[] data) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
        List<String> lines = new ArrayList<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }
}
