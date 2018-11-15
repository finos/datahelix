package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.walker.factory.ConstraintIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

public class LeafConstraintIterator implements ConstraintIterator {

    private int decisionIndexFromParent;

    private boolean hasNext = true;

    public LeafConstraintIterator(int decisionIndexFromParent){
        this.decisionIndexFromParent = decisionIndexFromParent;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public RowSpecRoute next() {
        hasNext = false;
        RowSpecRoute rowSpecRoute = new RowSpecRoute();
        rowSpecRoute.decisionIndex = decisionIndexFromParent;
        rowSpecRoute.subRoutes = new RowSpecRoute[0];
        return rowSpecRoute;
    }

    public void reset(){
        hasNext = true;
    }
}
