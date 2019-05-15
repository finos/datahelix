package com.scottlogic.deg.generator.outputs.formats.csv;

import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.RowSource;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

class CsvOutputFormatTests {
    @Test
    void writeRow_withBigDecimalAndNoFormat_shouldOutputDefaultFormat() throws IOException {
        expectCsv(
            fields("my_number"),
            unformattedValue(new BigDecimal("0.00000001")),

            containsString("0.00000001"));
    }

    @Test
    void writeRow_withBigDecimalAndAFormat_shouldOutputFormattedValue() throws IOException{
        expectCsv(
            fields("my_number"),
            formattedValue(new BigDecimal("0.00000001"), "%.1e"),

            containsString("\"1.0e-08\""));
    }

    @Test
    void writeRow_withNullValue_shouldOutputEmptyValue() throws IOException {
        expectCsv(
            fields("my_null"),
            unformattedValue(null),

            equalTo("my_null\n\n"));
    }

    @Test
    void writeRow_withNonBigDecimalNumberAndNoFormat_shouldOutputNumberFormattedCorrectly() throws IOException {
        expectCsv(
            fields("my_number"),
            unformattedValue(1.2f),

            equalTo("my_number\n1.2\n"));
    }

    @Test
    void writeRow_withStringAndFormat_shouldOutputValueQuotedAndFormatted() throws IOException {
        expectCsv(
            fields("my_string"),
            formattedValue("Hello World", "%.5s"), // Format string to max 5 chars

            containsString("\"Hello\""));
    }

    @Test
    void writeRow_withDateTimeGranularToASecondAndNoFormat_shouldFormatDateUsingISO8601Format() throws IOException {
        OffsetDateTime date = OffsetDateTime.of(
            2001, 02, 03,
            04, 05, 06,0,
            ZoneOffset.UTC);

        expectCsv(
            fields("my_date"),
            unformattedValue(date),

            containsString("2001-02-03T04:05:06Z"));
    }

    @Test
    void writeRow_withDateTimeGranularToAMillisecondAndNoFormat_shouldFormatDateUsingISO8601Format() throws IOException {
        OffsetDateTime date = OffsetDateTime.of(
            2001, 02, 03,
            04, 05, 06, 777_000_000,
            ZoneOffset.UTC);

        expectCsv(
            fields("my_date"),
            unformattedValue(date),

            containsString("2001-02-03T04:05:06.777Z"));
    }

    @Test
    void writeRow_withDateTimeAndAFormat_shouldUsePrescribedFormat() throws IOException {
        OffsetDateTime date = OffsetDateTime.of(
            2001, 02, 03,
            04, 05, 06, 0,
            ZoneOffset.UTC);

        expectCsv(
            fields("my_date"),
            formattedValue(date, "%tF"),

            containsString("\"2001-02-03\""));
    }

    private static GeneratedObject formattedValue(Object value, String format) {
        return new GeneratedObject(
            Collections.singletonList(
                new DataBagValue(value, format, DataBagValueSource.Empty)), // Format string to max 5 chars
            new RowSource(Collections.emptyList()));
    }

    private static GeneratedObject unformattedValue(Object value) {
        return new GeneratedObject(
            Collections.singletonList(
                new DataBagValue(value, DataBagValueSource.Empty)),
            new RowSource(Collections.emptyList()));
    }

    private static ProfileFields fields(String ...names) {
        return new ProfileFields(
            Arrays.stream(names)
                .map(Field::new)
                .collect(Collectors.toList()));
    }

    private static void expectCsv(ProfileFields fields, GeneratedObject generatedObject, Matcher<String> matcher) throws IOException {
        // Act
        String generatedCsv = generateCsv(fields, generatedObject);

        // Assert
        Assert.assertThat(generatedCsv, matcher);
    }

    private static String generateCsv(ProfileFields fields, GeneratedObject generatedObject) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try (DataSetWriter writer = new CsvOutputFormat().createWriter(stream, fields)) {
            writer.writeRow(generatedObject);
        }

        return stream
            .toString(StandardCharsets.UTF_8.name())
            .replace("\r\n", "\n"); // normalise line endings between e.g. Windows and Linux
    }
}
