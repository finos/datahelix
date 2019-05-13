package com.scottlogic.deg.profile.v0_1;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

class ProfileSchemaValidatorMedeiaTests  extends ProfileSchemaValidatorTests {

    private ProfileSchemaValidator profileValidator = new ProfileSchemaValidatorMedeia();

    @TestFactory
    Collection<DynamicTest> testInvalidProfiles() {
        return super.testInvalidProfiles(profileValidator);
    }

    @TestFactory
    Collection<DynamicTest> testValidProfiles() {
        return super.testValidProfiles(profileValidator);
    }

}
