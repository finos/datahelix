package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.generation.field_value_sources.*;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface FieldValueSourceEvaluator {
    List<IFieldValueSource> getFieldValueSources(FieldSpec fieldSpec);
}