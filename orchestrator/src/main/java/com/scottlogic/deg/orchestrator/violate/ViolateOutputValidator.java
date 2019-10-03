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

package com.scottlogic.deg.orchestrator.violate;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.output.OutputPath;
import com.scottlogic.deg.output.outputtarget.OutputTargetValidationException;
import com.scottlogic.deg.common.util.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

public class ViolateOutputValidator {
    private final boolean canOverwriteExistingFiles;
    private final Path directoryPath;
    private final FileUtils fileUtils;

    @Inject
    public ViolateOutputValidator(
        @Named("config:canOverwriteOutputFiles") boolean canOverwriteExistingFiles,
        OutputPath directoryPath, FileUtils fileUtils) {
        this.canOverwriteExistingFiles = canOverwriteExistingFiles;
        this.directoryPath = directoryPath.getPath();
        this.fileUtils = fileUtils;
    }

    public void validate(Profile profile) throws OutputTargetValidationException, IOException {
        if (!fileUtils.exists(directoryPath)) {
            fileUtils.createDirectories(directoryPath);
        } else if (!fileUtils.isDirectory(directoryPath)) {
            throw new OutputTargetValidationException(
                "not a directory, please enter a valid directory name");
        } else if (!canOverwriteExistingFiles && !fileUtils.isDirectoryEmpty(directoryPath, profile.getRules().size())) {
            throw new OutputTargetValidationException(
                "directory not empty, please remove any 'manifest.json' and '[0-9].csv' files or use the --replace option");
        }
    }
}
