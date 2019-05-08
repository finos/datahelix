package com.scottlogic.deg.generator.generation.databags;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;

import java.util.stream.Stream;

public class ReductiveRandomRowSpecDataBagGenerator implements RowSpecDataBagGenerator {
    private final StandardRowSpecDataBagGenerator underlyingFactory;

    @Inject
    public ReductiveRandomRowSpecDataBagGenerator(StandardRowSpecDataBagGenerator underlyingFactory) {
        this.underlyingFactory = underlyingFactory;
    }

    @Override
    public Stream<DataBag> createDataBags(RowSpec rowSpec) {
        //The Reductive walker will emit a single RowSpec that can represent multiple rows
        //each of these rows will have all fields (except the last one) fixed to a value
        //if this RowSpec is emitted fully it will give the impression of a set of non-random rows therefore:
        //emit only one row from the RowSpec then let the walker restart the generation for another random RowSpec
        return underlyingFactory.createDataBags(rowSpec).limit(1);
    }
}
