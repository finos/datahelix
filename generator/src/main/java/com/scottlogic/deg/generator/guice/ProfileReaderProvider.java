package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.inputs.SoftConstraintAppendingProfileReader;

public class ProfileReaderProvider implements Provider<ProfileReader> {
    private final JsonProfileReader underlyingReader;

    @Inject
    public ProfileReaderProvider(JsonProfileReader underlyingReader){
        this.underlyingReader = underlyingReader;
    }

    @Override
    public ProfileReader get() {
        return new SoftConstraintAppendingProfileReader(underlyingReader);
    }
}
