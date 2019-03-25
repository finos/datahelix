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
    private final NoopProfileSchemaValidator noopProfileSchemaValidator;

    @Inject
    public ProfileSchemaValidatorProvider(ConfigSource configSource, ProfileSchemaValidatorLeadPony leadPonyValidator,
                                          NoopProfileSchemaValidator noopProfileSchemaValidator) {
        this.configSource = configSource;
        this.leadPonyValidator = leadPonyValidator;
        this.noopProfileSchemaValidator = noopProfileSchemaValidator;
    }

    @Override
    public ProfileSchemaValidator get() {
        if (configSource.disableSchema()) {
            return noopProfileSchemaValidator;
        }

        return leadPonyValidator;
    }
}
