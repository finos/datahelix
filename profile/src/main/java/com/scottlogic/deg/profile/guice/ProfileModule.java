package com.scottlogic.deg.profile.guice;

import com.google.inject.AbstractModule;
import com.scottlogic.deg.profile.reader.*;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;

import java.util.Arrays;

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
        ConstraintReaderMapEntrySource[] mappingProviders = {
            new CoreAtomicTypesConstraintReaderSource(),
            new FinancialTypesConstraintReaderSource(),
            new PersonalDataTypesConstraintReaderSource()
        };
        BaseConstraintReaderMap map = new BaseConstraintReaderMap(Arrays.stream(mappingProviders));
        bind(ConstraintReaderMap.class).toInstance(map);
    }
}
