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
        return createConstraintIterator(constraintNode, null);
    }

    private static ConstraintIterator createConstraintIterator(ConstraintNode constraintNode, DecisionNode parentDecision){
        if (constraintNode.getDecisions().isEmpty()){
            return new LeafConstraintIterator(constraintNode, parentDecision);
        }

        DecisionIterator decisions = createDecisionIterator(new ArrayList<>(constraintNode.getDecisions()));
        return new RouteConstraintIterator(decisions, constraintNode, parentDecision);
    }


    private static DecisionIterator createDecisionIterator(List<DecisionNode> decisionNodes){
        if (decisionNodes == null || decisionNodes.isEmpty())
            return null;

        List<ConstraintIterator> options = new ArrayList<>();
        for (ConstraintNode constraintNode: decisionNodes.get(0).getOptions()) {
            options.add(createConstraintIterator(constraintNode, decisionNodes.get(0)));
        }//

        if(decisionNodes.size() == 1) {
            return new EndDecisionIterator(options);
        }

        List<DecisionNode> nextDecisionNodes = decisionNodes.subList(1, decisionNodes.size());
        DecisionIterator nextDecision = createDecisionIterator(nextDecisionNodes);
        return new RouteDecisionIterator(options, nextDecision);
    }

}
