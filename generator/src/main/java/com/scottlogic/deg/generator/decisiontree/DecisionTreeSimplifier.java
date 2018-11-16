package com.scottlogic.deg.generator.decisiontree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionTreeSimplifier {
    public DecisionTree simplify(DecisionTree originalTree) {
        return new DecisionTree(
            simplify(originalTree.getRootNode()),
            originalTree.getFields(),
            originalTree.getDescription());
    }

    public ConstraintNode simplify(ConstraintNode node) {
        if (node.getDecisions().isEmpty())
            return node;

        return new TreeConstraintNode(
            node.getAtomicConstraints(),
            node.getDecisions().stream()
                .map(this::simplify)
                .collect(Collectors.toList()));
    }

    private DecisionNode simplify(DecisionNode decision) {
        List<ConstraintNode> newNodes = new ArrayList<>();

        for (ConstraintNode existingOption : decision.getOptions()) {
            ConstraintNode simplifiedNode = simplify(existingOption);

            // if an option contains no constraints and only one decision, then it can be replaced by the set of options within that decision.
            // this helps simplify the sorts of trees that come from eg A OR (B OR C)
            if (simplifiedNode.getAtomicConstraints().isEmpty() && simplifiedNode.getDecisions().size() == 1) {
                newNodes.addAll(
                    simplifiedNode.getDecisions()
                        .iterator().next() //get only member
                        .getOptions());
            } else {
                newNodes.add(simplifiedNode);
            }
        }

        return new TreeDecisionNode(newNodes);
    }
}
