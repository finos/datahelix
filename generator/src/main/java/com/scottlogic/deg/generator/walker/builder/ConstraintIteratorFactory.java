package com.scottlogic.deg.generator.walker.builder;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.walker.RouteConstraintIterator;
import com.scottlogic.deg.generator.walker.RouteDecisionIterator;
import com.scottlogic.deg.generator.walker.EndDecisionIterator;
import com.scottlogic.deg.generator.walker.LeafConstraintIterator;

import java.util.ArrayList;
import java.util.List;

public class ConstraintIteratorFactory {

    public static IConstraintIterator create(ConstraintNode constraintNode){
        return createConstraintIterator(constraintNode, 0);
    }

    private static IConstraintIterator createConstraintIterator(ConstraintNode constraintNode, int decisionIndexFromParent){
        if (constraintNode.getDecisions().isEmpty()){
            return new LeafConstraintIterator(decisionIndexFromParent);
        }

        IDecisionIterator decisions = createDecisionIterator(new ArrayList<>(constraintNode.getDecisions()));
        return new RouteConstraintIterator(decisions, decisionIndexFromParent);
    }


    private static IDecisionIterator createDecisionIterator(List<DecisionNode> decisionNodes){
        if (decisionNodes == null || decisionNodes.isEmpty())
            return null;

        List<IConstraintIterator> options = new ArrayList<>();
        int count = 0;
        for (ConstraintNode constraintNode: decisionNodes.get(0).getOptions()) {
            options.add(createConstraintIterator(constraintNode, count));
            count++;
        }

        if(decisionNodes.size() == 1) {
            return new EndDecisionIterator(options);
        }

        List<DecisionNode> nextDecisionNodes = decisionNodes.subList(1, decisionNodes.size());
        IDecisionIterator nextDecision = createDecisionIterator(nextDecisionNodes);
        return new RouteDecisionIterator(options, nextDecision);
    }

}
