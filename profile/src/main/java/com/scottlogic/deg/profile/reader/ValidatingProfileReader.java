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
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.profile.guice.ProfileConfigSource;
import com.scottlogic.deg.profile.reader.validation.ConfigValidator;
import com.scottlogic.deg.profile.dtos.ProfileSchemaLoader;
import com.scottlogic.deg.profile.dtos.SchemaVersionValidator;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

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

        Profile profile = profileReader.read();

        validateFieldsAreTyped(profile.getFields());
        return profile;
    }

    private void validateFieldsAreTyped(Fields fields) {
        List<Field> untyped = fields.stream().filter(field -> field.getType() == null).collect(Collectors.toList());
        if (untyped.size() == 1) {
            throw new InvalidProfileException("Field [" + untyped.get(0).name + "]: is not typed; add its type to the field definition");
        }
        if (!untyped.isEmpty()) {
            throw new InvalidProfileException("Fields "
                + untyped.stream().map(f -> f.name).reduce((s, z) -> s + ", " + z).get()
                + " are not typed; add their type to the field definition");
        }
    }
}