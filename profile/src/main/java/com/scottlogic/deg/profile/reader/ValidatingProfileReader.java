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
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.profile.guice.ProfileConfigSource;

import java.io.IOException;
import java.net.URL;

public class ValidatingProfileReader {

    private final ProfileConfigSource configSource;
    private final ConfigValidator configValidator;
    private final ProfileReader profileReader;
    private final ProfileSchemaLoader profileSchemaLoader;
    private final SchemaVersionValidator schemaVersionValidator;

    @Inject
    public ValidatingProfileReader(ProfileConfigSource configSource,
                                   ConfigValidator configValidator,
                                   ProfileReader profileReader,
                                   ProfileSchemaLoader profileSchemaLoader,
                                   SchemaVersionValidator schemaVersionValidator) {
        this.configSource = configSource;
        this.configValidator = configValidator;
        this.profileReader = profileReader;
        this.profileSchemaLoader = profileSchemaLoader;
        this.schemaVersionValidator = schemaVersionValidator;
    }

    public Profile read() throws IOException {
        configValidator.checkProfileInputFile();
        URL schema = schemaVersionValidator.getSchemaFile();
        profileSchemaLoader.validateProfile(configSource.getProfileFile(), schema);
        return profileReader.read();
    }
}