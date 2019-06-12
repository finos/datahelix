package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
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

    private final ConstraintReducer constraintReducer;
    private Node contradictingNode = null;

    @Inject
    public ContradictionTreeValidator(ConstraintReducer constraintReducer){
        this.constraintReducer = constraintReducer;
    }

    /**
     * Takes a DecisionTree, walks every node, and check every child of each node for contradictory constraints.
     * @param decisionTree
     */
    public Node reportContradictions(DecisionTree decisionTree) {
        Node root = decisionTree.getRootNode();

        return walkTree(root);
    }

    private Node walkTree(Node root){
        Stack<Node> stack = new Stack<>();
        Node currentNode = root;

        while(!stack.empty() || currentNode!=null){
            if(currentNode!=null)
            {
                // push this node to the stack, so we can back track to it later.
                stack.push(currentNode);
                // get the next left node if it exists.
                currentNode = currentNode.getFirstChild();
            }
            else
            {
                // we reached the bottom of the branch, and now need to back track
                Node node = stack.pop();

                if(node instanceof ConstraintNode){
                    findContradictionForNode((ConstraintNode)node);
                }

                currentNode = null;
            }
        }
        return contradictingNode;
    }

    /**
     * Recursively looks for constraint contradictions between the nodeToCheck, and its descendants
     * @param nodeToCheck the node that should be checked for contradictions
     * @return return true if a contradiction is found.
     */
    private boolean findContradictionForNode(ConstraintNode nodeToCheck){
        return recursiveFindContradiction(nodeToCheck, nodeToCheck);
    }

    private boolean recursiveFindContradiction(Node nodeToCheck, Node currentNode){
        // can only check for contradictions on ConstraintNodes
        if (currentNode instanceof ConstraintNode && nodeToCheck instanceof ConstraintNode){
            boolean contradiction = checkContradictions((ConstraintNode)nodeToCheck, (ConstraintNode)currentNode);
            if (contradiction) {
                contradictingNode = currentNode;
                return true;
            }
        }

        // no contradiction, call next node.
        if (currentNode instanceof ConstraintNode) {
            // If any of the nodes in an AND statement are contradictory, then the statement itself is considered one.
            for (DecisionNode node : ((ConstraintNode) currentNode).getDecisions()) {
                boolean contradictionFound = recursiveFindContradiction(nodeToCheck, node);
                if (contradictionFound){
                    contradictingNode = currentNode;
                    return true;
                }
            }
        }
        if(currentNode instanceof DecisionNode){
            // If all the nodes in an OR statement are contradictory, then the statement itself is considered one.
            boolean contradictionInAllOptions = ((DecisionNode) currentNode).getOptions()
                .stream()
                .allMatch(n -> recursiveFindContradiction(nodeToCheck, n));
            if (contradictionInAllOptions) {
                contradictingNode = currentNode;
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
            .map(AtomicConstraint::getField)
            .collect(Collectors.toCollection(ArrayList::new));

        Collection<Field> rightFields = rightNode.getAtomicConstraints()
            .stream()
            .map(AtomicConstraint::getField)
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
