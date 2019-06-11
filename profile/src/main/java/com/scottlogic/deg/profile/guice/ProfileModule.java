package com.scottlogic.deg.profile.guice;

import com.google.inject.AbstractModule;
import com.scottlogic.deg.profile.reader.*;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;

import java.util.Arrays;
import java.util.stream.Stream;

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

        // Load built-in profile-to-constraint mappings
        BaseConstraintReaderMap map = new BaseConstraintReaderMap(Stream.of(
            new CoreAtomicTypesConstraintReaderSource(),
            new FinancialTypesConstraintReaderSource(),
            new PersonalDataTypesConstraintReaderSource()
        ));
        bind(ConstraintReaderMap.class).toInstance(map);
    }
}
