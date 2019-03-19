package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.util.Map;

public class NoopDataGeneratorMonitor implements ReductiveDataGeneratorMonitor {

    @Override
    public void generationStarting(GenerationConfig generationConfig) { }

    @Override
    public void rowEmitted(GeneratedObject row) { }

    @Override
    public void endGeneration() { }

    @Override
    public void rowSpecEmitted(RowSpec rowSpec) { }

    @Override
    public void fieldFixedToValue(Field field, Object current) { }

    @Override
    public void unableToStepFurther(ReductiveState reductiveState) { }

    @Override
    public void noValuesForField(ReductiveState reductiveState) { }

    @Override
    public void unableToEmitRowAsSomeFieldSpecsAreEmpty(ReductiveState reductiveState, Map<Field, FieldSpec> fieldSpecsPerField) { }
}
