package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.NotConstraint;

import java.util.*;
import java.util.stream.Collectors;

public class DecisionTreeOptimiser implements IDecisionTreeOptimiser {
    @Override
    public DecisionTree optimiseTree(DecisionTree tree){
        ConstraintNode rootNode = tree.getRootNode();
        Collection<DecisionNode> decisions = rootNode.getDecisions();
        if (decisions.size() <= 1)
            return tree; //not worth optimising

        optimiseDecisions(tree.getRootNode(), 0);

        return tree;
    }

    private void optimiseDecisions(ConstraintNode rootNode, int depth){
        Collection<DecisionNode> decisions = rootNode.getDecisions();
        if (decisions.size() <= 1)
            return; //not worth optimising

        int iteration = 0;
        int prevDecisionCount = decisions.size();
        while (iteration < 20 && optimiseDecisions(rootNode, rootNode.getDecisions(), depth))
        {
            int newDecisionCount = rootNode.getDecisions().size();
            int changeInDecisionCount = newDecisionCount - prevDecisionCount;
            if (Math.abs(changeInDecisionCount) < 1) {
                break;
            }

            prevDecisionCount = newDecisionCount;
            iteration++;
        }
    }

    private boolean optimiseDecisions(ConstraintNode rootNode, Collection<DecisionNode> decisions, int depth){
        Map<IConstraint, List<IConstraint>> decisionConstraints = decisions
                .stream()
                .flatMap(dn -> dn.getOptions().stream())
                .flatMap(option -> option.getAtomicConstraints().stream())
                .collect(Collectors.groupingBy(c -> c));

        Optional<Map.Entry<IConstraint, List<IConstraint>>> mostProlificConstraintOpt = decisionConstraints.entrySet()
                .stream()
                .sorted(Comparator
                        .comparing(DecisionTreeOptimiser::disfavourNotConstraints)
                        .thenComparing(DecisionTreeOptimiser::orderByFieldName))
                .max(Comparator.comparing(entry -> entry.getValue().size()));

        if (!mostProlificConstraintOpt.isPresent() || mostProlificConstraintOpt.get().getValue().size() == 1)
            return false; //constraint only appears once, cannot be optimised further

        Map.Entry<IConstraint, List<IConstraint>> mostProlificConstraint = mostProlificConstraintOpt.get();
        IConstraint mostProlificAtomicConstraint = mostProlificConstraint.getValue().get(0);
        DecisionNode rootNodeDecision = rootNode.addDecision();

        ConstraintNode newConstraint = new ConstraintNode(mostProlificAtomicConstraint);
        rootNodeDecision.addOption(newConstraint);

        ConstraintNode newNegatedConstraint = new ConstraintNode(NotConstraint.negate(mostProlificAtomicConstraint));
        rootNodeDecision.addOption(newNegatedConstraint);

        for (DecisionNode decision : decisions) {
            for (ConstraintNode option : decision.getOptions())
            {
                boolean matchingConstraints = option.getAtomicConstraints()
                        .stream()
                        .anyMatch(c -> c.equals(mostProlificConstraint.getKey()));

                if (!matchingConstraints)
                    continue;

                DecisionNode newDecision = newConstraint.addDecision();
                ConstraintNode optionWithoutProlificConstraint = option.cloneWithoutAtomicConstraint(mostProlificAtomicConstraint);

                if (!optionWithoutProlificConstraint.getAtomicConstraints().isEmpty()) {
                    newDecision.addOption(optionWithoutProlificConstraint);
                }

                decision.removeOption(option);

                //move all other options on this decision to the root node
                decision.getOptions()
                        .forEach(constraint -> {
                            if (!rootNodeDecision.optionWithAtomicConstraintExists(constraint)) {
                                newDecision.addOption(constraint);
                            }
                        });

                //remove this decision
                rootNode.removeDecision(decision);

                if (newDecision.getOptions().size() == 0) //decision is empty, remove it
                {
                    newConstraint.removeDecision(newDecision);
                }
                else if (newDecision.getOptions().size() == 1){ //simplification
                    newConstraint.addAtomicConstraints(
                            newDecision.getOptions()
                                    .stream()
                                    .flatMap(o -> o.getAtomicConstraints().stream())
                                    .collect(Collectors.toList()));
                    newConstraint.removeDecision(newDecision);
                }

                break;
            }
        }

        optimiseDecisions(newConstraint, depth + 1);
        optimiseDecisions(newNegatedConstraint, depth + 1);

        return true;
    }

    private static int disfavourNotConstraints(Map.Entry<IConstraint, List<IConstraint>> entry){
        return entry.getKey() instanceof NotConstraint ? 1 : 0;
    }

    private static String orderByFieldName(Map.Entry<IConstraint, List<IConstraint>> entry){
        IConstraint constraint = entry.getKey();
        return constraint.toString(); //TODO: Horrible hack; should get the field and order by that, rather than the full representation
    }
}
