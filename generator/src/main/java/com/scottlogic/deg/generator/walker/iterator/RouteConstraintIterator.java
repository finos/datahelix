package com.scottlogic.deg.generator.walker.iterator;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

public class RouteConstraintIterator implements ConstraintIterator {

    private final ConstraintNode constraintNode;
    private final DecisionIterator subDecisions;

    RouteConstraintIterator(DecisionIterator subDecisions, ConstraintNode constraintNode){
        this.subDecisions = subDecisions;
        this.constraintNode = constraintNode;
    }

    @Override
    public boolean hasNext() {
        return subDecisions.hasNext();
    }

    @Override
    public RowSpecRoute next() {
        return new RowSpecRoute(constraintNode, subDecisions.next());
    }

    public void reset(){
        subDecisions.reset();
    }
}
