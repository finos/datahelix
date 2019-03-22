package com.scottlogic.deg.generator.generation.databags;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;

public interface RowSpecDataBagSourceFactory{
    DataBagSource createDataBagSource(RowSpec rowSpec);
}

