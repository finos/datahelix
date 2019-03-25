package com.scottlogic.deg.schemas.v0_1;

import org.junit.jupiter.api.Test;

class ProfileValidatorLeadPonyTests extends ProfileSchemaValidatorTests {

    private ProfileSchemaValidator profileValidator = new ProfileSchemaValidatorLeadPony();

    @Test
    void testInvalidProfiles() {
        super.testInvalidProfiles(profileValidator);
    }

    @Test
    void testValidProfiles() {
        super.testValidProfiles(profileValidator);
    }

}
