package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.management.Notification;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

//TODO re implement with end node as different class, and use a builder to construct
public class ConstraintIterator implements Iterator<RowSpecRoute> {

    private int decisionIndexFromParent;

    private DecisionIterator decisions;
    private boolean hasNext;

    public ConstraintIterator(ConstraintNode constraintNode) { this(constraintNode, 0); }
    public ConstraintIterator(ConstraintNode constraintNode, int decisionIndexFromParent){
        decisions = DecisionIterator.build(constraintNode.getDecisions());
        hasNext = true;
        this.decisionIndexFromParent = decisionIndexFromParent;
    }

    @Override
    public boolean hasNext() {
        if (decisions == null) {
            return hasNext;
        }

        return decisions.hasNext();
    }

    @Override
    public RowSpecRoute next() {

        if(decisions == null) {
            hasNext = false;
            RowSpecRoute rowSpecRoute = new RowSpecRoute();
            rowSpecRoute.decisionIndex = decisionIndexFromParent;
            rowSpecRoute.subRoutes = new RowSpecRoute[]{};
            return rowSpecRoute;
        }

        RowSpecRoute rowSpecRoute = new RowSpecRoute();
        rowSpecRoute.decisionIndex = decisionIndexFromParent;
        List<RowSpecRoute> next = decisions.next();
        rowSpecRoute.subRoutes = next.toArray(new RowSpecRoute[next.size()]);

        return rowSpecRoute;
    }

    void reset(){
        hasNext=true;
        if(decisions!= null) decisions.reset();
    }
}
