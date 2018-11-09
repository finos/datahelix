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

public class ConstraintIterator implements Iterator<RowSpecRoute> {

    private int decisionIndexFromParent;

    private int currentDecision;
    private DecisionIterator decisions;

    public ConstraintIterator(ConstraintNode constraintNode) { this(constraintNode, 0); }
    public ConstraintIterator(ConstraintNode constraintNode, int decisionIndexFromParent){
        decisions = DecisionIterator.build(constraintNode.getDecisions());

        this.decisionIndexFromParent = decisionIndexFromParent;
    }

    @Override
    public boolean hasNext() {
        return currentDecision < 1;
        //TODO this is very wrong
    }

    @Override
    public RowSpecRoute next() {
        currentDecision++;

        if(decisions == null) {
            RowSpecRoute rowSpecRoute = new RowSpecRoute();
            rowSpecRoute.subRoutes = new RowSpecRoute[]{};
            rowSpecRoute.decisionOptionIndex = decisionIndexFromParent;
            return rowSpecRoute;
        }

        throw new NotImplementedException();
    }
}
