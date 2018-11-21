package com.scottlogic.deg.generator.walker.factory;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.walker.RouteConstraintIterator;
import com.scottlogic.deg.generator.walker.RouteDecisionIterator;
import com.scottlogic.deg.generator.walker.EndDecisionIterator;
import com.scottlogic.deg.generator.walker.LeafConstraintIterator;

import java.util.ArrayList;
import java.util.List;

public class ConstraintIteratorFactory {

    public static ConstraintIterator create(ConstraintNode constraintNode){
        return createConstraintIterator(constraintNode, 0);
    }

    private static ConstraintIterator createConstraintIterator(ConstraintNode constraintNode, int decisionIndexFromParent){
        if (constraintNode.getDecisions().isEmpty()){
            return new LeafConstraintIterator(decisionIndexFromParent, constraintNode);
        }

        DecisionIterator decisions = createDecisionIterator(new ArrayList<>(constraintNode.getDecisions()));
        return new RouteConstraintIterator(decisions, decisionIndexFromParent, constraintNode);
    }


    private static DecisionIterator createDecisionIterator(List<DecisionNode> decisionNodes){
        if (decisionNodes == null || decisionNodes.isEmpty())
            return null;

        List<ConstraintIterator> options = new ArrayList<>();
        int count = 0;
        for (ConstraintNode constraintNode: decisionNodes.get(0).getOptions()) {
            options.add(createConstraintIterator(constraintNode, count));
            count++;
        }

        if(decisionNodes.size() == 1) {
            return new EndDecisionIterator(options, decisionNodes.get(0));
        }

        List<DecisionNode> nextDecisionNodes = decisionNodes.subList(1, decisionNodes.size());
        DecisionIterator nextDecision = createDecisionIterator(nextDecisionNodes);
        return new RouteDecisionIterator(options, nextDecision, decisionNodes.get(0));
    }

}
