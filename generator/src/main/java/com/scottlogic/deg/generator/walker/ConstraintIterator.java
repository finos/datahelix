package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import java.util.List;

public class ConstraintIterator implements IConstraintIterator {

    private int decisionIndexFromParent;
    private IDecisionIterator decisions;

    public static IConstraintIterator build(ConstraintNode constraintNode, int decisionIndexFromParent){
        if (constraintNode.getDecisions().isEmpty()){
            return new LeafConstraintIterator(decisionIndexFromParent);
        }
        return new ConstraintIterator(constraintNode, decisionIndexFromParent);
    }
    public static IConstraintIterator build(ConstraintNode constraintNode){ return build(constraintNode, 0); }

    private ConstraintIterator(ConstraintNode constraintNode, int decisionIndexFromParent){
        decisions = DecisionIterator.build(constraintNode.getDecisions());
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
