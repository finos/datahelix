package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.util.Map;

public interface ReductiveDataGeneratorMonitor extends DataGeneratorMonitor {
    default void rowEmitted() {}
    default void fieldFixedToValue(Field field, Object current) {}
    default void unableToStepFurther(ReductiveState reductiveState) {}
    default void noValuesForField(ReductiveState reductiveState, Field field) {}
    default void unableToEmitRowAsSomeFieldSpecsAreEmpty(ReductiveState reductiveState, Map<Field, FieldSpec> fieldSpecsPerField) {}
}
