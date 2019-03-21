package com.scottlogic.deg.generator.utils;

import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;

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

    /**
     * @param path the Path to check the existence of.
     * @return true if the supplied FileOutputTarget exists on disk, false otherwise.
     */
    public boolean exists(Path path) {
        return Files.exists(path);
    }

    /**
     * @param path the Path to test.
     * @return true is the supplied FileOutputTarget is a directory, false otherwise.
     */
    public boolean isDirectory(Path path) {
        return Files.isDirectory(path);
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
        if (directoryContainsManifestJsonFile(filepath) ||
            directoryContainsFilesWithExt(filepath, "csv", fileCount)) {
            return false;
        }
        return true;
    }

    private boolean directoryContainsManifestJsonFile(Path filePath) {
        return Files.exists(Paths.get(filePath.toString(), "manifest.json"));
    }

    private boolean directoryContainsFilesWithExt(Path filePath, String ext, int fileCount) {
        DecimalFormat intFormatter = FileUtils.getDecimalFormat(fileCount);
        for (int x = 1; x <= fileCount; x++) {
            if (Files.exists(Paths.get(filePath.toString(), intFormatter.format(x) + "." + ext))) {
                return true;
            }
        }
        return false;
    }

    public File getTraceFile(GenerationConfigSource configSource) {
        String filenameWithoutExtension = configSource.getOutputPath().toString().replaceAll("\\.[^.]+$", "");
        return Paths.get(filenameWithoutExtension + "-trace.json").toFile();
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
