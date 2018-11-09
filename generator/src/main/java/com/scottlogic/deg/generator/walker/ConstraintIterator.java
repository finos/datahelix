package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ConstraintIterator implements Iterator<RowSpecRoute> {

    private ConstraintNode constraintNode;

    public ConstraintIterator(ConstraintNode constraintNode){
        this.constraintNode = constraintNode;
    }
    @Override
    public boolean hasNext() {
        if (constraintNode.getDecisions().isEmpty()){
            return false;
        }

        throw new NotImplementedException();
    }

    @Override
    public RowSpecRoute next() {
        return null;
    }
}
