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

import com.scottlogic.deg.common.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ProfileSchemaValidatorTests {
    private final String TEST_PROFILE_DIR = "/test-profiles/";
    private final String TEST_SCHEMA_DIR = "/profileschema/";
    private final String INVALID_PROFILE_DIR = "invalid";
    private final String VALID_PROFILE_DIR = "valid";

    FilenameFilter jsonFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            String lowercaseName = name.toLowerCase();
            if (lowercaseName.endsWith(".json")) {
                return true;
            } else {
                return false;
            }
        }
    };

    private File getFileFromURL(String profileDirName) {
        URL url = this.getClass().getResource(TEST_PROFILE_DIR + profileDirName);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        } finally {
            return file;
        }
    }

    Collection<DynamicTest> testInvalidProfiles(ProfileSchemaValidator profileValidator) {
        File[] listOfFiles = getFileFromURL(INVALID_PROFILE_DIR).listFiles(jsonFilter);
        Collection<DynamicTest> dynTsts = new ArrayList<DynamicTest>();
        String schemaVersion = "0.2";

        for (int i = 0; i < listOfFiles.length; i++) {
            String profileFilename = listOfFiles[i].getName();
            DynamicTest test = DynamicTest.dynamicTest(profileFilename, () -> {
                URL testProfileUrl =
                    this.getClass().getResource(
                        TEST_PROFILE_DIR + INVALID_PROFILE_DIR + "/" + profileFilename
                    );
                URL testSchemaUrl =
                    this.getClass().getResource(
                        TEST_SCHEMA_DIR + schemaVersion + "/mock-schema.json"
                    );
                try {
                    profileValidator.validateProfile(new File(testProfileUrl.getPath()), testSchemaUrl);

                    Supplier<String> msgSupplier = () -> "Profile ["
                        + profileFilename + "] should not be valid";
                    Assertions.fail(msgSupplier);
                }
                catch (ValidationException e) { }
            });
            dynTsts.add(test);
        }
        return dynTsts;
    }

    Collection<DynamicTest> testValidProfiles(ProfileSchemaValidator profileValidator) {
        File[] listOfFiles = getFileFromURL(VALID_PROFILE_DIR).listFiles(jsonFilter);
        Collection<DynamicTest> dynTsts = new ArrayList<DynamicTest>();
        String schemaVersion = "0.2";

        for (int i = 0; i < listOfFiles.length; i++) {
            String profileFilename = listOfFiles[i].getName();
            DynamicTest test = DynamicTest.dynamicTest(profileFilename, () -> {
                URL testProfileUrl =
                    this.getClass().getResource(
                        TEST_PROFILE_DIR + VALID_PROFILE_DIR + "/" + profileFilename
                    );
                URL testSchemaUrl =
                    this.getClass().getResource(
                        TEST_SCHEMA_DIR + schemaVersion + "/mock-schema.json"
                    );
                try {
                    profileValidator.validateProfile(new File(testProfileUrl.getPath()), testSchemaUrl);
                }
                catch (ValidationException e) {
                    Assertions.fail(
                        "Profile [" + profileFilename + "] should be valid [" + e.errorMessages + "]"
                    );
                }
            });
            dynTsts.add(test);
        }
        return dynTsts;
    }

    Collection<DynamicTest> testValidSchemaVersions(ProfileSchemaValidator profileValidator) {
        File[] listOfFiles = getFileFromURL(VALID_PROFILE_DIR).listFiles(jsonFilter);
        String profileFilename = listOfFiles[0].getName();
        Collection<DynamicTest> dynTsts = new ArrayList<DynamicTest>();

        List<String> validSchemaVersions = Arrays.asList("0.2", "1.0"); // Ones in profile/src/test/resources/profileschema
        validSchemaVersions.forEach(schemaVersion -> {
            DynamicTest test = DynamicTest.dynamicTest(schemaVersion, () -> {
                URL testProfileUrl =
                    this.getClass().getResource(
                        TEST_PROFILE_DIR + VALID_PROFILE_DIR + "/" + profileFilename
                    );
                URL testSchemaUrl =
                    this.getClass().getResource(
                        TEST_SCHEMA_DIR + schemaVersion + "/mock-schema.json"
                    );
                try {
                    profileValidator.validateProfile(new File(testProfileUrl.getPath()), testSchemaUrl);

                }
                catch (ValidationException e) {
                    Assertions.fail(
                        "Schema Version [" + schemaVersion + "] should be valid"
                    );
                }
            });
            dynTsts.add(test);
        });
        return dynTsts;
    }

    Collection<DynamicTest> testInvalidSchemaVersions(ProfileSchemaValidator profileValidator) {
        File[] listOfFiles = getFileFromURL(VALID_PROFILE_DIR).listFiles(jsonFilter);
        String profileFilename = listOfFiles[0].getName();
        Collection<DynamicTest> dynTsts = new ArrayList<DynamicTest>();

        List<String> invalidSchemaVersions = Arrays.asList("0.0", "0.11", "0.3", "1.1", "2.0", "0.1"); // Ones not in profile/src/test/resources/profileschema
        invalidSchemaVersions.forEach(schemaVersion -> {
            DynamicTest test = DynamicTest.dynamicTest(schemaVersion, () -> {
                URL testProfileUrl =
                    this.getClass().getResource(
                        TEST_PROFILE_DIR + VALID_PROFILE_DIR + "/" + profileFilename
                    );
                URL testSchemaUrl =
                    this.getClass().getResource(
                        TEST_SCHEMA_DIR + schemaVersion + "/mock-schema.json"
                    );
                try {
                    profileValidator.validateProfile(new File(testProfileUrl.getPath()), testSchemaUrl);
                    Assertions.fail(
                        "Schema Version [" + schemaVersion + "] should be invalid"
                    );
                }
                catch (ValidationException e) {

                }
            });
            dynTsts.add(test);
        });
        return dynTsts;
    }
}
