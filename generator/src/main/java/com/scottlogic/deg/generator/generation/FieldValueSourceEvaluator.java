package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.field_value_sources.FieldValueSource;

import java.util.List;

public interface FieldValueSourceEvaluator {
    List<FieldValueSource> getFieldValueSources(FieldSpec fieldSpec);
}