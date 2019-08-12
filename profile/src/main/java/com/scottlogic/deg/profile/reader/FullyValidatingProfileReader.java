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
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.profile.reader.validation.ConfigValidator;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;
import com.scottlogic.deg.profile.v0_1.SchemaVersionValidator;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FullyValidatingProfileReader implements ValidatingProfileReader {
    private final File profile;
    private final ConfigValidator configValidator;
    private final ProfileReader profileReader;
    private final ProfileSchemaValidator profileSchemaValidator;
    private final SchemaVersionValidator schemaVersionValidator;

    @Inject
    public FullyValidatingProfileReader(@Named("config:profileFile") File profile,
                                        ConfigValidator configValidator,
                                        ProfileReader profileReader,
                                        ProfileSchemaValidator profileSchemaValidator,
                                        SchemaVersionValidator schemaVersionValidator) {
        this.profile = profile;
        this.configValidator = configValidator;
        this.profileReader = profileReader;
        this.profileSchemaValidator = profileSchemaValidator;
        this.schemaVersionValidator = schemaVersionValidator;
    }

    @Override
    public Profile read() throws IOException {
        configValidator.checkProfileInputFile();
        URL schema = schemaVersionValidator.getSchemaFile();
        profileSchemaValidator.validateProfile(profile, schema);
        return profileReader.read();
    }
}