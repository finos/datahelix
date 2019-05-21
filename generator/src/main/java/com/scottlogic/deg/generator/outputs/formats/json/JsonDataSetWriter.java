package com.scottlogic.deg.generator.outputs.formats.json;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

class JsonDataSetWriter implements DataSetWriter {
    private static final DateTimeFormatter standardDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    private final SequenceWriter writer;
    private final ProfileFields fields;

    private JsonDataSetWriter(SequenceWriter writer, ProfileFields fields) {
        this.writer = writer;
        this.fields = fields;
    }

    static DataSetWriter open(OutputStream stream, ProfileFields fields) throws IOException {
        ObjectWriter objectWriter = new ObjectMapper().writer(new DefaultPrettyPrinter());
        SequenceWriter writer = objectWriter.writeValues(stream);
        writer.init(true);

        return new JsonDataSetWriter(writer, fields);
    }

    @Override
    public void writeRow(GeneratedObject row) throws IOException {
        Map<Field, Object> jsonObject = row.getFieldToValue().entrySet().stream().collect(
            Collectors.toMap(
                Map.Entry::getKey,
                e -> convertValue(e.getValue())));

        writer.write(jsonObject);
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }


    private static Object convertValue(DataBagValue dataBagValue) {
        Object value = dataBagValue.getFormattedValue();

        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return value;
        } else if (value instanceof String) {
            return value;
        } else if (value instanceof OffsetDateTime) {
            return standardDateFormat.format((OffsetDateTime)value);
        } else {
            return value.toString();
        }
    }
}
