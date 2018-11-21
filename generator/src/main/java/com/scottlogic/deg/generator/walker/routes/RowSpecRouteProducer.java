package com.scottlogic.deg.generator.walker.routes;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import java.util.stream.Stream;

public interface RowSpecRouteProducer {
    Stream<RowSpecRoute> produceRoutes(DecisionTree tree);
}
