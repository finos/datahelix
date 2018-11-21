package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import com.scottlogic.deg.generator.walker.routes.RowSpecRouteProducer;

import java.util.Arrays;
import java.util.stream.Stream;

public class TestRowSpecRouteProducer implements RowSpecRouteProducer {
    private final RowSpecRoute[] routes;
    public DecisionTree actualDecisionTree = null;

    public TestRowSpecRouteProducer(RowSpecRoute... routes) {

        this.routes = routes;
    }

    @Override
    public Stream<RowSpecRoute> produceRoutes(DecisionTree tree) {
        actualDecisionTree = tree;
        return Arrays.stream(routes);
    }
}
