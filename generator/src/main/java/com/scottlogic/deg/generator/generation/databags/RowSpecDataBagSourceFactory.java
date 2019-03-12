package com.scottlogic.deg.generator.generation.databags;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.ReductiveRowSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public interface RowSpecDataBagSourceFactory{
    DataBagSource createDataBagSource(RowSpec rowSpec);
}

