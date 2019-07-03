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

package com.scottlogic.deg.orchestrator.validator;

import com.google.inject.Inject;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.output.FileUtils;

import java.nio.file.Path;

/**
 * Class used to determine whether the command line options are valid for visualisation
 */
public class VisualisationConfigValidator {
    private final FileUtils fileUtils;

    @Inject
    public VisualisationConfigValidator(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    /**
     * @return the result of command line validation. Contains a list of error messages.
     * if the list is empty then the validation was successful.
     */
    public void validateCommandLine(boolean overwrite, Path outputPath) {
        if (fileUtils.isDirectory(outputPath)) {
            throw new ValidationException(
                "Invalid Output - target is a directory, please use a different output filename"
            );
        }
         if (!overwrite && fileUtils.exists(outputPath)) {
            throw new ValidationException(
                "Invalid Output - file already exists, please use a different output filename " +
                    "or use the --overwrite option"
            );
        }
    }
}
