/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.profile.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;

import java.io.*;
import java.net.URL;
import java.util.stream.Collectors;

public abstract class ProfileSchemaValidatorTests {
    private static final String SCHEMA_LOCATION = "profileschema/datahelix.schema.json";
    protected ProfileSchemaValidator validator;
    protected String schema;
    protected String schemaVersion;

    protected abstract ProfileSchemaValidator setValidator();

    private String getProfileSchema() throws IOException {
        String schema;
        URL schemaUrl = Thread.currentThread()
            .getContextClassLoader()
            .getResource(SCHEMA_LOCATION);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(schemaUrl.openStream()))) {
            schema = br.lines().collect(Collectors.joining(System.lineSeparator()));
        }

        return schema;
    }

    private String getProfileSchemaVersion() {
        InputStream schemaPath = Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream(SCHEMA_LOCATION);

        JsonValidationService service = JsonValidationService.newInstance();
        JsonSchema schema = service.readSchema(schemaPath);

        return schema.getSubschemaAt("/definitions/schemaVersion").toJson().asJsonObject().get("const").toString();
    }

    @BeforeEach
    void setUp() throws IOException {
        validator = setValidator();
        schema = getProfileSchema();
        schemaVersion = getProfileSchemaVersion();
    }

    @Test
    void validate_greaterThanMaxValue_isValid() {
        String profile = "{" +
            "  \"schemaVersion\": " + schemaVersion + "," +
            "  \"rules\": [" +
            "    {" +
            "      \"rule\": \"rule 1\"," +
            "      \"constraints\": [" +
            "        {" +
            "          \"field\": \"field1\"," +
            "          \"is\": \"greaterThan\"," +
            "          \"value\": 1E20 " +
            "        }" +
            "      ]" +
            "    }" +
            "  ]," +
            "  \"fields\": [ { \"name\": \"field1\", \"type\": \"integer\" } ]" +
            "}";

        validator.validateProfile(profile, schema);
    }

    @Test
    public void validate_greaterThanOrEqualToMaxValue_isValid() {
        String profile = "{" +
            "  \"schemaVersion\": " + schemaVersion + "," +
            "  \"rules\": [" +
            "    {" +
            "      \"rule\": \"rule 1\"," +
            "      \"constraints\": [" +
            "        {" +
            "          \"field\": \"field1\"," +
            "          \"is\": \"greaterThanOrEqualTo\"," +
            "          \"value\": 1E20 " +
            "        }" +
            "      ]" +
            "    }" +
            "  ]," +
            "  \"fields\": [ { \"name\": \"field1\", \"type\": \"integer\" } ]" +
            "}";

        validator.validateProfile(profile, schema);
    }

    @Test
    void validate_greaterThanMinValue_isValid() {
        String profile = "{" +
            "  \"schemaVersion\": " + schemaVersion + "," +
            "  \"rules\": [" +
            "    {" +
            "      \"rule\": \"rule 1\"," +
            "      \"constraints\": [" +
            "        {" +
            "          \"field\": \"field1\"," +
            "          \"is\": \"lessThan\"," +
            "          \"value\": -1E20 " +
            "        }" +
            "      ]" +
            "    }" +
            "  ]," +
            "  \"fields\": [ { \"name\": \"field1\", \"type\": \"integer\" } ]" +
            "}";

        validator.validateProfile(profile, schema);
    }

    @Test
    public void validate_lessThanOrEqualToMinValue_isValid() {
        String profile = "{" +
            "  \"schemaVersion\": " + schemaVersion + "," +
            "  \"rules\": [" +
            "    {" +
            "      \"rule\": \"rule 1\"," +
            "      \"constraints\": [" +
            "        {" +
            "          \"field\": \"field1\"," +
            "          \"is\": \"lessThanOrEqualTo\"," +
            "          \"value\": -1E20 " +
            "        }" +
            "      ]" +
            "    }" +
            "  ]," +
            "  \"fields\": [ { \"name\": \"field1\", \"type\": \"integer\" } ]" +
            "}";

        validator.validateProfile(profile, schema);
    }

    @Test
    public void validate_constraintsNestedInsideAllOf_isValid() {
        String profile = "{" +
            "  \"schemaVersion\": " + schemaVersion + "," +
            "  \"rules\": [" +
            "    {" +
            "      \"rule\": \"rule 1\"," +
            "      \"constraints\": [" +
            "        {" +
            "          \"allOf\": [" +
            "            { \"field\": \"field1\", \"is\": \"equalTo\", \"value\": \"foobar\" }," +
            "            { \"field\": \"field2\", \"is\": \"greaterThan\", \"value\": 43 } ] } ] } ]," +
            "  \"fields\": [" +
            "    { \"name\": \"field1\", \"type\": \"string\" }," +
            "    { \"name\": \"field2\", \"type\": \"integer\" } ]" +
            "}";

        validator.validateProfile(profile, schema);
    }

    @Test
    public void validate_constraintsNestedInsideAnyOf_isValid() {
        String profile = "{" +
            "  \"schemaVersion\": " + schemaVersion + "," +
            "  \"rules\": [" +
            "    {" +
            "      \"rule\": \"rule 1\"," +
            "      \"constraints\": [" +
            "        {" +
            "          \"anyOf\":[" +
            "            { \"field\": \"field1\", \"is\": \"longerThan\", \"value\": 5 }," +
            "            { \"field\": \"field1\", \"is\": \"shorterThan\", \"value\": 19 } ] } ] } ]," +
            "  \"fields\": [ { \"name\": \"field1\", \"type\": \"integer\", \"formatting\": \"%ggg\" } ]" +
            "}";

        validator.validateProfile(profile, schema);
    }

    @Test
    public void validate_dateEqualToDynamicOffset_isValid() {
        String profile = "{" +
            "  \"schemaVersion\": " + schemaVersion + "," +
            "  \"fields\": [" +
            "    { \"name\": \"first\", \"type\": \"datetime\", \"nullable\": false }," +
            "    { \"name\": \"second\", \"type\": \"datetime\", \"nullable\": false }," +
            "    { \"name\": \"firstWorking\", \"type\": \"datetime\", \"nullable\": false }," +
            "    { \"name\": \"secondWorking\", \"type\": \"datetime\", \"nullable\": false }," +
            "    { \"name\": \"current\", \"type\": \"datetime\", \"nullable\": false }" +
            "  ]," +
            "  \"rules\": [" +
            "    {" +
            "      \"constraints\": [" +
            "        { \"field\": \"first\", \"is\": \"after\", \"value\": {" +
            "            \"date\": \"8001-02-03T04:05:06.007\" }" +
            "        }," +
            "        { \"field\": \"second\", \"is\": \"equalTo\", \"otherField\": \"first\", \"offset\": 3, \"offsetUnit\": \"days\" }," +
            "        { \"field\": \"firstWorking\", \"is\": \"equalTo\", \"value\": {" +
            "            \"date\": \"2019-08-12T12:00:00.000\" }" +
            "        }," +
            "        { \"field\": \"secondWorking\", \"is\": \"equalTo\", \"otherField\": \"firstWorking\", \"offset\": 8, \"offsetUnit\": \"working days\" }," +
            "        { \"field\": \"current\", \"is\": \"before\", \"value\": {" +
            "            \"date\": \"now\" }" +
            "        }," +
            "        { \"field\": \"current\", \"is\": \"after\", \"value\": {" +
            "            \"date\": \"2019-06-01T12:00:00.000\" }" +
            "        } ] } ]" +
            "}";

        validator.validateProfile(profile, schema);
    }

    @Test
    public void validate_emptyRuleArray_isValid() {
        String profile = "{" +
            "  \"schemaVersion\": " + schemaVersion + "," +
            "  \"fields\": [" +
            "    { \"name\": \"first\", \"type\": \"datetime\" }," +
            "    { \"name\": \"second\", \"type\": \"datetime\" }" +
            "  ]," +
            "  \"rules\": []" +
            "}";

        validator.validateProfile(profile, schema);
    }

    @Test
    public void validate_simpleAnyOfAllOf_isValid() {
        String profile = "{" +
            "  \"schemaVersion\": " + schemaVersion + "," +
            "  \"fields\": [" +
            "    { \"name\": \"first\", \"type\": \"string\" }," +
            "    { \"name\": \"second\", \"type\": \"string\" }" +
            "  ]," +
            "  \"rules\": [" +
            "    {" +
            "      \"rule\": \"rule 1\"," +
            "      \"constraints\": [" +
            "        {" +
            "          \"anyOf\": [" +
            "            { \"field\": \"field1\", \"is\": \"shorterThan\", \"value\": 19 }," +
            "            { \"field\": \"field1\", \"is\": \"inSet\", \"values\": [\"1\", 2] } ]" +
            "        }," +
            "        {" +
            "          \"allOf\": [" +
            "            { \"field\": \"field2\", \"is\": \"null\" }," +
            "            { \"not\": { \"field\": \"field1\", \"is\": \"null\" } }" +
            "          ]" +
            "        } ] } ]" +
            "}";

        validator.validateProfile(profile, schema);
    }

    @Test
    public void validate_simpleIf_isValid() {
        String profile = "{" +
            "  \"schemaVersion\": " + schemaVersion + "," +
            "  \"fields\": [" +
            "    { \"name\": \"first\", \"type\": \"string\" }," +
            "    { \"name\": \"second\", \"type\": \"string\" }" +
            "  ]," +
            "  \"rules\": [" +
            "    {" +
            "      \"rule\": \"rule 1\"," +
            "      \"constraints\": [" +
            "        {" +
            "          \"if\": {\"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"integer\"}," +
            "          \"then\": {\"field\": \"foo\", \"is\": \"greaterThan\", \"value\": 0}," +
            "          \"else\": {\"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"N/A\"}" +
            "        }," +
            "        {" +
            "          \"if\": {\"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"integer\"}," +
            "          \"then\": {\"field\": \"foo\", \"is\": \"greaterThan\", \"value\": 0}" +
            "        }" +
            "      ]" +
            "    }" +
            "  ]" +
            "}";

        validator.validateProfile(profile, schema);
    }
}
