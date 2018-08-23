package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.Profile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class CsvTestCaseDataSetWriter implements IDataSetWriter {

    @Override
    public String write(
            Profile profile,
            TestCaseDataSet dataset,
            Path directoryPath,
            String filenameWithoutExtension) throws IOException {

        String filename = filenameWithoutExtension + ".csv";
        Path fileAbsolutePath = directoryPath.resolve(filename);

        System.out.println("  " + filename);

        CSVPrinter writer =
            CSVFormat.RFC4180
                .withHeader(profile.fields.stream()
                    .map(f -> f.name)
                    .toArray(String[]::new))
                .print(fileAbsolutePath, Charset.forName("UTF-8"));

        try {
            for (TestCaseDataRow row : dataset) {
                writer.printRecord(row.values.stream().map(x -> {

                    return x.format != null ? String.format(x.format, x.value) : x.value;
                }).collect(Collectors.toList()));
            }
        }
        finally {
            writer.close();
        }

        return filename;
    }
}
