package com.scottlogic.deg.generator.walker.routes;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.Collection;

public class RowSpecRoute {
    public DecisionNode thisDecision;
    public ConstraintNode chosenOption;

    public int decisionIndex;
    public Collection<RowSpecRoute> subRoutes;
}
