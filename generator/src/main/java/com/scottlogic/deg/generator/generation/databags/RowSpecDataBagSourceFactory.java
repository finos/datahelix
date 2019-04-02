package com.scottlogic.deg.generator.generation.databags;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StandardRowSpecDataBagSourceFactory implements RowSpecDataBagSourceFactory {
    private final FieldSpecValueGenerator generator;

    @Inject
    public StandardRowSpecDataBagSourceFactory (
        FieldSpecValueGenerator generator) {
        this.generator = generator;
    }

    public DataBagSource createDataBagSource(RowSpec rowSpec){
        List<Stream<GeneratedObject>> fieldDataBagSources = new ArrayList<>(rowSpec.getFields().size());

        for (Field field: rowSpec.getFields()) {
            FieldSpec fieldSpec = rowSpec.getSpecForField(field);

            fieldDataBagSources.add(
                generator.generate(field, fieldSpec)
                    .map(this::toGeneratedObject)
            );
        }

        return new FieldCombiningDataBagSource(fieldDataBagSources);
    }

    private GeneratedObject toGeneratedObject(DataBagValue dataBagValue) {
        return GeneratedObject.startBuilding().set(dataBagValue.getField(), dataBagValue).build();
    }
}
