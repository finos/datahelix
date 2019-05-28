package com.scottlogic.deg.profile.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.profile.guice.ProfileConfigSource;
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
        if (!profileConfigSource.isSchemaValidationEnabled()) {
            return new NoopProfileSchemaValidator();
        }

        return leadPonyValidator;
    }
}
