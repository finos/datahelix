package com.scottlogic.deg.generator.generation.databags;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;

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

        List<DataBagSource> fieldDataBagSources = new ArrayList<>(rowSpec.getFields().size());

        for (Field field: rowSpec.getFields()) {
            FieldSpec fieldSpec = rowSpec.getSpecForField(field);

            fieldDataBagSources.add(
                new StreamDataBagSource(generator.generate(field, fieldSpec)));
        }

        return new MultiplexingDataBagSource(fieldDataBagSources.stream());
    }

    class StreamDataBagSource implements DataBagSource{
        private final Stream<GeneratedObject> dataBags;

        StreamDataBagSource(Stream<GeneratedObject> dataBags) {
            this.dataBags = dataBags;
        }

        @Override
        public Stream<GeneratedObject> generate(GenerationConfig generationConfig) {
            return dataBags;
        }
    }
}
