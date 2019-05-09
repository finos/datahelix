package com.scottlogic.deg.generator.outputs.datasetwriters;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvDataSetWriter implements DataSetWriter<CSVPrinter, List<Object>> {
    private static final CSVFormat writerFormat = CSVFormat.RFC4180;

    @Override
    public CSVPrinter openWriter(Path directory, String fileName, ProfileFields profileFields) throws IOException {
        return writerFormat
            .withEscape('\0') //Dont escape any character, we're formatting strings ourselves
            .withQuoteMode(QuoteMode.NONE)
            .withHeader(profileFields.stream()
                .map(f -> f.name)
                .toArray(String[]::new))
            .print(
                directory.resolve(fileName),
                StandardCharsets.UTF_8);
    }

    @Override
    public void writeRow(CSVPrinter writer, GeneratedObject row, RowOutputFormatter<List<Object>> formatter) throws IOException {
        writer.printRecord(formatter.format(row));

        writer.flush();
    }

    @Override
    public String getFileName(String fileNameWithoutExtension) {
        return fileNameWithoutExtension + ".csv";
    }
}
