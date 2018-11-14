package com.scottlogic.deg.generator.smoke_tests;

import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.DecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.ExhaustiveDecisionTreeWalker;

public class ExhaustiveDecisionTreeWalkerFactory implements DecisionTreeWalkerFactory {

    @Override
    public DecisionTreeWalker getDecisionTreeWalker() {
        FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
        RowSpecMerger rowSpecMerger = new RowSpecMerger(fieldSpecMerger);
        ConstraintReducer constraintReducer = new ConstraintReducer(
            new FieldSpecFactory(),
            fieldSpecMerger);

        return new ExhaustiveDecisionTreeWalker(
            constraintReducer,
            rowSpecMerger);
    }
}
