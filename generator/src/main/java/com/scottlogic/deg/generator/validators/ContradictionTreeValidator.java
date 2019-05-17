package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.Node;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ContradictionTreeValidator {

    private DecisionTree decisionTree;
    private final ConstraintReducer constraintReducer;
    private final ContradictionValidatorMonitorInterface validationMonitor;

    @Inject
    public ContradictionTreeValidator(ConstraintReducer constraintReducer, ContradictionValidatorMonitorInterface validationMonitor){
        this.constraintReducer = constraintReducer;
        this.validationMonitor = validationMonitor;
    }

    /**
     * Takes a DecisionTree, walks every node, and check every child of each node for contradictory constraints
     * Emits output about contradictions via the ContradictionValidationMonitor
     * @param decisionTree
     */
    public void reportContradictions(DecisionTree decisionTree) {
        this.decisionTree = decisionTree;

        ConstraintNode root =  this.decisionTree.rootNode;

        // walk tree, check for contradictions in child nodes.
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
                validationMonitor.contradictionInTree(entry);
                return true;
            }
        }
        return false;
    }

    private void addToConstraintsMap(Map<Field, Collection<AtomicConstraint>> map, AtomicConstraint constraint) {
        if (!map.containsKey(constraint.getField())) {
            map.put(constraint.getField(), new ArrayList<>());
        }

        map.get(constraint.getField())
            .add(constraint);
    }
}
