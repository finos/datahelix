package com.scottlogic.deg.schemas.v0_1;

import com.scottlogic.deg.schemas.common.ValidationResult;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.Problem;
import org.leadpony.justify.api.ProblemHandler;

import javax.json.stream.JsonParser;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to validate a DataHelix Profile JSON file.
 * <p>
 * Checks that the profile JSON file is valid against the DataHelix Profile Schema (datahelix.schema.json)
 */
public class ProfileSchemaValidatorLeadPony implements ProfileSchemaValidator {

    private List<String> profileJsonLines;

    @Override
    public ValidationResult validateProfile(File profileFile) {
        try {
            profileJsonLines = Files.readAllLines(profileFile.toPath());
            return validateProfile(new FileInputStream(profileFile));
        } catch (IOException e) {
            List<String> errMsgs = new ArrayList<>();
            errMsgs.add(e.getLocalizedMessage());
            return new ValidationResult(errMsgs);
        }
    }

    /**
     * Validates a json file against the DataHelix Profile JSON Schema.
     *
     * @return the result of validating the provided profile
     */
    @Override
    public ValidationResult validateProfile(InputStream profileStream) {
        return validateProfile(this.getClass().getResourceAsStream(datahelixProfileSchema), profileStream);
    }

    /**
     * @return the result of validating the provided DataHelix Profile
     */
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
            List<Problem> problems = new ArrayList<>();
            ProblemHandler handler = ProblemHandler.collectingTo(problems);

            // We have to step over the profile otherwise it is not checked against the schema.
            try (JsonParser parser = service.createParser(profileStream, schema, handler)) {
                while (parser.hasNext()) {
                    JsonParser.Event event = parser.next();
                    // Do something useful here
                }
            }

            //Add all of the problems as error messages
            extractProblems(problems, errorMessages);
        }

        if(!errorMessages.isEmpty()) {
            errorMessages.add(0, "Error(s) occurred during schema validation." +
                "\nFor full details try opening the profile in a json schema-enabled IDE. See link");
        }
        return new ValidationResult(errorMessages);
    }

    private void extractProblems(List<Problem> problems, List<String> outputList) {
        for (Problem problem : problems) {
            extractProblem(problem, outputList);
        }
    }

    private void extractProblem(Problem problem, List<String> outputList) {
        if (!problem.hasBranches()) {
            String jsonSnippet = profileJsonLines.get((int)problem.getLocation().getLineNumber() -1).trim();
            outputList.add("Problem found at line " + problem.getLocation().getLineNumber() + "\n... " + jsonSnippet + " ...\n"
                + "Suggested fix:" + "\n- " + problem.getMessage() + "\n");
            return;
        }
        extractProblems(problem.getBranch(0), outputList);
    }
}
