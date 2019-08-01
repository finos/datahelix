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
package com.scottlogic.deg.profile.v0_1;

import com.scottlogic.deg.common.ValidationException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SchemaVersionValidator {
    private String directoryContainingSchemas;
    public SchemaVersionValidator(String directoryContainingSchemas) {
        this.directoryContainingSchemas = directoryContainingSchemas;
    }

    public URL getSchemaFile(String schemaVersion) throws MalformedURLException {
        validateSchemaVersion(schemaVersion);
        String protocol = "file://";
        return new URL(protocol + directoryContainingSchemas + schemaVersion + "/datahelix.schema.json");
    }

    private void validateSchemaVersion(String schemaVersion) {
        List<String> supportedSchemaVersions = getSupportedSchemaVersions();
        if (!supportedSchemaVersions.contains(schemaVersion)) {
            String errorMessage = "This version of the generator does not support v" +
                schemaVersion +
                " of the schema. Supported schema versions are " +
                supportedSchemaVersions;
            throw new ValidationException(errorMessage);
        }
    }

    private List<String> getSupportedSchemaVersions() {
        File file = new File(directoryContainingSchemas);
        String[] directoriesArray = file.list((current, name) -> new File(current, name).isDirectory());
        List<String> directories = new ArrayList<>();
        if (directoriesArray != null) {
            directories = Arrays.asList(directoriesArray);
        }
        return directories;
    }
}
