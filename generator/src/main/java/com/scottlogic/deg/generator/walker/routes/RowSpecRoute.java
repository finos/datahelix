package com.scottlogic.deg.generator.walker.routes;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.Collection;

public class RowSpecRoute {
    public ConstraintNode chosenOption;
    public Collection<RowSpecRoute> subRoutes;
}
