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
import com.scottlogic.deg.profile.serialisation.SchemaVersionRetriever;
import com.scottlogic.deg.profile.v0_1.*;

public class ProfileSchemaVersionCheckingProvider implements Provider<SchemaVersionValidator> {

    private final ProfileConfigSource profileConfigSource;
    private final SupportedVersionChecker validator;

    @Inject
    public ProfileSchemaVersionCheckingProvider(ProfileConfigSource profileConfigSource, SupportedVersionChecker validator) {
        this.profileConfigSource = profileConfigSource;
        this.validator = validator;
    }

    @Override
    public SchemaVersionValidator get() {
        System.err.println(profileConfigSource.isSchemaVersionValidationDisabled());
        if (profileConfigSource.isSchemaVersionValidationDisabled()) {
            return new NoValidationVersionChecker(new SchemaVersionRetriever(), profileConfigSource);
        }

        return validator;
    }
}
