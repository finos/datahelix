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
import com.scottlogic.deg.profile.v0_1.NoopProfileSchemaValidator;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidatorLeadPony;

public class ProfileSchemaValidatorProvider implements Provider<ProfileSchemaValidator> {

    private final ProfileConfigSource profileConfigSource;
    private final ProfileSchemaValidatorLeadPony leadPonyValidator;

    @Inject
    public ProfileSchemaValidatorProvider(ProfileConfigSource profileConfigSource, ProfileSchemaValidatorLeadPony leadPonyValidator) {
        this.profileConfigSource = profileConfigSource;
        this.leadPonyValidator = leadPonyValidator;
    }

    @Override
    public ProfileSchemaValidator get() {
        if (profileConfigSource.isSchemaValidationDisabled()) {
            return new NoopProfileSchemaValidator();
        }

        return leadPonyValidator;
    }
}
