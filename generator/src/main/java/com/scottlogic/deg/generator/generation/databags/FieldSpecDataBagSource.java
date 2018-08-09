package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.FieldSpecFulfiller;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.utils.ProjectingIterable;

public class FieldSpecDataBagSource implements IDataBagSource
{
    private final FieldSpecFulfiller fulfiller;
    private final Field field;

    public FieldSpecDataBagSource(Field field, FieldSpecFulfiller fulfiller) {
        this.field = field;
        this.fulfiller = fulfiller;
    }

    @Override
    public Iterable<DataBag> generate(GenerationConfig generationConfig) {
        return new ProjectingIterable<>(
            () -> this.fulfiller.iterator(generationConfig),
            value -> DataBag.startBuilding().set(this.field, value).build());
    }
}
