package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.generator.outputs.dataset_writers.CsvDataSetWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.cglib.core.Local;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

public class CsvDataSetWriterTests {
    private final CSVFormat format = CSVFormat.DEFAULT.withEscape('\0').withQuoteMode(QuoteMode.NONE);
    @Test
    public void bigDecimalNoFormatShouldOutputCorrectly() throws IOException {
        // Arrange
        StringBuffer stringBuffer = new StringBuffer();
        GeneratedObject generatedObject = new GeneratedObject(
            Collections.singletonList(getValue(new BigDecimal("0.00000001"))),
            new RowSource(Collections.emptyList()));
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        WriteToBuffer(printer, generatedObject);

        // Assert
        Assert.assertEquals("0.00000001", stringBuffer.toString().trim());
    }

    @Test
    void bigDecimalWithFormatShouldOutputCorrectly() throws IOException{
        // Arrange
        StringBuffer stringBuffer = new StringBuffer();
        GeneratedObject generatedObject = new GeneratedObject(
            Collections.singletonList(getValueWithFormat(new BigDecimal("0.00000001"), "%.1e")), // Formats the bigDecimal into Scientific notation
            new RowSource(Collections.emptyList()));
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        WriteToBuffer(printer, generatedObject);

        // Assert
        Assert.assertEquals("\"1.0e-08\"", stringBuffer.toString().trim());
    }

    @Test
    void nullShouldOutputCorrectly() throws IOException {
        // Arrange
        StringBuffer stringBuffer = new StringBuffer();
        GeneratedObject generatedObject = new GeneratedObject(
            Collections.singletonList(getValue(null)),
            new RowSource(Collections.emptyList()));
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        WriteToBuffer(printer, generatedObject);

        // Assert
        Assert.assertEquals("", stringBuffer.toString().trim());
    }

    @Test
    void nonBigDecimalNoFormatShouldOutputCorrectly() throws IOException {
        // Arrange
        StringBuffer stringBuffer = new StringBuffer();
        GeneratedObject generatedObject = new GeneratedObject(
            Collections.singletonList(new DataBagValue(1.2f, DataBagValueSource.Empty)),
            new RowSource(Collections.emptyList()));
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        WriteToBuffer(printer, generatedObject);

        // Assert
        Assert.assertEquals("1.2", stringBuffer.toString().trim());
    }

    @Test
    void nonBigDecimalWithFormatShouldOutputCorrectly() throws IOException {
        // Arrange
        StringBuffer stringBuffer = new StringBuffer();
        GeneratedObject generatedObject = new GeneratedObject(
            Collections.singletonList(getValueWithFormat("Hello World", "%.5s")), // Format string to max 5 chars
            new RowSource(Collections.emptyList()));
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        WriteToBuffer(printer, generatedObject);

        // Assert
        Assert.assertEquals("\"Hello\"", stringBuffer.toString().trim());
    }

    @Test
    void dateTimeWithNoFormatShouldUseDefaultFormat() throws IOException {
        // Arrange
        LocalDateTime date = LocalDateTime.of(2001, 02, 03, 04, 05, 06);
        StringBuffer stringBuffer = new StringBuffer();
        GeneratedObject generatedObject = new GeneratedObject(
            Collections.singletonList(getValue(date)), // Format string to max 5 chars
            new RowSource(Collections.emptyList()));
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        WriteToBuffer(printer, generatedObject);

        // Assert
        Assert.assertEquals("03-02-2001 04:05:06", stringBuffer.toString().trim());
    }

    @Test
    void dateTimeWithNFormatShouldUsePrescribedFormat() throws IOException {
        // Arrange
        LocalDateTime date = LocalDateTime.of(2001, 02, 03, 04, 05, 06);
        StringBuffer stringBuffer = new StringBuffer();
        GeneratedObject generatedObject = new GeneratedObject(
            Collections.singletonList(getValueWithFormat(date, "%tF")), // Format string to max 5 chars
            new RowSource(Collections.emptyList()));
        CSVPrinter printer = new CSVPrinter(stringBuffer, format);

        // Act
        WriteToBuffer(printer, generatedObject);

        // Assert
        Assert.assertEquals("\"2001-02-03\"", stringBuffer.toString().trim());
    }

    private DataBagValue getValue(Object value) {
        return new DataBagValue(value, DataBagValueSource.Empty);
    }

    private DataBagValue getValueWithFormat(Object value, String format) {
        return new DataBagValue(value, format, DataBagValueSource.Empty);
    }

    private void WriteToBuffer(CSVPrinter printer, GeneratedObject generatedObject) throws IOException {
        CsvDataSetWriter writer = new CsvDataSetWriter();
        writer.writeRow(printer, generatedObject);
        printer.close();
    }
}
