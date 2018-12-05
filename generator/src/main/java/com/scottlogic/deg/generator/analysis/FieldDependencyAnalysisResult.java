package com.scottlogic.deg.generator.analysis;

import com.scottlogic.deg.generator.Field;

import java.util.Map;
import java.util.Set;

public class FieldDependencyAnalysisResult {

    private final Map<Field, Set<Field>> fieldDependencies;
    private final Map<Field, Set<Field>> fieldDependants;

    public FieldDependencyAnalysisResult(Map<Field, Set<Field>> fieldDependencies, Map<Field, Set<Field>> fieldDependants){
        this.fieldDependencies = fieldDependencies;
        this.fieldDependants = fieldDependants;
    }

    public Set<Field> getDependentsOf(Field field){
        return fieldDependants.get(field);
    }

    public Set<Field> getDependenciesOf(Field field){
        return fieldDependencies.get(field);
    }

}
