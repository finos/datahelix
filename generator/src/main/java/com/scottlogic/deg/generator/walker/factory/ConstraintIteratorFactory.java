package com.scottlogic.deg.generator.walker.factory;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.walker.RouteConstraintIterator;
import com.scottlogic.deg.generator.walker.RouteDecisionIterator;
import com.scottlogic.deg.generator.walker.EndDecisionIterator;
import com.scottlogic.deg.generator.walker.LeafConstraintIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ConstraintIteratorFactory {

    public static ConstraintIterator create(ConstraintNode constraintNode){
        return createConstraintIterator(constraintNode);
    }

    private static ConstraintIterator createConstraintIterator(ConstraintNode constraintNode){
        if (constraintNode.getDecisions().isEmpty()){
            return new LeafConstraintIterator(constraintNode);
        }

        DecisionIterator decisions = createDecisionIterator(new ArrayList<>(constraintNode.getDecisions()));
        return new RouteConstraintIterator(decisions, constraintNode);
    }


    private static DecisionIterator createDecisionIterator(List<DecisionNode> decisionNodes){
        if (decisionNodes == null || decisionNodes.isEmpty())
            return null;

        Collection<ConstraintIterator> options = decisionNodes.get(0).getOptions().stream()
            .map(ConstraintIteratorFactory::createConstraintIterator)
            .collect(Collectors.toSet());

        if(decisionNodes.size() == 1) {
            return new EndDecisionIterator(options);
        }

        List<DecisionNode> nextDecisionNodes = decisionNodes.subList(1, decisionNodes.size());
        DecisionIterator nextDecision = createDecisionIterator(nextDecisionNodes);
        return new RouteDecisionIterator(options, nextDecision);
    }

}
