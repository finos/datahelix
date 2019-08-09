/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    @TestFactory
    Collection<DynamicTest> testValidSchemaVersions() {
        return super.testValidSchemaVersions(profileValidator);
    }

    @TestFactory
    Collection<DynamicTest> testInvalidSchemaVersions() {
        return super.testInvalidSchemaVersions(profileValidator);
    }
}
