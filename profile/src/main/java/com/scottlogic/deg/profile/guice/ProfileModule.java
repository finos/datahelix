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

package com.scottlogic.deg.profile.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.scottlogic.deg.profile.*;
import com.scottlogic.deg.profile.reader.JsonProfileReader;
import com.scottlogic.deg.profile.reader.ProfileReader;

import java.io.File;

public class ProfileModule extends AbstractModule {

    private final ProfileConfigSource profileConfigSource;

    public ProfileModule(ProfileConfigSource profileConfigSource) {
        this.profileConfigSource = profileConfigSource;
    }

    @Override
    protected void configure() {
        // Bind command line to correct implementation
        bind(ProfileConfigSource.class).toInstance(profileConfigSource);

        bind(ProfileSchemaValidator.class).to(ProfileSchemaValidatorLeadPony.class);
        bind(ProfileSchemaLoader.class).toProvider(ProfileSchemaLoaderProvider.class);
        bind(SchemaVersionValidator.class).to(SupportedVersionChecker.class);

        bind(ProfileReader.class).to(JsonProfileReader.class);

        bind(File.class)
            .annotatedWith(Names.named("config:profileFile"))
            .toInstance(profileConfigSource.getProfileFile());
        bind(String.class)
            .annotatedWith(Names.named("config:filePath"))
            .toInstance(profileConfigSource.fromFilePath());
    }
}
