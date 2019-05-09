package com.scottlogic.deg.generator.outputs.datasetwriters;

import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.util.ArrayList;
import java.util.Optional;


public class JsonFormatter implements RowOutputFormatter<String> {

    @Override
    public String format(GeneratedObject row) {
        ArrayList<String> strValue = new ArrayList<>();

        for (int i = 0; i < row.values.size(); i++) {
            String field = row.source.columns.get(i).field.toString();
            String value = Optional
                .ofNullable(row.values.get(i).getFormattedValue()).orElse("null")
                .toString();

            String desiredFormat = String.format("%s: %s", field, value);

            strValue.add(desiredFormat);
        }

        return String.format("{%s}", strValue.toString().substring(1, strValue.toString().length()-1));
    }
}
