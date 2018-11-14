package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.walker.builder.IConstraintIterator;
import com.scottlogic.deg.generator.walker.builder.IDecisionIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import java.util.List;

public class ConstraintIterator implements IConstraintIterator {

    private int decisionIndexFromParent;
    private IDecisionIterator subDecisions;

    public ConstraintIterator(IDecisionIterator subDecisions, int decisionIndexFromParent){
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
