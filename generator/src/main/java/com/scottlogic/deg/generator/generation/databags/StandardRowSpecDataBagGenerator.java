package com.scottlogic.deg.generator.generation.databags;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;

import java.util.stream.Stream;

public class StandardRowSpecDataBagGenerator implements RowSpecDataBagGenerator {
    private final FieldSpecValueGenerator generator;
    private final CombinationStrategy combinationStrategy;

    @Inject
    public StandardRowSpecDataBagGenerator(
        FieldSpecValueGenerator generator,
        CombinationStrategy combinationStrategy) {
        this.generator = generator;
        this.combinationStrategy = combinationStrategy;
    }

    public Stream<DataBag> createDataBags(RowSpec rowSpec){
        Stream<Stream<DataBag>> dataBagsForFields =
            rowSpec.getFields().stream()
                .map(field -> generateDataForField(rowSpec, field));

        return combinationStrategy
            .permute(dataBagsForFields);
    }

    private Stream<DataBag> generateDataForField(RowSpec rowSpec, Field field) {
        FieldSpec fieldSpec = rowSpec.getSpecForField(field);

        return generator.generate(field, fieldSpec);
    }
}
