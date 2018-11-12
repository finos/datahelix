package com.scottlogic.deg.generator.walker.builder;

import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.walker.DecisionIterator;
import com.scottlogic.deg.generator.walker.EndDecisionIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class DecisionBuilder {
    public static IDecisionIterator build(Collection<DecisionNode> decisionNodes){
        if (decisionNodes == null || decisionNodes.isEmpty())
            return null;

        if(decisionNodes.size() == 1)
            return new EndDecisionIterator(new ArrayList<>(decisionNodes).get(0));

        return new DecisionIterator(new LinkedList<>(decisionNodes));
    }
}
