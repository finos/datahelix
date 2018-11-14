package com.scottlogic.deg.generator.walker.builder;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.walker.ConstraintIterator;
import com.scottlogic.deg.generator.walker.DecisionIterator;
import com.scottlogic.deg.generator.walker.EndDecisionIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DecisionBuilder {
    public static IDecisionIterator build(List<DecisionNode> decisionNodes){
        if (decisionNodes == null || decisionNodes.isEmpty())
            return null;

        List<IConstraintIterator> options = new ArrayList<>();
        int count = 0;
        for (ConstraintNode constraintNode: decisionNodes.get(0).getOptions()) {
            options.add(ConstraintBuilder.build(constraintNode, count));
            count++;
        }
        
        if(decisionNodes.size() == 1) {
            return new EndDecisionIterator(options);
        }

        List<DecisionNode> nextDecisionNodes = decisionNodes.subList(1, decisionNodes.size());
        IDecisionIterator nextDecision = DecisionBuilder.build(nextDecisionNodes);
        return new DecisionIterator(options, nextDecision);
    }
}
