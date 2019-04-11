package com.scottlogic.deg.generator.generation.rows;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Value;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class RowSourceFactory {
    private final FieldSpecValueGenerator dataBagValueGenerator;

    @Inject
    public RowSourceFactory(FieldSpecValueGenerator dataBagValueGenerator) {
        this.dataBagValueGenerator = dataBagValueGenerator;
    }

    public RowSource createRowSource(RowSpec rowSpec){
        List<Stream<Row>> fieldFowSources = new ArrayList<>();

        for (Field field: rowSpec.getFields()) {
            FieldSpec fieldSpec = rowSpec.getSpecForField(field);

            fieldFowSources.add(
                dataBagValueGenerator.generate(field, fieldSpec)
                    .map(this::toGeneratedObject)
            );
        }

        return new FieldCombiningRowSource(fieldFowSources);
    }

    private Row toGeneratedObject(Value value) {
        return new Row(new HashMap<Field, Value>() {{
            put(value.field, value); }});
    }
}
