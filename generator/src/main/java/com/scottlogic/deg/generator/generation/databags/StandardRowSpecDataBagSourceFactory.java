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
    private final GenerationConfig config;

    @Inject
    public StandardRowSpecDataBagSourceFactory (
        FieldSpecValueGenerator generator,
        GenerationConfig config) {
        this.generator = generator;
        this.config = config;
    }

    public Stream<DataBag> createDataBagSource(RowSpec rowSpec){

        List<DataBagSource> fieldDataBagSources = new ArrayList<>(rowSpec.getFields().size());

        for (Field field: rowSpec.getFields()) {
            FieldSpec fieldSpec = rowSpec.getSpecForField(field);

            fieldDataBagSources.add(
                new StreamDataBagSource(generator.generate(field, fieldSpec)));
        }

        return new MultiplexingDataBagSource(fieldDataBagSources.stream()).generate(config);
    }

    class StreamDataBagSource implements DataBagSource{
        private final Stream<DataBag> dataBags;

        StreamDataBagSource(Stream<DataBag> dataBags) {
            this.dataBags = dataBags;
        }

        @Override
        public Stream<DataBag> generate(GenerationConfig generationConfig) {
            return dataBags;
        }
    }
}
