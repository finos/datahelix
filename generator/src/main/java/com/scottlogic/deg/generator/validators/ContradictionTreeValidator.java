package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.Node;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.walker.reductive.Merged;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ContradictionTreeValidator {

    private final ConstraintReducer constraintReducer;
    private final ContradictionValidatorMonitorInterface validationMonitor;
    private final FieldSpecMerger fieldSpecMerger;

    @Inject
    public ContradictionTreeValidator(ConstraintReducer constraintReducer, ContradictionValidatorMonitorInterface validationMonitor, FieldSpecMerger fieldSpecMerger){
        this.constraintReducer = constraintReducer;
        this.validationMonitor = validationMonitor;
        this.fieldSpecMerger = fieldSpecMerger;
    }

    /**
     * Takes a DecisionTree, walks every node, and check every child of each node for contradictory constraints
     * Emits output about contradictions via the ContradictionValidationMonitor
     * @param decisionTree
     */
    public void reportContradictions(DecisionTree decisionTree) {
        ConstraintNode root =  decisionTree.rootNode;

        walkTree(root);

        return;
    }

    private void walkTree(Node root){
        Stack<Node> stack = new Stack<Node>();
        Node currentNode = root;

        while(!stack.empty() || currentNode!=null){
            if(currentNode!=null)
            {
                // push this node to the stack, so we can back track to it later.
                stack.push(currentNode);
                // get the next left node if it exists.
                if (currentNode instanceof ConstraintNode){
                    // get left node
                    DecisionNode nextNode = ((ConstraintNode) currentNode).getDecisions().stream().findFirst().orElse(null);
                    currentNode=nextNode;
                }
                else if (currentNode instanceof DecisionNode){
                    // get left node
                    ConstraintNode nextNode = ((DecisionNode) currentNode).getOptions().stream().findFirst().orElse(null);
                    currentNode=nextNode;
                }
            }
            else
            {
                // we reached the bottom of the branch, and now need to back track
                Node node = stack.pop();

                if(node instanceof ConstraintNode){
                    findContradictionForNode((ConstraintNode)node);
                    //findContradictionForNodeFieldSpec((ConstraintNode)node);
                }

                // get the next right node if it exists.
                if (node instanceof ConstraintNode){
                    // get right node
                    DecisionNode nextNode = ((ConstraintNode) node).getDecisions().stream().skip(1).findFirst().orElse(null);
                    currentNode=nextNode;
                }
                else if (node instanceof DecisionNode){
                    // get right node
                    ConstraintNode nextNode = ((DecisionNode) node).getOptions().stream().skip(1).findFirst().orElse(null);
                    currentNode=nextNode;
                }
            }
        }
    }

    /**
     * Recursively looks for constraint contradictions between the nodeToCheck, and its descendants
     * @param nodeToCheck the node that should be checked for contradictions
     * @return return true if a contradiction is found.
     */
    private boolean findContradictionForNode(ConstraintNode nodeToCheck){
            for (DecisionNode node : (nodeToCheck).getDecisions()) {
                boolean contradictionFound = recursiveFindContradiction(nodeToCheck, node);
                if (contradictionFound) {
                    return true;
                }
            }
        return false;
    }

    private boolean findContradictionForNodeFieldSpec(ConstraintNode nodeToCheck){
        Map<Field, FieldSpec> fieldSpecsForNode = getFieldSpecsForNode(nodeToCheck);
        for (DecisionNode node : (nodeToCheck).getDecisions()) {
            boolean contradictionFound = recursiveFindContradiction(fieldSpecsForNode, node);
            if (contradictionFound) {
                return true;
            }
        }
        return false;
    }

    private boolean recursiveFindContradiction(Node nodeToCheck, Node currentNode){
        // can only check for contradictions on ConstraintNodes
        if (currentNode instanceof ConstraintNode && nodeToCheck instanceof ConstraintNode){
            boolean contradiction = checkContradictions((ConstraintNode)nodeToCheck, (ConstraintNode)currentNode);
            if(contradiction)
                return true;
        }

        // no contradiction, call next node.
        if (currentNode instanceof ConstraintNode) {
            for (DecisionNode node : ((ConstraintNode) currentNode).getDecisions()) {
                boolean contradictionFound = recursiveFindContradiction(nodeToCheck, node);
                if (contradictionFound){
                    return true;
                }
            }
        }
        if(currentNode instanceof DecisionNode){
            for (ConstraintNode node : ((DecisionNode) currentNode).getOptions()) {
                boolean contradictionFound = recursiveFindContradiction(nodeToCheck, node);
                if (contradictionFound){
                    return true;
                }
            }
        }

        // no more nodes, and no contradiction found.
       return false;

    }

    private boolean recursiveFindContradiction(Map<Field, FieldSpec> passedDownFieldSpec, Node currentNode){
        Map<Field, FieldSpec> fieldSpecToPassDown = passedDownFieldSpec;
        // can only check for contradictions on ConstraintNodes
        if (currentNode instanceof ConstraintNode){
            Merged<Map<Field, FieldSpec>> mergedMap = checkContradictions(passedDownFieldSpec, (ConstraintNode)currentNode);
            if(mergedMap.isContradictory()){
                return true;
            }
            fieldSpecToPassDown = mergedMap.get();
        }

        // no contradiction call next node.
        if (currentNode instanceof ConstraintNode) {
            for (DecisionNode node : ((ConstraintNode) currentNode).getDecisions()) {
                boolean contradictionFound = recursiveFindContradiction(fieldSpecToPassDown, node);
                if (contradictionFound)
                    return true;
            }
        }
        if(currentNode instanceof DecisionNode){
            for (ConstraintNode node : ((DecisionNode) currentNode).getOptions()) {
                boolean contradictionFound = recursiveFindContradiction(fieldSpecToPassDown, node);
                if (contradictionFound)
                    return true;
            }
        }
        // no more nodes, and no contradiction found.
        return false;
    }

    /**
     * Check for any contradictions
     * @param leftNode
     * @param rightNode
     * @return
     */
    public boolean checkContradictions(ConstraintNode leftNode, ConstraintNode rightNode){

        Collection<Field> leftFields = leftNode.getAtomicConstraints()
            .stream()
            .map(x -> x.getField())
            .collect(Collectors.toCollection(ArrayList::new));

        Collection<Field> rightFields = rightNode.getAtomicConstraints()
            .stream()
            .map(x -> x.getField())
            .collect(Collectors.toCollection(ArrayList::new));

        Collection<Field> combinedFields = Stream.concat(leftFields.stream(), rightFields.stream())
            .collect(Collectors.toCollection(ArrayList::new));

        Collection<AtomicConstraint> nodeToCheckConstraints = leftNode.getAtomicConstraints();
        Collection<AtomicConstraint> currentNodeConstraints = rightNode.getAtomicConstraints();

        Collection<AtomicConstraint> combinedConstraints = Stream.concat(
            nodeToCheckConstraints.stream(),
            currentNodeConstraints.stream()).collect(Collectors.toCollection(ArrayList::new));

        Map<Field, Collection<AtomicConstraint>> map = new HashMap<>();
        combinedConstraints.stream()
            .filter(constraint -> combinedFields.contains(constraint.getField()))
            .forEach(constraint -> addToConstraintsMap(map, constraint));

        for (Map.Entry<Field, Collection<AtomicConstraint>> entry : map.entrySet()) {
            Optional<FieldSpec> fieldSpec = constraintReducer.reduceConstraintsToFieldSpec(entry.getValue());

            if (!fieldSpec.isPresent()){
                validationMonitor.contradictionInTree(entry.getKey(),entry.getValue());
                return true;
            }
        }
        return false;
    }

    private Merged<Map<Field, FieldSpec>> checkContradictions(Map<Field, FieldSpec> passedDownFieldSpec, ConstraintNode nodeToCheck){

        Collection<Field> passedDownFields = passedDownFieldSpec.keySet();
        Collection<Field> nodeToCheckFields = nodeToCheck.getAtomicConstraints()
            .stream()
            .map(x -> x.getField())
            .collect(Collectors.toCollection(ArrayList::new));

        // get a collection of all the fields we are concerned with
        Collection<Field> combinedFields = Stream.concat(passedDownFields.stream(), nodeToCheckFields.stream())
            .collect(Collectors.toCollection(ArrayList::new));

        Map<Field, Collection<AtomicConstraint>> mapFromNodeToCheck = new HashMap<>();
        nodeToCheck.getAtomicConstraints()
            .stream()
            .filter(constraint -> combinedFields.contains(constraint.getField()))
            .forEach(constraint -> addToConstraintsMap(mapFromNodeToCheck, constraint));

        boolean contradictionFound = false;
        for (Map.Entry<Field, Collection<AtomicConstraint>> entry : mapFromNodeToCheck.entrySet()) {

            Optional<FieldSpec> fieldSpecFromNodeToCheck = constraintReducer.reduceConstraintsToFieldSpec(entry.getValue());

            // FieldSpec merger merge this spec, with one from parents. if returns optional empty, a contradiction occurred.
            if(fieldSpecFromNodeToCheck.isPresent()){
                Optional<FieldSpec> mergedFieldSpec = fieldSpecMerger.merge(passedDownFieldSpec.get(entry.getKey()) , fieldSpecFromNodeToCheck.get());
                if (!mergedFieldSpec.isPresent()){
                    validationMonitor.contradictionInTree(entry.getKey(), entry.getValue());
                    contradictionFound =true;
                }
                else{
                    passedDownFieldSpec.put(entry.getKey(), mergedFieldSpec.get());
                }
            }
            else{
                validationMonitor.contradictionInTree(entry.getKey(),  entry.getValue());
                contradictionFound = true;
            }
        }
        if(contradictionFound)
        {
            return Merged.contradictory();
        }
        return Merged.of(passedDownFieldSpec);
    }

    private void addToConstraintsMap(Map<Field, Collection<AtomicConstraint>> map, AtomicConstraint constraint) {
        if (!map.containsKey(constraint.getField())) {
            map.put(constraint.getField(), new ArrayList<>());
        }

        map.get(constraint.getField())
            .add(constraint);
    }

    private Map<Field, FieldSpec> getFieldSpecsForNode(ConstraintNode node){
        Map<Field, FieldSpec> mapToReturn = new HashMap<>();

        Collection<Field> nodeFields = node.getAtomicConstraints()
            .stream()
            .map(x -> x.getField())
            .collect(Collectors.toCollection(ArrayList::new));

        Collection<AtomicConstraint> allConstraints = node.getAtomicConstraints();

        Map<Field, Collection<AtomicConstraint>> mapOfFieldToAtomicConstraints = new HashMap<>();

        allConstraints.stream()
            .filter(constraint -> nodeFields.contains(constraint.getField()))
            .forEach(constraint -> addToConstraintsMap(mapOfFieldToAtomicConstraints, constraint));

        for (Map.Entry<Field, Collection<AtomicConstraint>> entry : mapOfFieldToAtomicConstraints.entrySet()) {
            Optional<FieldSpec> fieldSpec = constraintReducer.reduceConstraintsToFieldSpec(entry.getValue());

            if(fieldSpec.isPresent()){
                // add field and FieldSpec to map
                mapToReturn.put(entry.getKey(),fieldSpec.get());
            }
        }
        return mapToReturn;
    }
}
