package com.scottlogic.deg.schemas.v0_1;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

class ProfileSchemaValidatorLeadPonyTests extends ProfileSchemaValidatorTests {

    private ProfileSchemaValidator profileValidator = new ProfileSchemaValidatorLeadPony();

    @TestFactory
    Collection<DynamicTest> testInvalidProfiles() {
        return super.testInvalidProfiles(profileValidator);
    }

    @TestFactory
    Collection<DynamicTest> testValidProfiles() {
        return super.testValidProfiles(profileValidator);
    }

}
