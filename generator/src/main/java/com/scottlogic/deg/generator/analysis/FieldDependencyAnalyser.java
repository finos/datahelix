package com.scottlogic.deg.generator.analysis;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.constraints.ConditionalConstraint;
import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldDependencyAnalyser {

    private final Profile profile;

    public FieldDependencyAnalyser(Profile profile){
        this.profile = profile;
    }

    public FieldDependencyAnalysisResult analyse() {
        List<FieldDependencyNode> graph = getFieldDependencyGraph();
        Map<Field, Set<FieldDependency>> dependants = graph.stream()
            .collect(Collectors.toMap(fdn -> fdn.field, this::getAllDependentFields));
        Map<Field, Set<FieldDependency>> influencers = graph.stream()
            .collect(Collectors.toMap(fdn -> fdn.field, this::getAllInfluencingFields));
        return new FieldDependencyAnalysisResult(influencers, dependants);
    }

    private List<FieldDependencyNode> getFieldDependencyGraph(){
        Map<Field, Set<Field>> dependencyMapping = new HashMap<>();

        // First make a map of all fields to their directly dependent fields
        this.profile.fields.forEach(field -> {
            dependencyMapping.putIfAbsent(field, new HashSet<>());
            findFieldInPredicate(field).forEach(cc -> {
                dependencyMapping.get(field).addAll(cc.whenConditionIsTrue.getFields());
                if (cc.whenConditionIsFalse != null) {
                    dependencyMapping.get(field).addAll(cc.whenConditionIsFalse.getFields());
                }
            });
        });

        List<FieldDependencyNode> nodeList = new ArrayList<>();
        // Create field dependency objects
        dependencyMapping.forEach((currentField, dependantFields) -> {
                FieldDirectDependency directDependency = new FieldDirectDependency(currentField);
                directDependency.dependants = dependantFields;

                Set<Field> dependencies = new HashSet<>();
                dependencyMapping.forEach((otherField, otherFieldDependants) -> {
                    // Calculate whether another field has this one as a dependant, meaning current field
                    // has dependency on other field
                    if (otherFieldDependants.contains(currentField)){
                        dependencies.add(otherField);
                    }
                });
                directDependency.dependencies = dependencies;
                nodeList.add(new FieldDependencyNode(directDependency));
            });

        // Transform field dependency object to interlinked field dependency nodes
        nodeList.forEach(node -> {
            node.directDependencies.dependants.forEach(dep -> {
                FieldDependencyNode dependentNode = nodeList.stream().filter(depNode -> depNode.field.equals(dep)).findFirst().get();
                node.dependantNodes.add(dependentNode);
            });
            node.directDependencies.dependencies.forEach(dep -> {
                FieldDependencyNode dependencyNode = nodeList.stream().filter(depNode -> depNode.field.equals(dep)).findFirst().get();
                node.dependencyNodes.add(dependencyNode);
            });
        });
        return nodeList;
    }

    private Set<FieldDependency> getAllDependentFields(FieldDependencyNode node) {
        if (node.dependantNodes.isEmpty()) {
            return Collections.emptySet();
        }
        Set<FieldDependency> dependentFields = new HashSet<>();
        getAllDependentFields(node, dependentFields, 1);
        return dependentFields.stream().filter(fd -> !fd.getField().equals(node.field)).collect(Collectors.toSet());
    }

    private void getAllDependentFields(FieldDependencyNode node, Set<FieldDependency> dependants, int depth){
        node.dependantNodes.stream()
            .filter(dependantNode -> dependants.stream().noneMatch(fd -> fd.getField().equals(dependantNode.field))) // Make sure not already processed
            .forEach(unprocessedDependantNode-> {
                dependants.add(new FieldDependency(unprocessedDependantNode.field, depth));
                getAllDependentFields(unprocessedDependantNode, dependants, depth + 1);
            });
    }

    private Set<FieldDependency> getAllInfluencingFields(FieldDependencyNode node) {
        if (node.dependencyNodes.isEmpty()) {
            return Collections.emptySet();
        }
        Set<FieldDependency> influencingFields = new HashSet<>();
        getAllInfluencingFields(node, influencingFields, 1);
        return influencingFields.stream().filter(fd -> !fd.getField().equals(node.field)).collect(Collectors.toSet());
    }

    private void getAllInfluencingFields(FieldDependencyNode node, Set<FieldDependency> influencers, int depth){
        node.dependencyNodes.stream()
            .filter(influencingNode -> influencers.stream().noneMatch(fd -> fd.getField().equals(influencingNode.field))) // Make sure not already processed
            .forEach(unprocessedInfluencingNode-> {
                influencers.add(new FieldDependency(unprocessedInfluencingNode.field, depth));
                getAllInfluencingFields(unprocessedInfluencingNode, influencers, depth + 1);
            });
    }

    private Stream<IConstraint> constraintsFromProfile(){
        return profile.rules.stream()
            .flatMap(rule -> rule.constraints.stream());
    }

    private Stream<ConditionalConstraint> conditionalConstraintsFromProfile(){
        return constraintsFromProfile()
            .filter(constraint -> constraint instanceof ConditionalConstraint)
            .map(constraint -> (ConditionalConstraint) constraint);
    }

    private Stream<ConditionalConstraint> findFieldInPredicate(Field field){
        return conditionalConstraintsFromProfile()
            .filter(constraint -> constraint.condition.getFields().contains(field));
    }

    class FieldDirectDependency {
        Field representedField;
        Set<Field> dependants = new HashSet<>();
        Set<Field> dependencies = new HashSet<>();

        FieldDirectDependency(Field representedField) {
            this.representedField = representedField;
        }
    }

}

