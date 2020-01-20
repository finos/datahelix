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

package com.scottlogic.datahelix.generator.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    public boolean containsInvalidChars(File file) {
        return file.toString().matches(".*[?%*|><\"].*|^(?:[^:]*+:){2,}[^:]*+$");
    }

    public boolean isFileEmpty(File file) {
        return file.length() == 0;
    }

    public boolean exists(Path path) {
        return Files.exists(path);
    }

    public boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }

    /**
     * we wrap Files.createDirectories() so that we can mock it out in unit tests.
     *
     * @param dir the directory we want to create
     * @return true is the directory was created successfully,
     * false if the parent already exists and is a file
     * @throws IOException if we are unable to create the directory due to an I/O error
     */
    public boolean createDirectories(Path dir) throws IOException {
        try {
            checkValidDirectoryPath(dir);
            Files.createDirectories(dir);
        } catch (FileAlreadyExistsException e) {
            return false;
        }
        return true;
    }

    private void checkValidDirectoryPath(Path dir) throws FileAlreadyExistsException {
        if (Files.exists(dir) && !Files.isDirectory(dir)) {
            throw new FileAlreadyExistsException("Directory name exists as file");
        }
        Path prntDir = dir.getParent();
        if (prntDir != null) {
            checkValidDirectoryPath(prntDir);
        }
    }
}
