package com.scottlogic.deg.generator.analysis;

import com.scottlogic.deg.common.profile.Field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class FieldDependencyNode {
    public final Field field;
    final FieldDependencyAnalyser.FieldDirectDependency directDependencies;
    Collection<FieldDependencyNode> dependencyNodes = new ArrayList<>();
    Collection<FieldDependencyNode> dependantNodes = new ArrayList<>();

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
