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

package com.scottlogic.deg.orchestrator.validator;

import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidatorLeadPony;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

public class ProfileValidationTests {

        @TestFactory
        Collection<DynamicTest> shouldAllValidateWithoutErrors() throws IOException {
            Collection<DynamicTest> dynamicTests = new ArrayList<>();

            File[] directoriesArray =
                Paths.get("..", "examples")
                    .toFile()
                    .listFiles(File::isDirectory);
            for (File dir : directoriesArray) {
                File profileFile = Paths.get(dir.getCanonicalPath(), "profile.json").toFile();

                String schemaVersion = "0.1";
                DynamicTest test = DynamicTest.dynamicTest(dir.getName(), () -> {
                    new ProfileSchemaValidatorLeadPony().validateProfile(profileFile, schemaVersion);
                });

                dynamicTests.add(test);
            }
            return dynamicTests;
        }
    }