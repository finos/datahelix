package com.scottlogic.deg.generator.walker.builder;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.walker.ConstraintIterator;
import com.scottlogic.deg.generator.walker.DecisionIterator;
import com.scottlogic.deg.generator.walker.EndDecisionIterator;
import com.scottlogic.deg.generator.walker.LeafConstraintIterator;

import java.util.ArrayList;
import java.util.List;

public class ConstraintIteratorFactory {

    public static IConstraintIterator create(ConstraintNode constraintNode){
        return buildConstraintIterator(constraintNode, 0);
    }

    public static IConstraintIterator buildConstraintIterator(ConstraintNode constraintNode, int decisionIndexFromParent){
        if (constraintNode.getDecisions().isEmpty()){
            return new LeafConstraintIterator(decisionIndexFromParent);
        }

        IDecisionIterator decisions = buildDecisionIterator(new ArrayList<>(constraintNode.getDecisions()));
        return new ConstraintIterator(decisions, decisionIndexFromParent);
    }


    public static IDecisionIterator buildDecisionIterator(List<DecisionNode> decisionNodes){
        if (decisionNodes == null || decisionNodes.isEmpty())
            return null;

        List<IConstraintIterator> options = new ArrayList<>();
        int count = 0;
        for (ConstraintNode constraintNode: decisionNodes.get(0).getOptions()) {
            options.add(buildConstraintIterator(constraintNode, count));
            count++;
        }

        if(decisionNodes.size() == 1) {
            return new EndDecisionIterator(options);
        }

        List<DecisionNode> nextDecisionNodes = decisionNodes.subList(1, decisionNodes.size());
        IDecisionIterator nextDecision = buildDecisionIterator(nextDecisionNodes);
        return new DecisionIterator(options, nextDecision);
    }

}
