package com.scottlogic.deg.schemas.v0_1;

import com.scottlogic.deg.schemas.common.ValidationResult;

import java.io.InputStream;

/**
 * Used to validate a DataHelix Profile JSON file.
 * <p>
 * Checks that the profile JSON file is valid against the
 * DataHelix Profile Schema (datahelix.schema.json)
 * </p>
 */
public interface ProfileValidator {

    String datahelixProfileSchema = "/profileschema/0.1/datahelix.schema.json";

    /**
     * Validates a json file against the DataHelix Profile JSON Schema.
     *
     * @param profileStream an InputStream of the profile to validate
     * @return the result of validating the provided profile
     */
    ValidationResult validateProfile(InputStream profileStream);

    /**
     * Validates a json file against a customised DataHelix Profile JSON Schema.
     *
     * @param schemaStream  an InputStream of the schema to validate the profile against
     * @param profileStream an InputStream of the profile to validate
     * @return the result of validating the provided profile against the provided schema
     */
    ValidationResult validateMedeia(InputStream schemaStream, InputStream profileStream);
}
