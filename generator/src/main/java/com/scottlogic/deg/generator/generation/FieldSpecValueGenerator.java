package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.generation.fieldvaluesources.CombiningFieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FieldSpecValueGenerator {
    private final GenerationConfig generationConfig;
    private final FieldValueSourceEvaluator sourceFactory;
    private final JavaUtilRandomNumberGenerator randomNumberGenerator;

    @Inject
    public FieldSpecValueGenerator(GenerationConfig generationConfig, FieldValueSourceEvaluator sourceEvaluator, JavaUtilRandomNumberGenerator randomNumberGenerator) {
        this.generationConfig = generationConfig;
        this.sourceFactory = sourceEvaluator;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public Stream<DataBag> generate(Field field, FieldSpec spec) {
        List<FieldValueSource> fieldValueSources = this.sourceFactory.getFieldValueSources(spec);

        FieldValueSource combinedFieldValueSource = new CombiningFieldValueSource(fieldValueSources);

        Iterable<Object> iterable =  getDataValues(combinedFieldValueSource, generationConfig.getDataGenerationType());

        return StreamSupport.stream(iterable.spliterator(), false)
            .map(value -> {
                DataBagValue dataBagValue = new DataBagValue(
                    value,
                    spec.getFormatRestrictions() != null
                        ? spec.getFormatRestrictions().formatString
                        : null,
                    new DataBagValueSource(spec.getFieldSpecSource()));

                return DataBag.startBuilding()
                    .set(
                        field,
                        dataBagValue)
                    .build();
            });
    }

    private Iterable<Object> getDataValues(FieldValueSource source, GenerationConfig.DataGenerationType dataType) {
        switch (dataType) {
            case FULL_SEQUENTIAL:
            default:
                return source.generateAllValues();
            case INTERESTING:
                return source.generateInterestingValues();
            case RANDOM:
                return source.generateRandomValues(randomNumberGenerator);
        }
    }
}

