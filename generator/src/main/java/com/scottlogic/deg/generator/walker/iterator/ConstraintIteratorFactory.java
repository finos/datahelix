package com.scottlogic.deg.generator.walker.iterator;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class ConstraintIteratorFactory {

    public ConstraintIterator create(ConstraintNode constraintNode){
        return createConstraintIterator(constraintNode);
    }

    private ConstraintIterator createConstraintIterator(ConstraintNode constraintNode){
        if (constraintNode.getDecisions().isEmpty()){
            return new LeafConstraintIterator(constraintNode);
        }

        DecisionIterator decisions = createDecisionIterator(constraintNode.getDecisions().iterator());
        return new RouteConstraintIterator(decisions, constraintNode);
    }


    private DecisionIterator createDecisionIterator(Iterator<DecisionNode> decisionNodes){
        if (decisionNodes == null || !decisionNodes.hasNext())
            return null;

        Collection<ConstraintIterator> options = decisionNodes.next()
            .getOptions().stream()
            .map(this::createConstraintIterator)
            .collect(Collectors.toSet());

        if (options.isEmpty() || !options.iterator().next().hasNext()){
            throw new IllegalArgumentException("Decision with no valid options was supplied");
        }

        if(!decisionNodes.hasNext()) {
            return new EndDecisionIterator(options);
        }

        return new RouteDecisionIterator(options, createDecisionIterator(decisionNodes));
    }

}
