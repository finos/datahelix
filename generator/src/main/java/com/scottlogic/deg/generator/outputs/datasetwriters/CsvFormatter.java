package com.scottlogic.deg.generator.outputs.datasetwriters;

import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CsvFormatter implements RowOutputFormatter<List<Object>> {
    private static final DateTimeFormatter standardDateFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final CSVFormat writerFormat = CSVFormat.RFC4180;
    private static final CSVFormat csvStringFormatter = writerFormat.withQuoteMode(QuoteMode.ALL);

    @Override
    public List<Object> format(GeneratedObject row) {
        return row.values.stream()
            .map(CsvFormatter::extractCellValue)
            .map(CsvFormatter::wrapInQuotesIfString)
            .collect(Collectors.toList());
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
