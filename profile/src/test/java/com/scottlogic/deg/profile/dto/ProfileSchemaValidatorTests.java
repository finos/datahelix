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
}
