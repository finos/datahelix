package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.Node;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.walker.reductive.ReductiveTreePruner;

import java.util.*;
import java.util.stream.Collectors;


public class ContradictionTreeValidator {
    private final ContradictionChecker contradictionChecker;
    private final ReductiveTreePruner treePruner;

    @Inject
    public ContradictionTreeValidator(ContradictionChecker contradictionChecker, ReductiveTreePruner treePruner){
        this.contradictionChecker = contradictionChecker;
        this.treePruner = treePruner;
    }

    public DecisionTree reportThenCullContradictions(DecisionTree decisionTree, DataGeneratorMonitor monitor) {
        Collection<Node> contradictingNodes = reportContradictions(decisionTree);
        if (contradictingNodes.contains(decisionTree.getRootNode())) {
            monitor.addLineToPrintAtEndOfGeneration(
                "The provided profile is wholly contradictory. No fields can successfully be fixed.",
                System.err
            );
            return new DecisionTree(null, decisionTree.getFields(), decisionTree.getDescription());
        } else if (contradictingNodes.size() > 0) {
            monitor.addLineToPrintAtEndOfGeneration(
                "Warning: There are " +
                    contradictingNodes.size() +
                    " partial contradiction(s) in the profile. Run the profile through the visualiser for more information.",
                System.err
            );

            // Remove all contradicting subtrees in the decision tree and return the new tree.
            ConstraintNode prunedRootNode = pruneOutPartialContradictions(decisionTree.getRootNode(), decisionTree.getFields());
            return new DecisionTree(prunedRootNode, decisionTree.getFields(), decisionTree.getDescription());
        }
        // No contradictions.
        return decisionTree;
    }

    private ConstraintNode pruneOutPartialContradictions(ConstraintNode unPrunedNode, ProfileFields profileFields) {
        Map<Field, FieldSpec> fieldSpecs = new HashMap<>();

        for (Field field : profileFields.getFields()) {
            fieldSpecs.put(field, FieldSpec.Empty);
        }
        return treePruner.pruneConstraintNode(unPrunedNode, fieldSpecs).get();
    }

    /**
     * Takes a DecisionTree, walks every node, and check every child of each node for contradictory constraints.
     * @param decisionTree
     * @return all nodes that contain a contradiction. If the root is returned, then the whole tree is contradictory.
     */
    public Collection<Node> reportContradictions(DecisionTree decisionTree) {
        return walkTree(decisionTree.getRootNode());
    }

    private Collection<Node> walkTree(ConstraintNode root){
        return getContradictingNodes(root)
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private Collection<Node> getContradictingNodes(ConstraintNode currentNode) {
        if (currentNode.getDecisions().size() == 0) {
            // Base Case
            Node contradiction = findContradictionForNode(currentNode);
            if (contradiction == null) {
                return new ArrayList<>();
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
     * @return the highest contradiction.
     */
    private ConstraintNode findContradictionForNode(ConstraintNode nodeToCheck){
        return findConstraintContradictions(nodeToCheck, nodeToCheck);
    }

    private ConstraintNode findConstraintContradictions(ConstraintNode nodeToCheck, ConstraintNode currentNode){
        if (contradictionChecker.isContradictory(nodeToCheck, currentNode)) {
            return currentNode;
        }

        // If any of the nodes in an AND statement are contradictory, then the statement itself is considered one.
        for (DecisionNode node : currentNode.getDecisions()) {
            boolean contradictionFound = findDecisionContradictions(nodeToCheck, node) != null;
            if (contradictionFound){
                return currentNode;
            }
        }

        // no more nodes, and no contradiction found.
        return null;

    }

    private DecisionNode findDecisionContradictions(ConstraintNode nodeToCheck, DecisionNode currentNode){
        // If all the nodes in an OR statement are contradictory, then the statement itself is considered one.
        boolean contradictionInAllOptions = currentNode.getOptions()
            .stream()
            .allMatch(n -> findConstraintContradictions(nodeToCheck, n) != null);
        if (contradictionInAllOptions) {
            return currentNode;
        }

        // no more nodes, and no contradiction found.
        return null;
    }
}
