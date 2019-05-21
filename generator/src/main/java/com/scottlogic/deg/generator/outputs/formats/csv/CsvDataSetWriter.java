package com.scottlogic.deg.generator.outputs.formats.csv;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

class CsvDataSetWriter implements DataSetWriter {
    private static final DateTimeFormatter standardDateFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final CSVFormat writerFormat = CSVFormat.RFC4180;
    private static final CSVFormat csvStringFormatter = writerFormat.withQuoteMode(QuoteMode.ALL);

    @NotNull
    private final CSVPrinter csvPrinter;
    private final ProfileFields fieldOrder;

    private CsvDataSetWriter(@NotNull CSVPrinter csvPrinter, ProfileFields fieldOrder) {
        this.csvPrinter = csvPrinter;
        this.fieldOrder = fieldOrder;
    }

    static DataSetWriter open(OutputStream stream, ProfileFields fields) throws IOException {
        final Appendable outputStreamAsAppendable = new OutputStreamWriter(stream, StandardCharsets.UTF_8);

        CSVPrinter csvPrinter = writerFormat
            .withEscape('\0') //Dont escape any character, we're formatting strings ourselves
            .withQuoteMode(QuoteMode.NONE)
            .withHeader(fields.stream()
                .map(f -> f.name)
                .toArray(String[]::new))
            .print(outputStreamAsAppendable);

        return new CsvDataSetWriter(csvPrinter, fields);
    }

    @Override
    public void writeRow(GeneratedObject row) throws IOException {
        csvPrinter.printRecord(fieldOrder.stream()
                .map(row::getValueAndFormat)
                .map(CsvDataSetWriter::extractCellValue)
                .map(CsvDataSetWriter::wrapInQuotesIfString)
                .collect(Collectors.toList()));

        csvPrinter.flush();
    }

    @Override
    public void close() throws IOException {
        csvPrinter.close();
    }

    private static Object extractCellValue(DataBagValue cell) {
        return cell.getFormattedValue();
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
