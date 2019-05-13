package com.scottlogic.deg.profile.v0_1;

import com.scottlogic.deg.profile.common.ValidationResult;

import java.io.File;

/**
 * Used to validate a DataHelix Profile JSON file.
 * <p>
 * Checks that the profile JSON file is valid against the
 * DataHelix Profile Schema (datahelix.schema.json)
 * </p>
 */
public interface ProfileSchemaValidator {

    String datahelixProfileSchema = "/profileschema/0.1/datahelix.schema.json";

    /**
     * Validates a json file against the DataHelix Profile JSON Schema.
     *
     * @param profileFile an File object that is the profile to validate
     * @return the result of validating the provided profile
     */
    ValidationResult validateProfile(File profileFile);
}
