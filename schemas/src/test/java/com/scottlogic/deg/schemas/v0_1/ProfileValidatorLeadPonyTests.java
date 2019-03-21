package com.scottlogic.deg.schemas.v0_1;

import com.scottlogic.deg.schemas.common.ValidationResult;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

class ProfileValidatorLeadPonyTests {

    private ProfileSchemaValidator profileValidator = new ProfileSchemaValidatorLeadPony();

    private ValidationResult validate(String profile) {
        InputStream testProfile = this.getClass().getResourceAsStream(profile);
        return profileValidator.validateProfile(testProfile);
    }

    @Test
    void validateProfile_notExists_fails() {
        ValidationResult result = validate("non-existing-file");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }

    @Test
    void validateProfile_empty_fails() {
        ValidationResult result = validate("/test-profiles/profile-test-empty-errors.json");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }

    @Test
    void validateProfile_fields_array_empty_fails() {
        ValidationResult result = validate("/test-profiles/profile-test-fields-array-empty-errors.json");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }

    @Test
    void validateProfile_missing_constraint_fails() {
        ValidationResult result = validate("/test-profiles/profile-test-missing-constraint-errors.json");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }

    @Test
    void validateProfile_no_constraints_success() {
        ValidationResult result = validate("/test-profiles/profile-test-no-constraints.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_no_fields_fails() {
        ValidationResult result = validate("/test-profiles/profile-test-no-fields-errors.json");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }

    @Test
    void validateProfile_no_rules_fails() {
        ValidationResult result = validate("/test-profiles/profile-test-no-rules-errors.json");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }

    @Test
    void validateProfile_no_version_fails() {
        ValidationResult result = validate("/test-profiles/profile-test-no-version-errors.json");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }

    @Test
    void validateProfile_rule_array_empty_success() {
        ValidationResult result = validate("/test-profiles/profile-test-rule-array-empty.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_anyof_allof_success() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-anyof-allof.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_anyof_allof_fails() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-anyof-allof-errors.json");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }

    @Test
    void validateProfile_data_constraints_success() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-data-constraints.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_data_constraints_fails() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-data-constraints-errors.json");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }

    @Test
    void validateProfile_data_constraints_nested_1level_success() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-data-constraints-nested-1level.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_data_constraints_nested_2level_success() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-data-constraints-nested-2level.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_data_constraints_nested_7level_success() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-data-constraints-nested-7level.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_if_success() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-if.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_if_fails() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-if-errors.json");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }

    @Test
    void validateProfile_inset_success() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-inset.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_not_success() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-not.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_regex_success() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-regex.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_regex_fails() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-regex-errors.json");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }

    @Test
    void validateProfile_temporal_success() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-temporals.json");
        Assert.assertTrue("Profile should be valid", result.isValid());
    }

    @Test
    void validateProfile_temporal_fails() {
        ValidationResult result = validate("/test-profiles/profile-test-simple-temporals-errors.json");
        Assert.assertFalse("Profile should not be valid", result.isValid());
    }
}
