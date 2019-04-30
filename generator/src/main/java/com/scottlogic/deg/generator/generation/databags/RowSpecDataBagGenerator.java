package com.scottlogic.deg.generator.generation.databags;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;

import java.util.stream.Stream;

public interface RowSpecDataBagGenerator {
    Stream<DataBag> createDataBags(RowSpec rowSpec);
}

