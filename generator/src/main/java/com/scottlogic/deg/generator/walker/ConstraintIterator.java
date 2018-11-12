package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.builder.DecisionBuilder;
import com.scottlogic.deg.generator.walker.builder.IConstraintIterator;
import com.scottlogic.deg.generator.walker.builder.IDecisionIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import java.util.List;

public class ConstraintIterator implements IConstraintIterator {

    private int decisionIndexFromParent;
    private IDecisionIterator decisions;

    public ConstraintIterator(ConstraintNode constraintNode, int decisionIndexFromParent){
        decisions = DecisionBuilder.build(constraintNode.getDecisions());
        this.decisionIndexFromParent = decisionIndexFromParent;
    }

    @Override
    public boolean hasNext() {
        return decisions.hasNext();
    }

    @Override
    public RowSpecRoute next() {
        RowSpecRoute rowSpecRoute = new RowSpecRoute();
        rowSpecRoute.decisionIndex = decisionIndexFromParent;
        List<RowSpecRoute> next = decisions.next();
        rowSpecRoute.subRoutes = next.toArray(new RowSpecRoute[next.size()]);

        return rowSpecRoute;
    }

    public void reset(){
        decisions.reset();
    }
}
