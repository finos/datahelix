package com.scottlogic.deg.generator.analysis;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.constraint.grammatical.ConditionalConstraint;
import com.scottlogic.deg.common.constraint.Constraint;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldDependencyAnalyser {

    public FieldDependencyAnalysisResult analyse(Profile profile) {
        List<FieldDependencyNode> graph = getFieldDependencyGraph(profile);
        Map<Field, Collection<FieldDependency>> dependants = graph.stream()
            .collect(Collectors.toMap(fdn -> fdn.field, this::getAllDependentFields));
        Map<Field, Collection<FieldDependency>> influencers = graph.stream()
            .collect(Collectors.toMap(fdn -> fdn.field, this::getAllInfluencingFields));
        return new FieldDependencyAnalysisResult(influencers, dependants);
    }

    private List<FieldDependencyNode> getFieldDependencyGraph(Profile profile){
        Map<Field, Set<Field>> dependencyMapping = new HashMap<>();

        // First make a map of all fields to their directly dependent fields
        profile.fields.forEach(field -> {
            dependencyMapping.putIfAbsent(field, new HashSet<>());
            findFieldInPredicate(profile, field).forEach(cc -> {
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

    private Collection<FieldDependency> getAllDependentFields(FieldDependencyNode node) {
        if (node.dependantNodes.isEmpty()) {
            return Collections.emptyList();
        }
        Set<FieldDependency> dependentFields = new HashSet<>();
        getAllDependentFields(node, dependentFields, 1);
        return dependentFields.stream().filter(fd -> !fd.getField().equals(node.field)).collect(Collectors.toList());
    }

    private void getAllDependentFields(FieldDependencyNode node, Set<FieldDependency> dependants, int depth){
        node.dependantNodes.stream()
            .filter(dependantNode -> dependants.stream().noneMatch(fd -> fd.getField().equals(dependantNode.field))) // Make sure not already processed
            .forEach(unprocessedDependantNode-> {
                dependants.add(new FieldDependency(unprocessedDependantNode.field, depth));
                getAllDependentFields(unprocessedDependantNode, dependants, depth + 1);
            });
    }

    private Collection<FieldDependency> getAllInfluencingFields(FieldDependencyNode node) {
        if (node.dependencyNodes.isEmpty()) {
            return Collections.emptyList();
        }
        Set<FieldDependency> influencingFields = new HashSet<>();
        getAllInfluencingFields(node, influencingFields, 1);
        return influencingFields.stream().filter(fd -> !fd.getField().equals(node.field)).collect(Collectors.toList());
    }

    private void getAllInfluencingFields(FieldDependencyNode node, Set<FieldDependency> influencers, int depth){
        node.dependencyNodes.stream()
            .filter(influencingNode -> influencers.stream().noneMatch(fd -> fd.getField().equals(influencingNode.field))) // Make sure not already processed
            .forEach(unprocessedInfluencingNode-> {
                influencers.add(new FieldDependency(unprocessedInfluencingNode.field, depth));
                getAllInfluencingFields(unprocessedInfluencingNode, influencers, depth + 1);
            });
    }

    private Stream<Constraint> constraintsFromProfile(Profile profile){
        return FlatMappingSpliterator.flatMap(profile.rules.stream(), rule -> rule.constraints.stream());
    }

    private Stream<ConditionalConstraint> conditionalConstraintsFromProfile(Profile profile){
        return constraintsFromProfile(profile)
            .filter(constraint -> constraint instanceof ConditionalConstraint)
            .map(constraint -> (ConditionalConstraint) constraint);
    }

    private Stream<ConditionalConstraint> findFieldInPredicate(Profile profile, Field field){
        return conditionalConstraintsFromProfile(profile)
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

