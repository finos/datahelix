package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.FieldSpecFulfiller;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagSource;
import com.scottlogic.deg.generator.generation.databags.MultiplexingDataBagSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ReductiveRowSpec extends RowSpec {

    private final Field lastFixedField;

    public ReductiveRowSpec(ProfileFields fields, Map<Field, FieldSpec> fieldToFieldSpec, Field lastFixedField) {
        super(fields, fieldToFieldSpec);
        this.lastFixedField = lastFixedField;
    }

    @Override
    public DataBagSource createDataBagSource() {
        List<DataBagSource> fieldDataBagSources = new ArrayList<>(getFields().size() - 1);

        for (Field field : getFields()) {
            if (field.equals(this.lastFixedField)){
                continue;
            }

            FieldSpec fieldSpec = getSpecForField(field);

            fieldDataBagSources.add(
                new SingleValueDataBagSource(
                    new FieldSpecFulfiller(field, fieldSpec)));
        }

        DataBagSource sourceWithoutLastFixedField = new MultiplexingDataBagSource(fieldDataBagSources.stream());
        return new MultiplyingDataBagSource(
            sourceWithoutLastFixedField,
            new FieldSpecFulfiller(this.lastFixedField, this.getSpecForField(this.lastFixedField)));
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
