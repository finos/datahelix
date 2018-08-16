package com.scottlogic.deg.generator.outputs;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonTestCaseDataWriter implements IDataSetWriter {

    ObjectMapper mapper = new ObjectMapper();
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    @Override
    public String write(Profile profile,
                        TestCaseDataSet dataset,
                        Path directoryPath,
                        String filenameWithoutExtension) throws IOException {

        ArrayNode arrayNode = mapper.createArrayNode();

        String[] fieldNames = profile.fields.stream().map(x -> x.name).toArray(String[]::new);

        for (TestCaseDataRow row : dataset.enumerateRows()) {
            ObjectNode rowNode = mapper.createObjectNode();

            int i = 0;
            for (Object value : row.values) {
                String fieldName = fieldNames[i];

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

                i++;
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
