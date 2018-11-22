package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.factory.ConstraintIterator;
import com.scottlogic.deg.generator.walker.factory.DecisionIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

public class RouteConstraintIterator implements ConstraintIterator {

    ConstraintNode constraintNode;
    private DecisionIterator subDecisions;

    public RouteConstraintIterator(DecisionIterator subDecisions, ConstraintNode constraintNode){
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
