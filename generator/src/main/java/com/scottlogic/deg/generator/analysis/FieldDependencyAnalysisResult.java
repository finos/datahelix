package com.scottlogic.deg.generator.analysis;

import com.scottlogic.deg.generator.Field;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldDependencyAnalysisResult {

    private final Map<Field, Set<FieldDependency>> fieldDependencies;
    private final Map<Field, Set<FieldDependency>> fieldDependants;

    public FieldDependencyAnalysisResult(Map<Field, Set<FieldDependency>> fieldDependencies, Map<Field, Set<FieldDependency>> fieldDependants){
        this.fieldDependencies = fieldDependencies;
        this.fieldDependants = fieldDependants;
    }

    public Set<Field> getDependentsOf(Field field){
        return getFieldDependencyObjectStream(field, fieldDependants);
    }

    public Set<Field> getDependenciesOf(Field field){
        return getFieldDependencyObjectStream(field, fieldDependencies);
    }

    private Set<Field> getFieldDependencyObjectStream(Field field, Map<Field, Set<FieldDependency>> relationMap) {
        return relationMap.get(field).stream().map(FieldDependency::getField).collect(Collectors.toSet());
    }


}
