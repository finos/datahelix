package com.scottlogic.deg.schemas.v0_1;

import com.scottlogic.deg.schemas.common.ValidationResult;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.ProblemHandler;

import javax.json.stream.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to validate a DataHelix Profile JSON file.
 * <p>
 * Checks that the profile JSON file is valid against the DataHelix Profile Schema
 * (datahelix.schema.json) using the <a href="https://github.com/leadpony/justify">
 * LeadPony Justify JSON Schema Validator</a>
 * </p>
 */
public class ProfileSchemaValidatorLeadPony implements ProfileSchemaValidator {

    @Override
    public ValidationResult validateProfile(File profileFile) {
        try {
            return validateProfile(new FileInputStream(profileFile));
        } catch (FileNotFoundException e) {
            List<String> errMsgs = new ArrayList<>();
            errMsgs.add(e.getLocalizedMessage());
            return new ValidationResult(errMsgs);
        }
    }

    @Override
    public ValidationResult validateProfile(InputStream profileStream) {
        return validateProfile(this.getClass().getResourceAsStream(datahelixProfileSchema), profileStream);
    }

    @Override
    public ValidationResult validateProfile(InputStream schemaStream, InputStream profileStream) {
        List<String> errorMessages = new ArrayList<>();
        if (schemaStream == null) {
            errorMessages.add("Null Profile Schema Stream");
        } else if (profileStream == null) {
            errorMessages.add("Null Profile Stream");
        } else {
            JsonValidationService service = JsonValidationService.newInstance();

            // Reads the JSON schema
            JsonSchema schema = service.readSchema(schemaStream);

            // Problem handler which will print problems found.
            ProblemHandler handler = service.createProblemPrinter(s -> errorMessages.add(s));

            // We have to step over the profile otherwise it is not checked against the schema.
            try (JsonParser parser = service.createParser(profileStream, schema, handler)) {
                while (parser.hasNext()) {
                    JsonParser.Event event = parser.next();
                    // Do something useful here
                }
            }
        }
        return new ValidationResult(errorMessages);
    }

}
