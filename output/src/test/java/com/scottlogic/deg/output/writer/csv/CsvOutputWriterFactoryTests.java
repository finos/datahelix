/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.output.writer.csv;

import com.scottlogic.deg.common.profile.FieldBuilder;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.output.writer.DataSetWriter;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.scottlogic.deg.common.profile.FieldBuilder.createField;
import static com.scottlogic.deg.common.profile.FieldBuilder.createInternalField;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsvOutputWriterFactoryTests {
    @Test
    void writeRow_withBigDecimalAndNoFormat_shouldOutputDefaultFormat() throws IOException {
        expectCsv(
            fields("my_number"),
            (new BigDecimal("0.00000001")),

            Matchers.containsString("0.00000001"));
    }

    @Test
    void writeRow_withNullValue_shouldOutputEmptyValue() throws IOException {
        expectCsv(
            fields("my_null"),
            (null),

            Matchers.equalTo("my_null\n\n"));
    }

    @Test
    void writeRow_withNonBigDecimalNumberAndNoFormat_shouldOutputNumberFormattedCorrectly() throws IOException {
        expectCsv(
            fields("my_number"),
            (1.2f),

            Matchers.equalTo("my_number\n1.2\n"));
    }

    @Test
    void writeRow_withDateTimeGranularToASecondAndNoFormat_shouldFormatDateUsingISO8601Format() throws IOException {
        OffsetDateTime date = OffsetDateTime.of(
            2001, 02, 03,
            04, 05, 06,0,
            ZoneOffset.UTC);

        expectCsv(
            fields("my_date"),
            (date),

            Matchers.containsString("2001-02-03T04:05:06Z"));
    }

    @Test
    void writeRow_withDateTimeGranularToAMillisecondAndNoFormat_shouldFormatDateUsingISO8601Format() throws IOException {
        OffsetDateTime date = OffsetDateTime.of(
            2001, 02, 03,
            04, 05, 06, 777_000_000,
            ZoneOffset.UTC);

        expectCsv(
            fields("my_date"),
            (date),

            Matchers.containsString("2001-02-03T04:05:06.777Z"));
    }

    @Test
    void writeRow_withInternalFields_shouldNotWriteInternalFields() throws IOException {
        Fields fields = new Fields(
            Arrays.asList(
                createField("External"),
                createInternalField("Internal")
            )
        );
        expectCsv(
            fields,
            (100),
            Matchers.equalTo("External\n100\n")
        );
    }

    private static Fields fields(String ...names) {
        return new Fields(
            Arrays.stream(names)
                .map(FieldBuilder::createField)
                .collect(Collectors.toList()));
    }

    private static void expectCsv(Fields fields, Object value, Matcher<String> matcher) throws IOException {
        // Act
        GeneratedObject mockGeneratedObject = mock(GeneratedObject.class);
        when(mockGeneratedObject.getFormattedValue(eq(fields.iterator().next()))).thenReturn(value);
        String generatedCsv = generateCsv(fields, mockGeneratedObject);

        // Assert
        Assert.assertThat(generatedCsv, matcher);
    }

    private static String generateCsv(Fields fields, GeneratedObject generatedObject) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try (DataSetWriter writer = new CsvOutputWriterFactory().createWriter(stream, fields)) {
            writer.writeRow(generatedObject);
        }

        return stream
            .toString(StandardCharsets.UTF_8.name())
            .replace("\r\n", "\n"); // normalise line endings between e.g. Windows and Linux
    }
}
