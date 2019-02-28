package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.MustContainsFieldValueSource;

import java.util.List;

public interface FieldValueSourceEvaluator {
    List<FieldValueSource> getFieldValueSources(FieldSpec fieldSpec);
    MustContainsFieldValueSource getRequiredValueSources(FieldSpec fieldSpec);
}