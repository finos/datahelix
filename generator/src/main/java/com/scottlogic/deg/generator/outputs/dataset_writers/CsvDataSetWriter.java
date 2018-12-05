package com.scottlogic.deg.generator.outputs.dataset_writers;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class CsvDataSetWriter implements IDataSetWriter<CSVPrinter> {
    public CSVPrinter openWriter(Path directory, String filenameWithoutExtension, ProfileFields profileFields) throws IOException {
        return CSVFormat.RFC4180
            .withHeader(profileFields.stream()
                .map(f -> f.name)
                .toArray(String[]::new))
            .print(
                directory.resolve(filenameWithoutExtension + ".csv"),
                StandardCharsets.UTF_8);
    }

    public void writeRow(CSVPrinter writer, GeneratedObject row) throws IOException {
        writer.printRecord(row.values.stream().map(cell -> {
            if (cell.value == null) {
                return null;
            }

            if (cell.format == null) {
                return cell.value;
            }

            return String.format(cell.format, cell.value);
        }).collect(Collectors.toList()));

        writer.flush();
    }
}
