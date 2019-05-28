package com.scottlogic.deg.profile.guice;

import com.google.inject.AbstractModule;
import com.scottlogic.deg.profile.reader.AtomicConstraintReaderLookup;
import com.scottlogic.deg.profile.reader.BaseAtomicConstraintReaderLookup;
import com.scottlogic.deg.profile.reader.JsonProfileReader;
import com.scottlogic.deg.profile.reader.ProfileReader;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;

public class ProfileModule extends AbstractModule {

    private final ProfileConfigSource profileConfigSource;

    public ProfileModule(ProfileConfigSource profileConfigSource) {
        this.profileConfigSource = profileConfigSource;
    }

    @Override
    protected void configure() {
        // Bind command line to correct implementation
        bind(ProfileConfigSource.class).toInstance(profileConfigSource);

        bind(ProfileSchemaValidator.class).toProvider(ProfileSchemaValidatorProvider.class);

        bind(ProfileReader.class).to(JsonProfileReader.class);

        bind(AtomicConstraintReaderLookup .class).to(BaseAtomicConstraintReaderLookup .class);
    }
}
