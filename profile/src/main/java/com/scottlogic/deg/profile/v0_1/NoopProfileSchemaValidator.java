package com.scottlogic.deg.profile.v0_1;

import java.io.File;

public class NoopProfileSchemaValidator implements ProfileSchemaValidator {

    @Override
    public void validateProfile(File profileFile) {
        return;
    }
}
