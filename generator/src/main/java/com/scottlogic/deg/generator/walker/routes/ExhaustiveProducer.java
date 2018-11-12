package com.scottlogic.deg.generator.walker.routes;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.walker.ConstraintIterator;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ExhaustiveProducer implements RowSpecRouteProducer{
    @Override
    public Stream<RowSpecRoute> produceRoutes(DecisionTree tree) {
        Iterator<RowSpecRoute> iterator = ConstraintIterator.build(tree.getRootNode());
        Iterable<RowSpecRoute> iterable = ()->iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
