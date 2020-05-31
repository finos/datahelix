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

package com.scottlogic.datahelix.generator.profile.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.scottlogic.datahelix.generator.common.commands.CommandBus;
import com.scottlogic.datahelix.generator.common.validators.Validator;
import com.scottlogic.datahelix.generator.profile.commands.CreateProfile;
import com.scottlogic.datahelix.generator.profile.commands.ReadRelationships;
import com.scottlogic.datahelix.generator.profile.dtos.ProfileDTO;
import com.scottlogic.datahelix.generator.profile.validators.CreateProfileValidator;
import com.scottlogic.datahelix.generator.profile.validators.ReadRelationshipsValidator;
import com.scottlogic.datahelix.generator.profile.validators.profile.ProfileValidator;
import com.scottlogic.datahelix.generator.profile.reader.*;

import java.io.File;

public class ProfileModule extends AbstractModule {
    private final ProfileConfigSource profileConfigSource;

    public ProfileModule(ProfileConfigSource profileConfigSource)
    {
        this.profileConfigSource = profileConfigSource;
    }

    @Override
    protected void configure() {
        // Bind command line to correct implementation
        bind(ProfileConfigSource.class).toInstance(profileConfigSource);
        bind(ProfileReader.class).to(JsonProfileReader.class);

        bind(File.class)
            .annotatedWith(Names.named("config:profileFile"))
            .toInstance(profileConfigSource.getProfileFile());
        bind(String.class)
            .annotatedWith(Names.named("config:filePath"))
            .toInstance(profileConfigSource.fromFilePath());

        bind(Key.get(new TypeLiteral<Validator<ProfileDTO>>(){})).to(ProfileValidator.class);
        bind(Key.get(new TypeLiteral<Validator<CreateProfile>>(){})).to(CreateProfileValidator.class);
        bind(Key.get(new TypeLiteral<Validator<ReadRelationships>>(){})).to(ReadRelationshipsValidator.class);
        bind(CommandBus.class).to(ProfileCommandBus.class);
    }
}
