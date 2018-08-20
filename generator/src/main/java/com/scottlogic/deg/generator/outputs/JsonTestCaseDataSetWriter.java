package com.scottlogic.deg.generator.outputs;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

public class JsonTestCaseDataSetWriter implements IDataSetWriter {

    ObjectMapper mapper = new ObjectMapper();
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    @Override
    public String write(Profile profile,
                        TestCaseDataSet dataset,
                        Path directoryPath,
                        String filenameWithoutExtension) throws IOException {

        ArrayNode arrayNode = mapper.createArrayNode();

        for (TestCaseDataRow row : dataset) {
            ObjectNode rowNode = mapper.createObjectNode();

            Iterator<DataBagValue> dataBagIterator = row.values.iterator();
            Iterator<Field> fieldNameIterator = profile.fields.iterator();

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
                    rowNode.put(fieldName, df.format(value));
                } else {
                    rowNode.put(fieldName, value.toString());
                }

            }

            arrayNode.add(rowNode);
        }

        String filename = filenameWithoutExtension + ".json";
        Path fileAbsolutePath = directoryPath.resolve(filename);

        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(fileAbsolutePath.toFile(), arrayNode);

        return filename;
    }
}
