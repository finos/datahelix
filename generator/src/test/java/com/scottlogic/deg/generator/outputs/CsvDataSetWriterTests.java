package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.Value;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import com.scottlogic.deg.generator.generation.rows.Row;
import com.scottlogic.deg.generator.generation.rows.GeneratedObjectBuilder;
import com.scottlogic.deg.generator.outputs.datasetwriters.CsvDataSetWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;

public class CsvDataSetWriterTests {
    private final CSVFormat format = CSVFormat.DEFAULT.withEscape('\0').withQuoteMode(QuoteMode.NONE);
    Field field1 = new Field("field1");
    Field field2 = new Field("field2");
    ProfileFields profileFields = new ProfileFields(Collections.singletonList(field1));

    @Test
    public void writeRow_withBigDecimalAndNoFormat_shouldOutputDefaultFormat() throws IOException {
        // Arrange
        StringBuffer stringBuffer = new StringBuffer();
        Row row = GeneratedObjectBuilder.startBuilding().set(field1, new Value(field1, new BigDecimal("0.00000001"))).build();
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        writeToBuffer(printer, row, profileFields);

        // Assert
        Assert.assertEquals("0.00000001", stringBuffer.toString().trim());
    }

    @Test
    void writeRow_withBigDecimalAndAFormat_shouldOutputFormattedValue() throws IOException{
        // Arrange
        StringBuffer stringBuffer = new StringBuffer();
        Row row = GeneratedObjectBuilder.startBuilding().set(field1, getValueWithFormat(new BigDecimal("0.00000001"), "%.1e")).build();
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        writeToBuffer(printer, row, profileFields);

        // Assert
        Assert.assertEquals("\"1.0e-08\"", stringBuffer.toString().trim());
    }

    @Test
    void writeRow_withNullValue_shouldOutputEmptyValue() throws IOException {
        // Arrange
        StringBuffer stringBuffer = new StringBuffer();
        Row row = GeneratedObjectBuilder.startBuilding().set(field1, new Value(field1, null)).build();
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        writeToBuffer(printer, row, profileFields);

        // Assert
        Assert.assertEquals("", stringBuffer.toString().trim());
    }

    @Test
    void writeRow_withNonBigDecimalNumberAndNoFormat_shouldOutputNumberFormattedCorrectly() throws IOException {
        // Arrange
        StringBuffer stringBuffer = new StringBuffer();
        Row row = GeneratedObjectBuilder.startBuilding().set(field1, new Value(field1, 1.2f)).build();
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        writeToBuffer(printer, row, profileFields);

        // Assert
        Assert.assertEquals("1.2", stringBuffer.toString().trim());
    }

    @Test
    void writeRow_withStringAndFormat_shouldOutputValueQuotedAndFormatted() throws IOException {
        // Arrange
        StringBuffer stringBuffer = new StringBuffer();
        Row row = GeneratedObjectBuilder.startBuilding().set(field1, getValueWithFormat("Hello World", "%.5s")).build();
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        writeToBuffer(printer, row, profileFields);

        // Assert
        Assert.assertEquals("\"Hello\"", stringBuffer.toString().trim());
    }

    @Test
    void writeRow_withDateTimeGranularToASecondAndNoFormat_shouldFormatDateUsingISO8601Format() throws IOException {
        // Arrange
        OffsetDateTime date = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        StringBuffer stringBuffer = new StringBuffer();
        Row row = GeneratedObjectBuilder.startBuilding().set(field1, new Value(field1, date)).build();
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        writeToBuffer(printer, row, profileFields);

        // Assert
        Assert.assertEquals("2001-02-03T04:05:06Z", stringBuffer.toString().trim());
    }

    @Test
    void writeRow_withDateTimeGranularToAMillisecondAndNoFormat_shouldFormatDateUsingISO8601Format() throws IOException {
        // Arrange
        OffsetDateTime date = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 777_000_000, ZoneOffset.UTC);
        StringBuffer stringBuffer = new StringBuffer();
        Row row = GeneratedObjectBuilder.startBuilding().set(field1, new Value(field1, date)).build();
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        writeToBuffer(printer, row, profileFields);

        // Assert
        Assert.assertEquals("2001-02-03T04:05:06.777Z", stringBuffer.toString().trim());
    }

    @Test
    void writeRow_withDateTimeAndAFormat_shouldUsePrescribedFormat() throws IOException {
        // Arrange
        OffsetDateTime date = OffsetDateTime.of(2001, 02, 03, 04, 05, 06, 0, ZoneOffset.UTC);
        StringBuffer stringBuffer = new StringBuffer();
        Row row = GeneratedObjectBuilder.startBuilding().set(field1, getValueWithFormat(date, "%tF")).build();
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        writeToBuffer(printer, row, profileFields);

        // Assert
        Assert.assertEquals("\"2001-02-03\"", stringBuffer.toString().trim());
    }

    @Test
    void writeRow_withProfileFieldsOrder_generatesInOrder() throws IOException {
        String value1 = "value1";
        String value2 = "value2";
        StringBuffer stringBuffer = new StringBuffer();
        Row row = GeneratedObjectBuilder.startBuilding()
            .set(field1, new Value(field1, value1))
            .set(field2, new Value(field2, value2)).build();
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        profileFields = new ProfileFields(Arrays.asList(field1, field2));

        // Act
        writeToBuffer(printer, row, profileFields);

        // Assert
        Assert.assertEquals("\"value1\",\"value2\"", stringBuffer.toString().trim());
    }

    @Test
    void writeRow_withProfileFieldsReverseOrder_generatesInReverseOrder() throws IOException {
        String value1 = "value1";
        String value2 = "value2";
        StringBuffer stringBuffer = new StringBuffer();
        Row row = GeneratedObjectBuilder.startBuilding()
            .set(field1, new Value(field1, value1))
            .set(field2, new Value(field2, value2)).build();
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        profileFields = new ProfileFields(Arrays.asList(field2, field1));

        // Act
        writeToBuffer(printer, row, profileFields);

        // Assert
        Assert.assertEquals("\"value2\",\"value1\"", stringBuffer.toString().trim());
    }

    private Value getValueWithFormat(Object value, String format) {
        return new Value(field1, value, format, FieldSpecSource.Empty);
    }

    private void writeToBuffer(CSVPrinter printer, Row row, ProfileFields profileFields) throws IOException {
        CsvDataSetWriter writer = new CsvDataSetWriter();
        writer.writeRow(printer, row, profileFields);
        printer.close();
    }
}
