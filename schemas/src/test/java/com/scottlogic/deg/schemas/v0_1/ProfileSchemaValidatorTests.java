package com.scottlogic.deg.schemas.v0_1;

import com.scottlogic.deg.schemas.common.ValidationResult;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class ProfileSchemaValidatorTests {

    private final String TEST_PROFILE_DIR = "/test-profiles/";
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

        for (int i = 0; i < listOfFiles.length; i++) {
            String profileFilename = listOfFiles[i].getName();
            DynamicTest test = DynamicTest.dynamicTest(profileFilename, () -> {
                URL testProfileUrl = this.getClass().getResource(TEST_PROFILE_DIR + INVALID_PROFILE_DIR + "/" + profileFilename);
                ValidationResult result = profileValidator.validateProfile(new File(testProfileUrl.getPath()));
                Supplier<String> msgSupplier = () -> "Profile ["
                    + profileFilename + "] should not be valid";
                Assertions.assertFalse(result.isValid(), msgSupplier);
            });
            dynTsts.add(test);
        }
        return dynTsts;
    }

    Collection<DynamicTest> testValidProfiles(ProfileSchemaValidator profileValidator) {
        File[] listOfFiles = getFileFromURL(VALID_PROFILE_DIR).listFiles(jsonFilter);
        Collection<DynamicTest> dynTsts = new ArrayList<DynamicTest>();

        for (int i = 0; i < listOfFiles.length; i++) {
            String profileFilename = listOfFiles[i].getName();
            DynamicTest test = DynamicTest.dynamicTest(profileFilename, () -> {
                URL testProfileUrl = this.getClass().getResource(TEST_PROFILE_DIR + VALID_PROFILE_DIR + "/" + profileFilename);
                ValidationResult result = profileValidator.validateProfile(new File(testProfileUrl.getPath()));
                Assert.assertTrue("Profile [" + profileFilename + "] should be valid [" + result.errorMessages + "]", result.isValid());
            });
            dynTsts.add(test);
        }
        return dynTsts;
    }

}
