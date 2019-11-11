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

package com.scottlogic.deg.output.outputtarget;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.common.util.FileUtils;
import com.scottlogic.deg.output.OutputPath;
import com.scottlogic.deg.output.writer.DataSetWriter;
import com.scottlogic.deg.output.writer.OutputWriterFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

public class FileOutputTarget implements SingleDatasetOutputTarget {
    private final Path filePath;
    private final boolean canOverwriteExistingFiles;
    private final OutputWriterFactory outputWriterFactory;
    private final FileUtils fileUtils;

    @Inject
    public FileOutputTarget(
        OutputPath outputPath,
        OutputWriterFactory outputWriterFactory,
        @Named("config:canOverwriteOutputFiles") boolean canOverwriteOutputFiles, FileUtils fileUtils) {
        this.canOverwriteExistingFiles = canOverwriteOutputFiles;
        this.outputWriterFactory = outputWriterFactory;
        this.fileUtils = fileUtils;
        this.filePath = outputPath.getPath();
    }

    @Override
    public DataSetWriter openWriter(Fields fields) throws IOException {
        final OutputStream stream = new FileOutputStream(
            this.filePath.toFile(),
            false);

        try {
            return outputWriterFactory.createWriter(stream, fields);
        } catch (Exception e) {
            stream.close();
            throw e;
        }
    }

    @Override
    public void validate() throws OutputTargetValidationException, IOException {
        if (fileUtils.isDirectory(filePath)) {
            throw new OutputTargetValidationException(
                "target is a directory; please use a different output filename"
            );
        }
        else if (!canOverwriteExistingFiles && fileUtils.exists(filePath)) {
            throw new OutputTargetValidationException(
                "file already exists; please use a different output filename or use the --replace option"
            );
        }
        else if (!fileUtils.exists(filePath)) {
            Path parent = filePath.toAbsolutePath().getParent();
            if (!fileUtils.createDirectories(parent)) {
                throw new OutputTargetValidationException(
                    "parent directory of output file already exists but is not a directory; please use a different output filename"
                );
            }
        }
    }
}
