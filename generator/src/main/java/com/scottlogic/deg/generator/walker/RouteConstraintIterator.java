package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.walker.factory.ConstraintIterator;
import com.scottlogic.deg.generator.walker.factory.DecisionIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import java.util.List;

public class RouteConstraintIterator implements ConstraintIterator {

    ConstraintNode constraintNode;
    DecisionNode parentDecision;
    private DecisionIterator subDecisions;

    public RouteConstraintIterator(DecisionIterator subDecisions, ConstraintNode constraintNode, DecisionNode parentDecision){
        this.subDecisions = subDecisions;
        this.constraintNode = constraintNode;
        this.parentDecision = parentDecision;
    }

    @Override
    public boolean hasNext() {
        return subDecisions.hasNext();
    }

    @Override
    public RowSpecRoute next() {
        RowSpecRoute rowSpecRoute = new RowSpecRoute();
        rowSpecRoute.chosenOption = constraintNode;
        rowSpecRoute.thisDecision = parentDecision;
        rowSpecRoute.subRoutes = subDecisions.next();

        return rowSpecRoute;
    }

    public void reset(){
        subDecisions.reset();
    }
}
