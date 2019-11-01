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

import com.google.inject.Inject;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.profile.guice.ProfileConfigSource;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SupportedVersionChecker implements SchemaVersionValidator {
    private SchemaVersionGetter schemaVersionGetter;
    private ProfileConfigSource configSource;
    private SupportedVersionsGetter supportedVersionsGetter;

    @Inject
    public SupportedVersionChecker(
        SchemaVersionGetter schemaVersionGetter,
        ProfileConfigSource configSource,
        SupportedVersionsGetter supportedVersionsGetter
    ) {
        this.schemaVersionGetter = schemaVersionGetter;
        this.configSource = configSource;
        this.supportedVersionsGetter = supportedVersionsGetter;
    }

    public URL getSchemaFile() throws IOException {
        String schemaVersion = schemaVersionGetter.getSchemaVersionOfJson(configSource.getProfileFile().toPath());
        validateSchemaVersion(schemaVersion);
        return this.getClass().getResource("/profileschema/datahelix.schema.json");
    }

    private void validateSchemaVersion(String schemaVersion) {
        List<String> supportedSchemaVersions = supportedVersionsGetter.getSupportedSchemaVersions();
        if (!supportedSchemaVersions.contains(schemaVersion)) {
            String errorMessage = "This version of the generator does not support v" +
                schemaVersion +
                " of the schema. Supported schema versions are " +
                supportedSchemaVersions;
            throw new ValidationException(errorMessage);
        }
    }
}
