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
package com.scottlogic.deg.profile;

import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SupportedVersionsGetter {
    private final static String RESOURCES_PATH = "profileschema/datahelix.schema.json";

    /**
     * @return all valid schema versions
     **/
    public List<String> getSupportedSchemaVersions() {
        List<String> supportedSchemaVersions = new ArrayList<>();
        InputStream schemaPath = getClass().getClassLoader().getResourceAsStream(RESOURCES_PATH);

        JsonValidationService service = JsonValidationService.newInstance();
        JsonSchema schema = service.readSchema(schemaPath);

        String version = schema.getSubschemaAt("/definitions/schemaVersion").toJson().asJsonObject().get("const").toString();
        supportedSchemaVersions.add(version.replace("\"", ""));
        return supportedSchemaVersions;
    }
}
