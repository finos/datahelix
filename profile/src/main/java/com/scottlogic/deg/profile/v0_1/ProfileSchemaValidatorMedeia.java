package com.scottlogic.deg.profile.v0_1;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.common.ValidationException;
import com.worldturner.medeia.api.SchemaSource;
import com.worldturner.medeia.api.UrlSchemaSource;
import com.worldturner.medeia.api.ValidationFailedException;
import com.worldturner.medeia.api.jackson.MedeiaJacksonApi;
import com.worldturner.medeia.schema.validation.SchemaValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileSchemaValidatorMedeia implements ProfileSchemaValidator {

    private static MedeiaJacksonApi api = new MedeiaJacksonApi();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void validateProfile(File profileFile) {
        try {
            validateProfile(this.getClass().getResourceAsStream(datahelixProfileSchema), new FileInputStream(profileFile));
        } catch (FileNotFoundException e) {
            throw new ValidationException(e.getLocalizedMessage());
        }
    }

    private void validateProfile(InputStream schemaStream, InputStream profileStream) {
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

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }

    private SchemaValidator loadSchema() {
        SchemaSource source = new UrlSchemaSource(getClass().getResource(datahelixProfileSchema));
        return api.loadSchema(source);
    }
}
