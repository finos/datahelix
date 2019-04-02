package com.scottlogic.deg.schemas.v0_1;

import com.scottlogic.deg.schemas.common.ValidationResult;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import javafx.util.Pair;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.Problem;
import org.leadpony.justify.api.ProblemHandler;

import javax.json.stream.JsonParser;
import javax.print.DocFlavor;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Used to validate a DataHelix Profile JSON file.
 * <p>
 * Checks that the profile JSON file is valid against the DataHelix Profile Schema (datahelix.schema.json)
 */
public class ProfileSchemaValidatorLeadPony implements ProfileSchemaValidator {

    private List<String> profileJsonLines;
    private Path profilePath;

    @Override
    public ValidationResult validateProfile(File profileFile) {
        try {
            byte[] data = Files.readAllBytes(profilePath = profileFile.toPath());
            profileJsonLines = readAllLines(data);
            return validateProfile(new ByteArrayInputStream(data));
        } catch (IOException e) {
            List<String> errMsgs = new ArrayList<>();
            errMsgs.add(e.getLocalizedMessage());
            return new ValidationResult(errMsgs);
        }
    }

    private List<String> readAllLines(byte[] data) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
        List<String> lines = new ArrayList<>();

        String line;
        while ((line = bufferedReader.readLine()) != null){
            lines.add(line);
        }

        return lines;
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
            if(!problems.isEmpty()) {
                HashMap<Integer, String> problemDictionary = new HashMap<>();
                extractProblems(problems, problemDictionary);
                errorMessages.addAll(formatProblems(problemDictionary));
            }
        }

        if(!errorMessages.isEmpty()) {
            errorMessages.add(0,
                "Error(s) occurred during schema validation." +
                "\nFile path: " + profilePath.toString() +
                "\nFor full details try opening the profile in a json schema-enabled IDE." +
                "\nSee https://github.com/ScottLogic/datahelix/blob/master/docs/ProfileDeveloperGuide.md#Microsoft-Visual-Studio-Code\n");
        }
        return new ValidationResult(errorMessages);
    }

    private void extractProblems(List<Problem> problems, HashMap<Integer, String> problemDictionary) {
        for (Problem problem : problems) {
            extractProblem(problem, problemDictionary);
        }
    }

    private void extractProblem(Problem problem, HashMap<Integer, String> problemDictionary) {
        if (!problem.hasBranches()) {
            int lineNumber = (int)problem.getLocation().getLineNumber();
            String formattedMessage = "- " + problem.getMessage() + "\n";
            problemDictionary.merge(lineNumber, formattedMessage, String::concat);

            return;
        }
        extractProblems(problem.getBranch(0), problemDictionary);
    }

    private List<String> formatProblems(HashMap<Integer, String> problemDictionary) {
        List<String> outputList = new ArrayList<>();
        String messageFormat = "Problem found at line %d\n... %s ...\nSuggested fix:\n%s";

        problemDictionary.forEach(
            (lineNo, messages)
                -> outputList.add(
                String.format(
                    messageFormat,
                    lineNo,
                    profileJsonLines.get(lineNo - 1).trim(),
                    messages
                )
            )
        );

        return outputList;
    }
}
