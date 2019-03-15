package com.scottlogic.deg.schemas.v0_1;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.schemas.common.ValidationResult;
import com.worldturner.medeia.api.SchemaSource;
import com.worldturner.medeia.api.UrlSchemaSource;
import com.worldturner.medeia.api.ValidationFailedException;
import com.worldturner.medeia.api.jackson.MedeiaJacksonApi;
import com.worldturner.medeia.schema.validation.SchemaValidator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to validate a DataHelix Profile JSON file.
 * <p>
 * Checks that the profile JSON file is valid against the DataHelix Profile Schema (datahelix.schema.json)
 */
public class ProfileValidatorMedia implements ProfileValidator {

    private static MedeiaJacksonApi api = new MedeiaJacksonApi();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ValidationResult validateProfile(InputStream profileStream) {
        return validateMedeia(
            this.getClass().getResourceAsStream(datahelixProfileSchema),
            profileStream);
    }

    @Override
    public ValidationResult validateMedeia(InputStream schemaStream, InputStream profileStream) {
        List<String> errorMessages = new ArrayList<>();
        if (schemaStream == null) {
            errorMessages.add("Null Profile Schema Stream");
        } else if (profileStream == null) {
            errorMessages.add("Null Profile Stream");
        } else {
            try {
                SchemaValidator validator = loadSchema();

                JsonParser unvalidatedParser = objectMapper.getFactory().createParser(profileStream);
                JsonParser validatedParser = api.decorateJsonParser(validator, unvalidatedParser);
                api.parseAll(validatedParser);
            } catch (ValidationFailedException | IOException e) {
                errorMessages.add("Exception validating profile:" + e);
            }
        }
        return new ValidationResult(errorMessages);
    }

    @NotNull
    private SchemaValidator loadSchema() {
        SchemaSource source = new UrlSchemaSource(getClass().getResource(datahelixProfileSchema));
        return api.loadSchema(source);
    }
}
