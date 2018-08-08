package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.FieldSpecFulfiller;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.ArrayList;
import java.util.List;

public class RowSpecDataBagSource {
    public static IDataBagSource create(RowSpec rowSpec) {
        List<IDataBagSource> fieldDataBagSources = new ArrayList<>(rowSpec.getFields().size());

        for (Field field : rowSpec.getFields()) {
            FieldSpec fieldSpec = rowSpec.getSpecForField(field);

            fieldDataBagSources.add(
                new FieldSpecDataBagSource(
                    field,
                    new FieldSpecFulfiller(fieldSpec)));
        }

        return new MultiplexingDataBagSource(fieldDataBagSources);
    }
}
