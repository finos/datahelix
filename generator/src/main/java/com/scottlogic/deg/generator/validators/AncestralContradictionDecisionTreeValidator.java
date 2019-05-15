package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.Node;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.walker.reductive.Merged;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AncestralContradictionDecisionTreeValidator {

    private DecisionTree decisionTree;
    private final ConstraintReducer constraintReducer;

    public AncestralContradictionDecisionTreeValidator(ConstraintReducer cr){
        constraintReducer = cr;
    }

    public DecisionTree markContradictions(DecisionTree tree) {

        decisionTree = tree;

        // get starting point
        ConstraintNode root =  tree.rootNode;

        // walk tree, check for contradictions in child nodes.
        //recursiveWalkTree(cn);
        inOrderWalk(root);
        // modify the tree


        return tree;
    }

    private void inOrderWalk (Node root){
        //https://java2blog.com/binary-tree-inorder-traversal-in-java/
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
                // perform contradiction check
                boolean contradictionFound = findContradictionForNode(node);
                if(contradictionFound){

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

//    private boolean recursiveWalkTree(Node currentNode){
//
//        if (currentNode instanceof ConstraintNode) {
//            for (DecisionNode nextNode : ((ConstraintNode) currentNode).getDecisions()) {
//                boolean contradictionFound = findContradictionForNode(currentNode);
//                if(contradictionFound){
//                    return true;
//                }
//                recursiveWalkTree(nextNode);
//            }
//        }
//        if(currentNode instanceof DecisionNode){
//            for (ConstraintNode nextNode : ((DecisionNode) currentNode).getOptions()) {
//                boolean contradictionFound = findContradictionForNode(currentNode);
//                if(contradictionFound){
//                    return true;
//                }
//                //recursiveWalkTree(nextNode);
//            }
//        }
//        return false;
//    }

    // kick off a recursive contradiction check
    private boolean findContradictionForNode(Node nodeToCheck){
        if (nodeToCheck instanceof ConstraintNode) {
            for (DecisionNode node : ((ConstraintNode) nodeToCheck).getDecisions()) {
                boolean contradictionFound = recursiveFindContradiction(nodeToCheck, node);
                if (contradictionFound) {
                    return true;
                }
            }
        }
        // remove this.. we can't check for contradictions on a decision node
        if (nodeToCheck instanceof DecisionNode) {
            for (ConstraintNode node : ((DecisionNode) nodeToCheck).getOptions()) {
                boolean contradictionFound = recursiveFindContradiction(nodeToCheck, node);
                if (contradictionFound) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean recursiveFindContradiction(Node nodeToCheck, Node currentNode){

        // can only check for contradictions on ConstraintNodes
        if (currentNode instanceof ConstraintNode && nodeToCheck instanceof ConstraintNode){
            // check for contradiction by creating RowSpec
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
