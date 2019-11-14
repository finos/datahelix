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

package com.scottlogic.datahelix.generator.output.outputtarget;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.datahelix.generator.common.util.FileUtils;
import com.scottlogic.datahelix.generator.output.OutputPath;
import com.scottlogic.datahelix.generator.output.writer.OutputWriterFactory;

import java.nio.file.Path;

/** Represents a directory specified by a user as a target for violation data */
public class OutputTargetFactory {
    private final Path directoryPath;
    private final boolean canOverwriteExistingFiles;
    private final OutputWriterFactory formatOfViolationDatasets;
    private final FileUtils fileUtils;

    @Inject
    public OutputTargetFactory(
        OutputPath directoryPath,
        OutputWriterFactory formatOfViolationDatasets,
        @Named("config:canOverwriteOutputFiles") boolean canOverwriteExistingFiles,
        FileUtils fileUtils) {
        this.directoryPath = directoryPath.getPath();
        this.canOverwriteExistingFiles = canOverwriteExistingFiles;
        this.formatOfViolationDatasets = formatOfViolationDatasets;
        this.fileUtils = fileUtils;
    }

    public SingleDatasetOutputTarget create(String name) {
        String filename =
            formatOfViolationDatasets.getFileExtensionWithoutDot()
                .map(extension -> name + "." + extension)
                .orElse(name);

        return new FileOutputTarget(
            new OutputPath(directoryPath.resolve(filename)),
            formatOfViolationDatasets,
            canOverwriteExistingFiles,
            fileUtils);
    }
}
