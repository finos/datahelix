package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.util.Map;

public interface ReductiveDataGeneratorMonitor extends DataGeneratorMonitor{
    void rowSpecEmitted(RowSpec rowSpec);
    void fieldFixedToValue(Field field, Object current);
    void unableToStepFurther(ReductiveState reductiveState);
    void noValuesForField(ReductiveState reductiveState);
    void unableToEmitRowAsSomeFieldSpecsAreEmpty(ReductiveState reductiveState, Map<Field, FieldSpec> fieldSpecsPerField);
    }
