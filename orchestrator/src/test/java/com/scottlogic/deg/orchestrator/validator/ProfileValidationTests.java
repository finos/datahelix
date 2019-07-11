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

                DynamicTest test = DynamicTest.dynamicTest(dir.getName(), () -> {
                    new ProfileSchemaValidatorLeadPony().validateProfile(profileFile);
                });

                dynamicTests.add(test);
            }
            return dynamicTests;
        }
    }