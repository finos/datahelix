package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.walker.builder.IConstraintIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

public class LeafConstraintIterator implements IConstraintIterator {

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
        rowSpecRoute.subRoutes = new RowSpecRoute[]{};
        return rowSpecRoute;
    }

    public void reset(){
        hasNext = true;
    }
}
