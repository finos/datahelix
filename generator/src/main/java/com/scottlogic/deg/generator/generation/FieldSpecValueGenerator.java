package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.generation.fieldvaluesources.CombiningFieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FieldSpecValueGenerator {
    private final DataGenerationType dataType;
    private final FieldValueSourceEvaluator sourceFactory;
    private final JavaUtilRandomNumberGenerator randomNumberGenerator;

    @Inject
    public FieldSpecValueGenerator(DataGenerationType dataGenerationType, FieldValueSourceEvaluator sourceEvaluator, JavaUtilRandomNumberGenerator randomNumberGenerator) {
        this.dataType = dataGenerationType;
        this.sourceFactory = sourceEvaluator;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public Stream<DataBag> generate(Field field, FieldSpec spec) {
        List<FieldValueSource> fieldValueSources = this.sourceFactory.getFieldValueSources(spec);

        FieldValueSource combinedFieldValueSource = new CombiningFieldValueSource(fieldValueSources);

        Iterable<Object> iterable =  getDataValues(combinedFieldValueSource);

        return StreamSupport.stream(iterable.spliterator(), false)
            .map(value -> {
                DataBagValue dataBagValue = new DataBagValue(
                    value,
                    spec.getFormatRestrictions() != null
                        ? spec.getFormatRestrictions().formatString
                        : null,
                    spec.getFieldSpecSource().toDataBagValueSource());

                Map<Field, DataBagValue> map = new HashMap<>();
                map.put(field, dataBagValue);
                return new DataBag(map);
            });
    }

    private Iterable<Object> getDataValues(FieldValueSource source) {
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

