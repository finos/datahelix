package com.scottlogic.deg.generator.analysis;

import com.scottlogic.deg.common.profile.Field;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FieldDependencyAnalysisResult {

    private final Map<Field, Collection<FieldDependency>> fieldDependencies;
    private final Map<Field, Collection<FieldDependency>> fieldDependants;

    public FieldDependencyAnalysisResult(Map<Field, Collection<FieldDependency>> fieldDependencies, Map<Field, Collection<FieldDependency>> fieldDependants){
        this.fieldDependencies = fieldDependencies;
        this.fieldDependants = fieldDependants;
    }

    public Set<Field> getDependentsOf(Field field){
        return getFieldDependencyObjectStream(field, fieldDependants);
    }

    public Set<Field> getDependenciesOf(Field field){
        return getFieldDependencyObjectStream(field, fieldDependencies);
    }

    private Set<Field> getFieldDependencyObjectStream(Field field, Map<Field, Collection<FieldDependency>> relationMap) {
        return relationMap.get(field).stream().map(FieldDependency::getField).collect(Collectors.toSet());
    }


}
