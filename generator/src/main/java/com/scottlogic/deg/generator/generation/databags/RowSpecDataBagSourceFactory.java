package com.scottlogic.deg.generator.generation.databags;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.ReductiveRowSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RowSpecDataBagSourceFactory {
    private final FieldSpecValueGenerator generator;

    @Inject
    public RowSpecDataBagSourceFactory(FieldSpecValueGenerator generator) {
        this.generator = generator;
    }

    public DataBagSource createDataBagSource(RowSpec rowSpec){
        if (rowSpec instanceof ReductiveRowSpec){
            return createReductiveDataBagSourceFor((ReductiveRowSpec) rowSpec);
        }

        List<DataBagSource> fieldDataBagSources = new ArrayList<>(rowSpec.getFields().size());

        for (Field field: rowSpec.getFields()) {
            FieldSpec fieldSpec = rowSpec.getSpecForField(field);

            fieldDataBagSources.add(
                new StreamDataBagSource(generator.generate(field, fieldSpec)));
        }

        return new MultiplexingDataBagSource(fieldDataBagSources.stream());
    }

    private DataBagSource createReductiveDataBagSourceFor(ReductiveRowSpec rowSpec) {
        List<DataBagSource> fieldDataBagSources = new ArrayList<>(rowSpec.getFields().size() - 1);
        Field lastFixedField = rowSpec.lastFixedField;

        for (Field field: rowSpec.getFields()) {
            if (field.equals(lastFixedField)){
                continue;
            }

            FieldSpec fieldSpec = rowSpec.getSpecForField(field);

            fieldDataBagSources.add(
                new SingleValueDataBagSource(
                    new StreamDataBagSource(generator.generate(field, fieldSpec))));
        }

        DataBagSource sourceWithoutLastFixedField = new MultiplexingDataBagSource(fieldDataBagSources.stream());
        return new MultiplyingDataBagSource(
            sourceWithoutLastFixedField,
            new StreamDataBagSource(
                generator.generate(
                    lastFixedField,
                    rowSpec.getSpecForField(lastFixedField))));
    }

    class MultiplyingDataBagSource implements DataBagSource {

        private final DataBagSource fieldsForAllFixedFields;
        private final DataBagSource valuesForLastField;

        MultiplyingDataBagSource(DataBagSource fieldsForAllFixedFields, DataBagSource valuesForLastField) {
            this.fieldsForAllFixedFields = fieldsForAllFixedFields;
            this.valuesForLastField = valuesForLastField;
        }

        @Override
        public Stream<DataBag> generate(GenerationConfig generationConfig) {
            Stream<DataBag> valuesForLastField = this.valuesForLastField.generate(generationConfig);
            DataBag singleValuePerField = this.fieldsForAllFixedFields
                .generate(generationConfig)
                .reduce(
                    DataBag.empty,
                    (prev, current) -> DataBag.merge(prev, current));

            return valuesForLastField.map(lastFieldValue -> DataBag.merge(lastFieldValue, singleValuePerField));
        }
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

