package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.IDataBagSource;
import com.scottlogic.deg.generator.generation.field_value_sources.CombiningFieldValueSource;
import com.scottlogic.deg.generator.generation.field_value_sources.IFieldValueSource;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FieldSpecFulfiller implements IDataBagSource {
    private final Field field;
    private final FieldSpec spec;
    private final FieldValueSourceEvaluator sourceFactory;

    public FieldSpecFulfiller(Field field, FieldSpec spec) {
        this(field, spec, new StandardFieldValueSourceEvaluator());
    }

    public FieldSpecFulfiller(Field field, FieldSpec spec, FieldValueSourceEvaluator sourceEvaluator) {
        this.field = field;
        this.spec = spec;
        this.sourceFactory = sourceEvaluator;
    }

    @Override
    public Stream<DataBag> generate(GenerationConfig generationConfig) {
        List<IFieldValueSource> fieldValueSources = this.sourceFactory.getFieldValueSources(this.spec);

        IFieldValueSource combinedFieldValueSource = new CombiningFieldValueSource(fieldValueSources);

        Iterable<Object> iterable =  getDataValues(combinedFieldValueSource, generationConfig.getDataGenerationType());

        return StreamSupport.stream(iterable.spliterator(), false)
            .map(value -> {
                DataBagValue dataBagValue = new DataBagValue(
                    value,
                    this.spec.getFormatRestrictions() != null
                        ? this.spec.getFormatRestrictions().formatString
                        : null,
                    new DataBagValueSource(this.spec.getFieldSpecSource()));

                return DataBag.startBuilding()
                    .set(
                        this.field,
                        dataBagValue)
                    .build();
            });
    }

    private Iterable<Object> getDataValues(IFieldValueSource source, GenerationConfig.DataGenerationType dataType) {
        switch (dataType) {
            case FULL_SEQUENTIAL:
            default:
                return source.generateAllValues();
            case INTERESTING:
                return source.generateInterestingValues();
            case RANDOM:
                return source.generateRandomValues(new JavaUtilRandomNumberGenerator(0));
        }
    }
}

