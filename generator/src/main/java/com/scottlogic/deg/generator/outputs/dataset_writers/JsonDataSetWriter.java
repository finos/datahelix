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

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Iterator;

public class JsonDataSetWriter implements IDataSetWriter {
    private static final SimpleDateFormat standardDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    @Override
    public void write(
        ProfileFields profileFields,
        Iterable<GeneratedObject> dataset,
        Path filePath) throws IOException {

        ObjectMapper jsonObjectMapper = new ObjectMapper();

        ArrayNode arrayNode = jsonObjectMapper.createArrayNode();

        for (GeneratedObject row : dataset) {
            ObjectNode rowNode = jsonObjectMapper.createObjectNode();

            Iterator<DataBagValue> dataBagIterator = row.values.iterator();
            Iterator<Field> fieldNameIterator = profileFields.iterator();

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

            arrayNode.add(rowNode);
        }

        ObjectWriter writer = jsonObjectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(filePath.toFile(), arrayNode);
    }

    @Override
    public String makeFilename(String filenameWithoutExtension) {
        return filenameWithoutExtension + ".json";
    }
}
