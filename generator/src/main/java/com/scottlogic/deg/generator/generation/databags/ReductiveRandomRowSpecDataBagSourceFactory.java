package com.scottlogic.deg.generator.generation.databags;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.StandardFieldValueSourceEvaluator;

import java.util.stream.Stream;

public class ReductiveRandomRowSpecDataBagSourceFactory implements RowSpecDataBagSourceFactory {
    private final StandardRowSpecDataBagSourceFactory underlyingFactory;

    @Inject
    public ReductiveRandomRowSpecDataBagSourceFactory(StandardRowSpecDataBagSourceFactory underlyingFactory) {
        this.underlyingFactory = underlyingFactory;
    }

    @Override
    public DataBagSource createDataBagSource(RowSpec rowSpec) {
        DataBagSource source = underlyingFactory.createDataBagSource(rowSpec);

        //The Reductive walker will emit a single RowSpec that can represent multiple rows
        //each of these rows will have all fields (except the last one) fixed to a value
        //if this RowSpec is emitted fully it will give the impression of a set of non-random rows therefore:
        //emit only one row from the RowSpec then let the walker restart the generation for another random RowSpec
        return generationConfig -> source.generate(generationConfig).limit(1);
    }
}
