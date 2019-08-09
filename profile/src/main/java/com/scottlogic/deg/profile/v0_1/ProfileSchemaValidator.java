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

package com.scottlogic.deg.profile.v0_1;

import java.io.File;
import java.net.URL;

/**
 * Used to validate a DataHelix Profile JSON file.
 * <p>
 * Checks that the profile JSON file is valid against the
 * DataHelix Profile Schema (datahelix.schema.json)
 * </p>
 */
public interface ProfileSchemaValidator {

    /**
     * Validates a json file against the DataHelix Profile JSON Schema.
     *
     * @param profileFile an File object that is the profile to validate
     * @param schema the schema to check validate against
     * @return the result of validating the provided profile
     */
    void validateProfile(File profileFile, URL schema);
}
