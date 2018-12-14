package com.scottlogic.deg.generator.walker.iterator;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.Collections;

public class LeafConstraintIterator implements ConstraintIterator {

    private final ConstraintNode constraintNode;

    private boolean hasNext = true;

    LeafConstraintIterator(ConstraintNode constraintNode){
        this.constraintNode = constraintNode;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public RowSpecRoute next() {
        hasNext = false;
        return new RowSpecRoute(constraintNode, Collections.emptySet());
    }

    public void reset(){
        hasNext = true;
    }
}
