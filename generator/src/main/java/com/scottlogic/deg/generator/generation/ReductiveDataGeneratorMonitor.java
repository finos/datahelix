package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.FieldCollection;
import com.scottlogic.deg.generator.walker.reductive.FixedField;

public interface ReductiveDataGeneratorMonitor extends DataGeneratorMonitor{
    void rowSpecEmitted(
        FixedField lastFixedField,
        FieldSpec fieldSpecForValuesInLastFixedField,
        RowSpec rowSpecWithAllValuesForLastFixedField);

    void fieldFixedToValue(Field field, Object current);
    void unableToStepFurther(FieldCollection fieldCollection);
}
