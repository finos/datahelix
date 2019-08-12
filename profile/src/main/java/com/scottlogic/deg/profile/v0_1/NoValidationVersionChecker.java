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

import com.google.inject.Inject;
import com.scottlogic.deg.profile.guice.ProfileConfigSource;
import com.scottlogic.deg.profile.serialisation.SchemaVersionRetriever;

import java.io.IOException;
import java.net.URL;

public class NoValidationVersionChecker implements SchemaVersionValidator {
    private final SchemaVersionRetriever schemaVersionRetriever;
    private final ProfileConfigSource configSource;
    @Inject
    public NoValidationVersionChecker(SchemaVersionRetriever schemaVersionRetriever, ProfileConfigSource configSource) {
        this.schemaVersionRetriever = schemaVersionRetriever;
        this.configSource = configSource;
    }

    /**
     * @return URL of schema file that matches the input profile's schemaVersion
     * @throws IOException
     */
    @Override
    public URL getSchemaFile() throws IOException {
        String schemaVersion = schemaVersionRetriever.getSchemaVersionOfJson(configSource.getProfileFile().toPath());
        return this.getClass().getResource(
            "/profileschema/" +
            schemaVersion +
            "/datahelix.schema.json");
    }
}
