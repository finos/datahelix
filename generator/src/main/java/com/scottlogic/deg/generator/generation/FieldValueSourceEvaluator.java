package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.field_value_sources.FieldValueSource;

import java.util.Set;

public interface FieldValueSourceEvaluator {
    Set<FieldValueSource> getFieldValueSources(FieldSpec fieldSpec);
}