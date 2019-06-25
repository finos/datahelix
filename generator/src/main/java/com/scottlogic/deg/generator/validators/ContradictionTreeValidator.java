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
        ContradictionWrapper contradictionWrapper = getAllNodesInTreeThatAreRootsOfWhollyContradictorySubTrees(decisionTree);
        if (contradictionWrapper.hasNoContradictions()) {
            return decisionTree;
        } else if (contradictionWrapper.isOnlyPartiallyContradictory(decisionTree)) {
            monitor.addLineToPrintAtEndOfGeneration(
                "Warning: There are " + contradictionWrapper.getContradictingNodes().size() +
                    " partial contradiction(s) in the profile. Run the profile through the visualiser for more information.");

            ConstraintNode prunedRootNode = pruneOutPartialContradictions(decisionTree.getRootNode(), decisionTree.getFields());
            return new DecisionTree(prunedRootNode, decisionTree.getFields(), decisionTree.getDescription());
        } else {
            monitor.addLineToPrintAtEndOfGeneration(
                "The provided profile is wholly contradictory. No fields can successfully be fixed." );
            return new DecisionTree(null, decisionTree.getFields(), decisionTree.getDescription());
        }
    }

    private ConstraintNode pruneOutPartialContradictions(ConstraintNode unPrunedNode, ProfileFields profileFields) {
        Map<Field, FieldSpec> fieldSpecs = new HashMap<>();

        for (Field field : profileFields.getFields()) {
            fieldSpecs.put(field, FieldSpec.Empty);
        }
        return treePruner.pruneConstraintNode(unPrunedNode, fieldSpecs).get();
    }

    public ContradictionWrapper getAllNodesInTreeThatAreRootsOfWhollyContradictorySubTrees(DecisionTree decisionTree) {
        return new ContradictionWrapper(getAllNodesThatAreContradictoryAndADescendentOfNode(decisionTree.getRootNode())
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
    }

    private Collection<ConstraintNode> getAllNodesThatAreContradictoryAndADescendentOfNode(ConstraintNode currentNode) {
        if (currentNode.getDecisions().size() == 0) {
            // Base Case
            Node contradiction = findNodeHighestInTreeThatContradictsWithCurrent(currentNode);
            if (contradiction == null) {
                return new ArrayList<>();
            } else {
                return Collections.singleton(currentNode);
            }
        } else {
            // Recursive Case
            // It only makes sense for Constraint Nodes to be checked for contradictions, because if there is a
            // contradiction in a DecisionNode then by definition there must be a contradiction in the parent
            // ConstraintNode. So, add all the Constraint Nodes, which are always children of the children of the
            // current ConstraintNode.
            List<ConstraintNode> nodesToCheck = new ArrayList<>();
            for (DecisionNode decisionNode : currentNode.getDecisions()) {
                nodesToCheck.addAll(decisionNode.getOptions());
            }
            Collection<ConstraintNode> contradictingNodes = new ArrayList<>();

            contradictingNodes.add(findNodeHighestInTreeThatContradictsWithCurrent(currentNode));

            for (ConstraintNode nodeToCheck : nodesToCheck) {
                 contradictingNodes.addAll(getAllNodesThatAreContradictoryAndADescendentOfNode(nodeToCheck));
            }

            return contradictingNodes;
        }
    }

    private ConstraintNode findNodeHighestInTreeThatContradictsWithCurrent(ConstraintNode nodeToCheck){
        return getConstraintNodeIfItContradictsElseReturnNull(nodeToCheck, nodeToCheck);
    }

    private ConstraintNode getConstraintNodeIfItContradictsElseReturnNull(ConstraintNode nodeToCheck, ConstraintNode potentiallyContradictingSubTree){
        if (contradictionChecker.isContradictory(nodeToCheck, potentiallyContradictingSubTree)) {
            return potentiallyContradictingSubTree;
        }

        // If any of the nodes in an AND statement are contradictory, then the statement itself is considered one.
        for (DecisionNode node : potentiallyContradictingSubTree.getDecisions()) {
            boolean contradictionFound = getDecisionNodeIfItContradictsElseReturnNull(nodeToCheck, node) != null;
            if (contradictionFound){
                return potentiallyContradictingSubTree;
            }
        }

        return null;

    }

    private DecisionNode getDecisionNodeIfItContradictsElseReturnNull(ConstraintNode nodeToCheck, DecisionNode potentiallyContradictingSubTree){
        // If all the nodes in an OR statement are contradictory, then the statement itself is considered one.
        boolean contradictionInAllOptions = potentiallyContradictingSubTree.getOptions()
            .stream()
            .allMatch(n -> getConstraintNodeIfItContradictsElseReturnNull(nodeToCheck, n) != null);
        if (contradictionInAllOptions) {
            return potentiallyContradictingSubTree;
        }

        return null;
    }
}
