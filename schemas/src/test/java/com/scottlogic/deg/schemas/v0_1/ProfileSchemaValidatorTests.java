package com.scottlogic.deg.schemas.v0_1;

import com.scottlogic.deg.schemas.common.ValidationResult;
import org.junit.Assert;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileSchemaValidatorTests {

    private final String TEST_PROFILE_DIR = "/test-profiles/";
    private final String INVALID_PROFILE_DIR = "invalid";
    private final String VALID_PROFILE_DIR = "valid";


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

    void testInvalidProfiles(ProfileSchemaValidator profileValidator) {
        File[] listOfFiles = getFileFromURL(INVALID_PROFILE_DIR).listFiles();
        Map<String, ValidationResult> testResults = new HashMap<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            String profileFilename = listOfFiles[i].getName();
            System.out.println("Testing " + INVALID_PROFILE_DIR + " profile [" + profileFilename + "]");
            InputStream testProfile = this.getClass().getResourceAsStream(TEST_PROFILE_DIR + INVALID_PROFILE_DIR + profileFilename);
            ValidationResult result = profileValidator.validateProfile(testProfile);
            testResults.put(profileFilename, result);
        }
        boolean testFailed = false;
        List<String> msgs = new ArrayList<>();
        for (String profile : testResults.keySet()) {
            ValidationResult result = testResults.get(profile);
            if (!result.isValid()) {
                System.out.println("Invalid profile ["+profile+"] tested successfully");
            } else {
                testFailed = true;
                msgs.add("\nInvalid profile ["+profile+"] failed : Validation did fail as expected");
            }
        }
        if (testFailed) {
            Assert.fail("Testing Invalid Profiles Failed with errors [" + msgs + "]");
        }
    }

    void testValidProfiles(ProfileSchemaValidator profileValidator) {
        File[] listOfFiles = getFileFromURL(VALID_PROFILE_DIR).listFiles();
        Map<String, ValidationResult> testResults = new HashMap<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            String profileFilename = listOfFiles[i].getName();
            System.out.println("Testing " + VALID_PROFILE_DIR + " profile [" + profileFilename + "]");
            InputStream testProfile = this.getClass().getResourceAsStream(TEST_PROFILE_DIR + VALID_PROFILE_DIR + profileFilename);
            ValidationResult result = profileValidator.validateProfile(testProfile);
            testResults.put(profileFilename, result);
        }
        boolean testFailed = false;
        List<String> msgs = new ArrayList<>();
        for (String profile : testResults.keySet()) {
            ValidationResult result = testResults.get(profile);
            if (result.isValid()) {
                System.out.println("Valid profile ["+profile+"] tested successfully");
            } else {
                testFailed = true;
                msgs.add("\nValid profile ["+profile+"] test failed :" + result.errorMessages);
            }
        }
        if (testFailed) {
            Assert.fail("Testing Valid Profiles Failed with errors [" + msgs + "]");
        }
    }

}
