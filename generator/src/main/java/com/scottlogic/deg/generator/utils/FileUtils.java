package com.scottlogic.deg.generator.utils;

import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;

import java.io.File;
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

    public boolean isEmpty(File file) {
        return file.length() == 0;
    }
     /**
     * @param target the FileOutputTarget to check the existence of.
     * @return true if the supplied FileOutputTarget exists on disk, false otherwise.
     */
    public boolean exists(FileOutputTarget target) {
        return Files.exists(target.getFilePath());
    }

    /**
     * @param target the FileOutputTarget to test.
     * @return true is the supplied FileOutputTarget is a directory, false otherwise.
     */
    public boolean isDirectory(FileOutputTarget target) {
        return Files.isDirectory(target.getFilePath());
    }

    /**
     * check for the existence of files named "<code>manifest.json</code>"
     * and "<code>/^[0-9]{filecount}.csv$/</code>" in the given directory.
     *
     * @param target the FileOutputTarget that contains the directory to test.
     * @param fileCount the number of files we will check for.
     * @return true if any of the files exist, false only if none of the files exist in the directory.
     */
    public boolean isDirectoryEmpty(FileOutputTarget target, int fileCount) {
        Path filepath = target.getFilePath();
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
}
