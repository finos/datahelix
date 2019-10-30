package com.scottlogic.deg.output.writer.json;

import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.FieldBuilder;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.output.writer.DataSetWriter;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static com.scottlogic.deg.common.profile.FieldBuilder.createField;
import static com.scottlogic.deg.common.profile.FieldBuilder.createInternalField;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonOutputWriterFactoryTest {

    @Test
    void writer_withNDJSONTrue__shouldOutputNewLineDelimiterRows() throws IOException {
        Fields fields = new Fields(Collections.singletonList(FieldBuilder.createField("my_field")));

        expectJson(
            fields,
            true,
            Matchers.equalTo("{\n  \"my_field\" : \"my_value\"\n}\n{\n  \"my_field\" : \"my_value\"\n}"));
    }

    @Test
    void writer_withNDJSONFalse__shouldOutputRowsWrappedInAnArray() throws IOException {
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
            Matchers.equalTo("{\n  \"External\" : \"my_value\"\n}\n{\n  \"External\" : \"my_value\"\n}")
        );
    }

    private static void expectJson(Fields fields, boolean useNdJson, Matcher<String> matcher) throws IOException {
        // Act
        GeneratedObject mockGeneratedObject = mock(GeneratedObject.class);
        when(mockGeneratedObject.getFormattedValue(eq(fields.iterator().next()))).thenReturn("my_value");
        String generateJson = generateJson(fields, mockGeneratedObject, useNdJson);

        // Assert
        Assert.assertThat(generateJson, matcher);
    }

    private static String generateJson(Fields fields, GeneratedObject generatedObject, boolean useNdJson) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try (DataSetWriter writer = new JsonOutputWriterFactory(useNdJson).createWriter(stream, fields)) {
            writer.writeRow(generatedObject);
            writer.writeRow(generatedObject);
        }

        return stream
            .toString(StandardCharsets.UTF_8.name())
            .replace("\r\n", "\n"); // normalise line endings between e.g. Windows and Linux
    }
}