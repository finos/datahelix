package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;
import com.scottlogic.deg.generator.walker.reductive.FixedField;

public class NoopDataGeneratorMonitor implements DataGeneratorMonitor {

    @Override
    public void generationStarting(GenerationConfig generationConfig) { }

    @Override
    public void rowEmitted(GeneratedObject row) { }
}
