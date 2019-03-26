package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.ConfigSource;
import com.scottlogic.deg.schemas.v0_1.NoopProfileSchemaValidator;
import com.scottlogic.deg.schemas.v0_1.ProfileSchemaValidator;
import com.scottlogic.deg.schemas.v0_1.ProfileSchemaValidatorLeadPony;

public class ProfileSchemaValidatorProvider implements Provider<ProfileSchemaValidator> {

    private final ConfigSource configSource;
    private final ProfileSchemaValidatorLeadPony leadPonyValidator;

    @Inject
    public ProfileSchemaValidatorProvider(ConfigSource configSource, ProfileSchemaValidatorLeadPony leadPonyValidator) {
        this.configSource = configSource;
        this.leadPonyValidator = leadPonyValidator;
    }

    @Override
    public ProfileSchemaValidator get() {
        if (!configSource.isSchemaValidationEnabled()) {
            return new NoopProfileSchemaValidator();
        }

        return leadPonyValidator;
    }
}
