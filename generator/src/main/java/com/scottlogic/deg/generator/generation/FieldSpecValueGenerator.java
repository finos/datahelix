package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.generation.fieldvaluesources.CombiningFieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
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

    public Stream<DataBagValue> generate(Set<FieldSpec> specs) {
        List<FieldValueSource> fieldValueSources = specs.stream()
            .map(sourceFactory::getFieldValueSources)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());

        return createValuesFromSources(specs.stream().findFirst().orElse(FieldSpec.Empty), fieldValueSources);
    }

    public Stream<DataBagValue> generate(FieldSpec spec) {
        List<FieldValueSource> fieldValueSources = sourceFactory.getFieldValueSources(spec);

        return createValuesFromSources(spec, fieldValueSources);
    }

    @NotNull
    private Stream<DataBagValue> createValuesFromSources(FieldSpec spec, List<FieldValueSource> fieldValueSources) {
        FieldValueSource combinedFieldValueSource = new CombiningFieldValueSource(fieldValueSources);

        Iterable<Object> iterable =  getDataValues(combinedFieldValueSource);

        return StreamSupport.stream(iterable.spliterator(), false)
            .map(value -> new DataBagValue(value, spec.getFormatting()));
    }

    private Iterable<Object> getDataValues(FieldValueSource source) {
        switch (dataType) {
            case FULL_SEQUENTIAL:
                return source.generateAllValues();
            case INTERESTING:
                return source.generateInterestingValues();
            case RANDOM:
                return source.generateRandomValues(randomNumberGenerator);
            default:
                throw new UnsupportedOperationException("No data generation type set.");
        }
    }
}

