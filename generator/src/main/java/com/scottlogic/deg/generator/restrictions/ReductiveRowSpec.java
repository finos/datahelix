package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.FieldSpecFulfiller;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.IDataBagSource;
import com.scottlogic.deg.generator.generation.databags.MultiplexingDataBagSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReductiveRowSpec extends RowSpec {

    private final Field lastFixedField;

    public ReductiveRowSpec(ProfileFields fields, Map<Field, FieldSpec> fieldToFieldSpec, Field lastFixedField) {
        super(fields, fieldToFieldSpec);
        this.lastFixedField = lastFixedField;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        ReductiveRowSpec otherRowSpec = (ReductiveRowSpec) obj;
        if ((fields != null && !fields.equals(otherRowSpec.fields)) ||
            (fields == null && otherRowSpec.fields != null)) {
            return false;
        }
        if ((fieldToFieldSpec != null && !fieldToFieldSpec.equals(otherRowSpec.fieldToFieldSpec)) ||
            (fieldToFieldSpec == null && otherRowSpec.fieldToFieldSpec != null)) {
            return false;
        }
        if (!lastFixedField.equals(otherRowSpec.lastFixedField)) {
            return false;
        }

        return true;
    }

    @Override
    public IDataBagSource createDataBagSource() {
        List<IDataBagSource> fieldDataBagSources = new ArrayList<>(getFields().size() - 1);

        for (Field field : getFields()) {
            if (field.equals(this.lastFixedField)){
                continue;
            }

            FieldSpec fieldSpec = getSpecForField(field);

            fieldDataBagSources.add(
                new SingleValueDataBagSource(
                    new FieldSpecFulfiller(field, fieldSpec)));
        }

        IDataBagSource sourceWithoutLastFixedField = new MultiplexingDataBagSource(fieldDataBagSources.stream());
        return new MultiplyingDataBagSource(
            sourceWithoutLastFixedField,
            new FieldSpecFulfiller(this.lastFixedField, this.getSpecForField(this.lastFixedField)));
    }

    class SingleValueDataBagSource implements IDataBagSource {
        private final IDataBagSource source;

        public SingleValueDataBagSource(IDataBagSource source) {
            this.source = source;
        }

        @Override
        public Stream<DataBag> generate(GenerationConfig generationConfig) {
            return source.generate(generationConfig)
                .limit(1);
        }
    }

    class MultiplyingDataBagSource implements IDataBagSource {

        private final IDataBagSource fieldsForAllFixedFields;
        private final IDataBagSource valuesForLastField;

        public MultiplyingDataBagSource(IDataBagSource fieldsForAllFixedFields, IDataBagSource valuesForLastField) {
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
