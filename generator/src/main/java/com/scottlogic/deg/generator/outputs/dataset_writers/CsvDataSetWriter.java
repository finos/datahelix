package com.scottlogic.deg.generator.outputs.dataset_writers;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvDataSetWriter implements IDataSetWriter {
    @Override
    public void write(
        ProfileFields profileFields,
        Stream<GeneratedObject> dataset,
        Path filePath) throws IOException {

        try (CSVPrinter writer = CSVFormat.RFC4180
            .withHeader(profileFields.stream()
                .map(f -> f.name)
                .toArray(String[]::new))
            .print(filePath, StandardCharsets.UTF_8)) {

            dataset.forEach(row -> {
                try {
                    writer.printRecord(row.values.stream().map(cell -> {
                        if (cell.value == null)
                            return null;

                        if (cell.format == null)
                            return cell.value;

                        return String.format(cell.format, cell.value);
                    }).collect(Collectors.toList()));

                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public String makeFilename(String filenameWithoutExtension) {
        return filenameWithoutExtension + ".csv";
    }
}
