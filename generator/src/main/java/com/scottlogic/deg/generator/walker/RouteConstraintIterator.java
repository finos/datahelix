package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.walker.factory.ConstraintIterator;
import com.scottlogic.deg.generator.walker.factory.DecisionIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import java.util.List;

public class RouteConstraintIterator implements ConstraintIterator {

    private int decisionIndexFromParent;
    private DecisionIterator subDecisions;

    public RouteConstraintIterator(DecisionIterator subDecisions, int decisionIndexFromParent){
        this.subDecisions = subDecisions;
        this.decisionIndexFromParent = decisionIndexFromParent;
    }

    @Override
    public boolean hasNext() {
        return subDecisions.hasNext();
    }

    @Override
    public RowSpecRoute next() {
        RowSpecRoute rowSpecRoute = new RowSpecRoute();
        rowSpecRoute.decisionIndex = decisionIndexFromParent;
        List<RowSpecRoute> subroutes = subDecisions.next();
        rowSpecRoute.subRoutes = subroutes.toArray(new RowSpecRoute[subroutes.size()]);

        return rowSpecRoute;
    }

    public void reset(){
        subDecisions.reset();
    }
}
