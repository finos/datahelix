package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagSource;
import com.scottlogic.deg.generator.generation.databags.MultiplexingDataBagSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class RowSpecDataBagSourceFactory {
    public DataBagSource createDataBagSource(RowSpec rowSpec){
        if (rowSpec instanceof ReductiveRowSpec){
            return createDataBagSource((ReductiveRowSpec) rowSpec);
        }

        List<DataBagSource> fieldDataBagSources = new ArrayList<>(rowSpec.fieldToFieldSpec.size());

        for (Map.Entry<Field, FieldSpec> entry : rowSpec.fieldToFieldSpec.entrySet()) {
            FieldSpec fieldSpec = entry.getValue();
            Field field = entry.getKey();

            fieldDataBagSources.add(
                new FieldSpecValueGenerator(field, fieldSpec));
        }

        return new MultiplexingDataBagSource(fieldDataBagSources.stream());
    }

    public DataBagSource createDataBagSource(ReductiveRowSpec rowSpec) {
        List<DataBagSource> fieldDataBagSources = new ArrayList<>(rowSpec.getFields().size() - 1);

        for (Map.Entry<Field, FieldSpec> entry: rowSpec.fieldToFieldSpec.entrySet()) {
            Field field = entry.getKey();

            if (field.equals(rowSpec.lastFixedField)){
                continue;
            }

            FieldSpec fieldSpec = entry.getValue();

            fieldDataBagSources.add(
                new SingleValueDataBagSource(
                    new FieldSpecValueGenerator(field, fieldSpec)));
        }

        DataBagSource sourceWithoutLastFixedField = new MultiplexingDataBagSource(fieldDataBagSources.stream());
        return new MultiplyingDataBagSource(
            sourceWithoutLastFixedField,
            new FieldSpecValueGenerator(rowSpec.lastFixedField, rowSpec.fieldToFieldSpec.get(rowSpec.lastFixedField)));
    }

    class SingleValueDataBagSource implements DataBagSource {
        private final DataBagSource source;

        public SingleValueDataBagSource(DataBagSource source) {
            this.source = source;
        }

        @Override
        public Stream<DataBag> generate(GenerationConfig generationConfig) {
            return source.generate(generationConfig)
                .limit(1);
        }
    }

    class MultiplyingDataBagSource implements DataBagSource {

        private final DataBagSource fieldsForAllFixedFields;
        private final DataBagSource valuesForLastField;

        public MultiplyingDataBagSource(DataBagSource fieldsForAllFixedFields, DataBagSource valuesForLastField) {
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
}
