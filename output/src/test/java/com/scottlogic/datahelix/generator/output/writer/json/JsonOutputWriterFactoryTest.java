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
package com.scottlogic.datahelix.generator.output.writer.json;

import com.scottlogic.datahelix.generator.common.output.GeneratedObject;
import com.scottlogic.datahelix.generator.common.profile.FieldBuilder;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.output.writer.DataSetWriter;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;
import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createInternalField;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonOutputWriterFactoryTest {
    @Test
    void writer_whereStreamingJson__shouldOutputNewLineDelimiterRows() throws IOException {
        Fields fields = new Fields(Collections.singletonList(FieldBuilder.createField("my_field")));

        expectJson(
            fields,
            true,
            Matchers.equalTo("{\"my_field\":\"my_value\"}\n{\"my_field\":\"my_value\"}"));
    }

    @Test
    void writer_whereNotStreamingJson__shouldOutputRowsWrappedInAnArray() throws IOException {
        Fields fields = new Fields(Collections.singletonList(FieldBuilder.createField("my_field")));

        expectJson(
            fields,
            false,
            Matchers.equalTo("[ {\n  \"my_field\" : \"my_value\"\n}, {\n  \"my_field\" : \"my_value\"\n} ]"));
    }

    @Test
    void writeRow_withInternalFields_shouldNotWriteInternalFields() throws IOException {
        Fields fields = new Fields(
            Arrays.asList(
                createField("External"),
                createInternalField("Internal")
            )
        );
        expectJson(
            fields,
            true,
            Matchers.equalTo("{\"External\":\"my_value\"}\n{\"External\":\"my_value\"}")
        );
    }

    private static void expectJson(Fields fields, boolean streamOutput, Matcher<String> matcher) throws IOException {
        // Act
        GeneratedObject mockGeneratedObject = mock(GeneratedObject.class);
        when(mockGeneratedObject.getFormattedValue(eq(fields.iterator().next()))).thenReturn("my_value");
        String generateJson = generateJson(fields, mockGeneratedObject, streamOutput);

        // Assert
        Assert.assertThat(generateJson, matcher);
    }

    private static String generateJson(Fields fields, GeneratedObject generatedObject, boolean streamOutput) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try (DataSetWriter writer = new JsonOutputWriterFactory(streamOutput).createWriter(stream, fields)) {
            writer.writeRow(generatedObject);
            writer.writeRow(generatedObject);
        }

        return stream
            .toString(StandardCharsets.UTF_8.name())
            .replace("\r\n", "\n"); // normalise line endings between e.g. Windows and Linux
    }
}