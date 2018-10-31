package com.scottlogic.deg.generator.outputs.dataset_writers;

import com.scottlogic.deg.generator.Generate;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvDataSetWriter implements IDataSetWriter {
    @Override
    public void write(
        ProfileFields profileFields,
        Stream<GeneratedObject> dataset,
        Path filePath) throws IOException {

        CSVPrinter writer =
            CSVFormat.RFC4180
                .withHeader(profileFields.stream()
                    .map(f -> f.name)
                    .toArray(String[]::new))
                .print(filePath, Charset.forName("UTF-8"));

        try {
            for (GeneratedObject row : (Iterable<GeneratedObject>)dataset::iterator) {
                writer.printRecord(row.values.stream().map(x -> {
                    if (x.value == null)
                        return null;

                    if (x.format == null)
                        return x.value;

                    return String.format(x.format, x.value);
                }).collect(Collectors.toList()));
            }
        }
        finally {
            writer.close();
        }
    }

    @Override
    public String makeFilename(String filenameWithoutExtension) {
        return filenameWithoutExtension + ".csv";
    }
}
