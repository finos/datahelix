package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.walker.CartesianProductDecisionTreeWalker;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

public class CartestianProductWalkerProvider implements Provider<DecisionTreeWalker> {

    private final ConstraintReducer constraintReducer;
    private final RowSpecMerger rowSpecMerger;

    @Inject
    public CartestianProductWalkerProvider(ConstraintReducer constraintReducer,
                                           RowSpecMerger rowSpecMerger) {

        this.constraintReducer = constraintReducer;
        this.rowSpecMerger = rowSpecMerger;
    }

    @Override
    public DecisionTreeWalker get() {
        return new CartesianProductDecisionTreeWalker(constraintReducer, rowSpecMerger);
    }
}
