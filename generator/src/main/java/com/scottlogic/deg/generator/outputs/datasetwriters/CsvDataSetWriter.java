package com.scottlogic.deg.generator.outputs.datasetwriters;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.ProfileFields;
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
import java.util.stream.Collectors;

public class CsvDataSetWriter implements DataSetWriter<CSVPrinter> {
    private static final DateTimeFormatter standardDateFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final CSVFormat writerFormat = CSVFormat.RFC4180;
    private static final CSVFormat csvStringFormatter = writerFormat.withQuoteMode(QuoteMode.ALL);

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

    public void writeRow(CSVPrinter writer, GeneratedObject row) throws IOException {
        writer.printRecord(row.values.stream()
            .map(CsvDataSetWriter::extractCellValue)
            .map(CsvDataSetWriter::wrapInQuotesIfString)
            .collect(Collectors.toList()));

        writer.flush();
    }

    @Override
    public String getFileName(String fileNameWithoutExtension) {
        return fileNameWithoutExtension + ".csv";
    }

    private static Object extractCellValue(DataBagValue cell) {
        if (cell.value == null) {
            return null;
        }

        return cell.format != null
            ? String.format(cell.format, cell.value)
            : cell.value;
    }

    private static Object wrapInQuotesIfString(Object value){
        if (value == null){
            return null;
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }

        if (value instanceof OffsetDateTime){
            return standardDateFormat.format((OffsetDateTime) value);
        }

        if (value instanceof String){
            return csvStringFormatter.format(value);
        }

        return value;
    }
}
