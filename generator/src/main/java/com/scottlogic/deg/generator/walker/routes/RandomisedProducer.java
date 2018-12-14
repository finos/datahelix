package com.scottlogic.deg.generator.walker.routes;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.walker.RandomRouteIterator;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RandomisedProducer implements RowSpecRouteProducer {

    int totalIterations;

    public RandomisedProducer(int totalIterations){
        this.totalIterations = totalIterations;
    }

    @Override
    public Stream<RowSpecRoute> produceRoutes(DecisionTree tree) {
        Iterator<RowSpecRoute> iterator = new RandomRouteIterator(tree.getRootNode(), totalIterations);
        Iterable<RowSpecRoute> iterable = ()->iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
