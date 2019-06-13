package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.Node;

import java.util.Stack;


public class ContradictionTreeValidator {
    private Node contradictingNode = null;
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
    public Node reportContradictions(DecisionTree decisionTree) {
        Node root = decisionTree.getRootNode();

        return walkTree(root);
    }

    private Node walkTree(Node root){
        Stack<Node> stack = new Stack<>();
        Node currentNode = root;

        while (!stack.empty() || currentNode != null){
            if (currentNode != null) {
                // push this node to the stack, so we can back track to it later.
                stack.push(currentNode);
                // get the next left node if it exists.
                currentNode = currentNode.getFirstChild();
            } else {
                // we reached the bottom of the branch, and now need to back track
                Node node = stack.pop();

                if (node instanceof ConstraintNode) {
                    findContradictionForNode((ConstraintNode)node);
                }
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
            boolean contradiction = contradictionChecker.checkContradictions((ConstraintNode)nodeToCheck, (ConstraintNode)currentNode);
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
}
