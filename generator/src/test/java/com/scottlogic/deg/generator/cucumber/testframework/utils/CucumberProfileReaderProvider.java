package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.inputs.SoftConstraintAppendingProfileReader;

public class CucumberProfileReaderProvider implements Provider<ProfileReader> {
    private final CucumberProfileReader profileReader;

    @Inject
    public CucumberProfileReaderProvider(CucumberProfileReader profileReader) {
        this.profileReader = profileReader;
    }

    @Override
    public ProfileReader get() {
        return new SoftConstraintAppendingProfileReader(profileReader);
    }
}
