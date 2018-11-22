package com.scottlogic.deg.generator.walker.routes;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;

import java.util.Collection;

public class RowSpecRoute {
    public RowSpecRoute(ConstraintNode chosenOption, Collection<RowSpecRoute> subroutes){
        this.chosenOption = chosenOption;
        this.subRoutes = subroutes;
    }

    public ConstraintNode chosenOption;
    public Collection<RowSpecRoute> subRoutes;
}
