package com.scottlogic.deg.generator.utils;

import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;

import java.io.IOException;
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


    public boolean exists(FileOutputTarget target) {
        return Files.exists(target.getFilePath());
    }

    public boolean isDirectory(FileOutputTarget target) {
        return Files.isDirectory(target.getFilePath());
    }

    public boolean isDirectoryEmpty(FileOutputTarget target, int fileCount) {
        try {
            Path filepath = target.getFilePath();
            if (directoryContainsManifestJsonFile(filepath) ||
                directoryContainsFilesWithExt(filepath, "csv", fileCount)) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private boolean directoryContainsManifestJsonFile(Path filePath) {
        return Files.exists(Paths.get(filePath.toString(), "manifest.json"));
    }

    private boolean directoryContainsFilesWithExt(Path filePath, String ext, int fileCount) throws IOException {
        DecimalFormat intFormatter = FileUtils.getDecimalFormat(fileCount);
        for (int x = 1; x <= fileCount; x++) {
            if (Files.exists(Paths.get(filePath.toString(), intFormatter.format(x) + "." + ext))) {
                return true;
            }
        }
        return false;
    }
}
