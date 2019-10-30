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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.profile.NoopProfileSchemaLoader;
import com.scottlogic.deg.profile.ProfileSchemaFileLoader;
import com.scottlogic.deg.profile.ProfileSchemaLoader;
import com.scottlogic.deg.profile.ProfileSchemaValidator;

public class ProfileSchemaLoaderProvider implements Provider<ProfileSchemaLoader> {

    private final ProfileConfigSource profileConfigSource;
    private final ProfileSchemaValidator validator;

    @Inject
    public ProfileSchemaLoaderProvider(ProfileConfigSource profileConfigSource, ProfileSchemaValidator validator) {
        this.profileConfigSource = profileConfigSource;
        this.validator = validator;
    }

    @Override
    public ProfileSchemaLoader get() {
        if (profileConfigSource.isSchemaValidationDisabled()) {
            return new NoopProfileSchemaLoader();
        }

        return new ProfileSchemaFileLoader(validator);
    }
}
