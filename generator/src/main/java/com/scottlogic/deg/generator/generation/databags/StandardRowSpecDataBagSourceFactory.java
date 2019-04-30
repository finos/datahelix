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

        List<StreamDataBagSource> fieldDataBagSources = new ArrayList<>(rowSpec.getFields().size());

        for (Field field: rowSpec.getFields()) {
            FieldSpec fieldSpec = rowSpec.getSpecForField(field);

            fieldDataBagSources.add(
                new StreamDataBagSource(generator.generate(field, fieldSpec)));
        }

        return config.getCombinationStrategy().permute(
            fieldDataBagSources.stream()
                .map(StreamDataBagSource::generate));
    }

    class StreamDataBagSource {
        private final Stream<DataBag> dataBags;

        StreamDataBagSource(Stream<DataBag> dataBags) {
            this.dataBags = dataBags;
        }

        public Stream<DataBag> generate() {
            return dataBags;
        }
    }
}
