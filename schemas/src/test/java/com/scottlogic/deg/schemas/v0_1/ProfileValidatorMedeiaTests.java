package com.scottlogic.deg.schemas.v0_1;

import org.junit.jupiter.api.Test;

class ProfileValidatorMedeiaTests  extends ProfileSchemaValidatorTests{

    private ProfileSchemaValidator profileValidator = new ProfileSchemaValidatorMedeia();

    @Test
    void testInvalidProfiles() {
        super.testInvalidProfiles(profileValidator);
    }

    @Test
    void testValidProfiles() {
        super.testValidProfiles(profileValidator);
    }

}
