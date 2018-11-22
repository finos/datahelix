package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomRouteIterator implements Iterator<RowSpecRoute> {

    private int maxIterations;
    private int currentIteration;
    private ConstraintNode initialNode;
    Random rand = new Random();

    public RandomRouteIterator(ConstraintNode initialNode, int maxIterations){
        this.maxIterations = maxIterations;
        this.initialNode = initialNode;
    }

    @Override
    public boolean hasNext() {
        return currentIteration < maxIterations;
    }

    @Override
    public RowSpecRoute next() {
        currentIteration++;
        return new RowSpecRoute(null, produceRoute(initialNode));
    }

    private Collection<RowSpecRoute> produceRoute(ConstraintNode constraint) {
        Collection<DecisionNode> decisions = constraint.getDecisions();

        return decisions.stream().map(d -> produceRoute(d)).collect(Collectors.toSet());
    }

    private RowSpecRoute produceRoute(DecisionNode decision) {
        int decisionIndex = rand.nextInt(decision.getOptions().size());
        ConstraintNode decisionOption = new ArrayList<>(decision.getOptions()).get(decisionIndex);

        return new RowSpecRoute(decisionOption, produceRoute(decisionOption));
    }
}
