package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.*;
import java.util.stream.Collectors;


public class ContradictionTreeValidator {
    private final ContradictionChecker contradictionChecker;

    @Inject
    public ContradictionTreeValidator(ContradictionChecker contradictionChecker){
        this.contradictionChecker = contradictionChecker;
    }

    /**
     * Takes a DecisionTree, walks every node, and check every child of each node for contradictory constraints.
     * @param decisionTree
     * @return the contradicting Node, or null if there are no contradictions.
     */
    public Collection<Node> reportContradictions(DecisionTree decisionTree) {
        return walkTree(decisionTree.getRootNode());
    }

    private Collection<Node> walkTree(ConstraintNode root){
        Collection<Node> contradictingNodes = getContradictingNodes(root)
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return contradictingNodes;
    }

    private Collection<Node> getContradictingNodes(ConstraintNode currentNode) {
        if (currentNode.getDecisions().size() == 0) {
            // Base Case
            Node contradiction = findContradictionForNode(currentNode);
            if (contradiction == null) {
                return Collections.EMPTY_LIST;
            } else {
                return Collections.singleton(currentNode);
            }
        } else {
            // Recursive Case
            // Only Constraint Nodes can be checked for contradictions, so add all the Constraint Nodes, which are
            // always children of the children of the current ConstraintNode.
            List<ConstraintNode> nodesToCheck = new ArrayList<>();
            for (DecisionNode decisionNode : currentNode.getDecisions()) {
                nodesToCheck.addAll(decisionNode.getOptions());
            }
            Collection<Node> contradictingNodes = new ArrayList<>();

            contradictingNodes.add(findContradictionForNode(currentNode)); // Check from the current node.

            for (ConstraintNode nodeToCheck : nodesToCheck) {
                 contradictingNodes.addAll(getContradictingNodes(nodeToCheck));
            }

            return contradictingNodes;
        }
    }

    /**
     * Recursively looks for constraint contradictions between the nodeToCheck, and its descendants
     * @param nodeToCheck the node that should be checked for contradictions
     * @return return true if a contradiction is found.
     */
    private Node findContradictionForNode(ConstraintNode nodeToCheck){
        return recursiveFindContradiction(nodeToCheck, nodeToCheck);
    }

    private Node recursiveFindContradiction(ConstraintNode nodeToCheck, ConstraintNode currentNode){
        // can only check for contradictions on ConstraintNodes
        if (contradictionChecker.isContradictory(nodeToCheck, currentNode)) {
            return currentNode;
        }

        // no contradiction, call next node.
        // If any of the nodes in an AND statement are contradictory, then the statement itself is considered one.
        for (DecisionNode node : currentNode.getDecisions()) {
            boolean contradictionFound = recursiveFindContradiction(nodeToCheck, node) != null;
            if (contradictionFound){
                return currentNode;
            }
        }

        // no more nodes, and no contradiction found.
        return null;

    }

    private Node recursiveFindContradiction(ConstraintNode nodeToCheck, DecisionNode currentNode){
        // If all the nodes in an OR statement are contradictory, then the statement itself is considered one.
        boolean contradictionInAllOptions = currentNode.getOptions()
            .stream()
            .allMatch(n -> recursiveFindContradiction(nodeToCheck, n) != null);
        if (contradictionInAllOptions) {
            return currentNode;
        }

        // no more nodes, and no contradiction found.
        return null;

    }
}
