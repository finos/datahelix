package com.scottlogic.deg.generator.outputs.dataset_writers;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Iterator;

public class JsonDataSetWriter implements DataSetWriter<JsonDataSetWriter.JsonWriter> {
    private static final SimpleDateFormat standardDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    @Override
    public JsonWriter openWriter(Path directory, String filenameWithoutExtension, ProfileFields profileFields) {
        return new JsonWriter(directory.resolve(filenameWithoutExtension + ".json"), profileFields);
    }

    @Override
    public void writeRow(JsonDataSetWriter.JsonWriter writer, GeneratedObject row) {
        //TODO: Change this type to write progressively to the JSON file, currently it holds all rows in memory: Issue: #256

        ObjectNode rowNode = writer.jsonObjectMapper.createObjectNode();

        Iterator<DataBagValue> dataBagIterator = row.values.iterator();
        Iterator<Field> fieldNameIterator = writer.profileFields.iterator();

        while(dataBagIterator.hasNext() && fieldNameIterator.hasNext()){
            String fieldName = fieldNameIterator.next().name;
            DataBagValue dataBagValue = dataBagIterator.next();

            Object value = dataBagValue.value;

            if(dataBagValue.format != null){
                value = String.format(dataBagValue.format, value);
            }

            if (value == null) {
                rowNode.put(fieldName, (String) null);
            } else if (value instanceof BigDecimal) {
                rowNode.put(fieldName, (BigDecimal) value);
            } else if (value instanceof String) {
                rowNode.put(fieldName, (String) value);
            } else if (value instanceof LocalDateTime) {
                rowNode.put(fieldName, standardDateFormat.format(value));
            } else {
                rowNode.put(fieldName, value.toString());
            }
        }

        writer.arrayNode.add(rowNode);
    }

    public class JsonWriter implements Closeable {
        private final ObjectMapper jsonObjectMapper;
        private final ObjectWriter writer;
        private final ArrayNode arrayNode;
        private final Path filePath;
        private final ProfileFields profileFields;

        public JsonWriter(Path filePath, ProfileFields profileFields) {
            this.filePath = filePath;
            this.profileFields = profileFields;
            this.jsonObjectMapper = new ObjectMapper();
            this.writer = jsonObjectMapper.writer(new DefaultPrettyPrinter());
            this.arrayNode = jsonObjectMapper.createArrayNode();
        }

        @Override
        public void close() throws IOException {
            this.writer.writeValue(filePath.toFile(), arrayNode);
        }
    }
}
