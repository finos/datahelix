package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.factory.ConstraintIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.Collections;

public class LeafConstraintIterator implements ConstraintIterator {

    private ConstraintNode constraintNode;

    private boolean hasNext = true;

    public LeafConstraintIterator(ConstraintNode constraintNode){
        this.constraintNode = constraintNode;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public RowSpecRoute next() {
        hasNext = false;
        RowSpecRoute rowSpecRoute = new RowSpecRoute();
        rowSpecRoute.chosenOption = constraintNode;
        rowSpecRoute.subRoutes = Collections.emptyList();
        return rowSpecRoute;
    }

    public void reset(){
        hasNext = true;
    }
}
