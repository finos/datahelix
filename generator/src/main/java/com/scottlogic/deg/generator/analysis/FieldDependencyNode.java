package com.scottlogic.deg.generator.analysis;

import com.scottlogic.deg.generator.Field;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FieldDependencyNode {
    public final Field field;
    final FieldDependencyAnalyser.FieldDirectDependency directDependencies;
    Set<FieldDependencyNode> dependencyNodes = new HashSet<>();
    Set<FieldDependencyNode> dependantNodes = new HashSet<>();

    FieldDependencyNode(FieldDependencyAnalyser.FieldDirectDependency directDependencies){
        this.field = directDependencies.representedField;
        this.directDependencies = directDependencies;
    }

    @Override
    public String toString(){
        String dependencies = dependencyNodes.stream().map(node -> node.field.name).collect(Collectors.joining(", ", "[", "] -> "));
        String dependants = dependantNodes.stream().map(node -> node.field.name).collect(Collectors.joining(", ", " -> [", "]"));
        return dependencies + field.name + dependants;
    }
}
