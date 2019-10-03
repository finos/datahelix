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

package com.scottlogic.deg.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.stream.IntStream;

public class FileUtils {
    /**
     * Provides a decimal formatter for pre-padding numbers with just enough zeroes.
     * E.g. if there are 42 records in a dataset this will produce a formatter of "00"
     *
     * @param numberOfDataSets Maximum number of data sets we will number
     * @return Decimal format comprised of n zeroes where n is the number of digits in the input integer.
     */
    public static DecimalFormat getDecimalFormat(int numberOfDataSets) {
        int maxNumberOfDigits = (int) Math.ceil(Math.log10(numberOfDataSets));

        char[] zeroes = new char[maxNumberOfDigits];
        Arrays.fill(zeroes, '0');

        return new DecimalFormat(new String(zeroes));
    }

    public boolean containsInvalidChars(File file) {
        return file.toString().matches(".*[?%*|><\"].*|^(?:[^:]*+:){2,}[^:]*+$");
    }

    public boolean isFileEmpty(File file) {
        return file.length() == 0;
    }

    public boolean exists(Path path) {
        return path.toFile().exists();
    }

    public boolean isDirectory(Path path) {
        return path.toFile().isDirectory();
    }

    /**
     * check for the existence of files named "<code>manifest.json</code>"
     * and "<code>/^[0-9]{filecount}.csv$/</code>" in the given directory.
     *
     * @param filepath    the Path that contains the directory to test.
     * @param fileCount the number of files we will check for.
     * @return true if any of the files exist, false only if none of the files exist in the directory.
     */
    public boolean isDirectoryEmpty(Path filepath, int fileCount) {
        return !directoryContainsManifestJsonFile(filepath) && !directoryContainsFilesWithExt(filepath, "csv", fileCount);
    }

    private boolean directoryContainsManifestJsonFile(Path filePath) {
        return Paths.get(filePath.toString(), "manifest.json").toFile().exists();
    }

    private boolean directoryContainsFilesWithExt(Path filePath, String ext, int fileCount) {
        DecimalFormat intFormatter = FileUtils.getDecimalFormat(fileCount);
        return IntStream.rangeClosed(1, fileCount).anyMatch(x -> Paths.get(filePath.toString(), intFormatter.format(x) + "." + ext).toFile().exists());
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
        if (dir.toFile().exists() && !dir.toFile().isDirectory()) {
            throw new FileAlreadyExistsException("Directory name exists as file");
        }
        Path prntDir = dir.getParent();
        if (prntDir != null) {
            checkValidDirectoryPath(prntDir);
        }
    }
}
