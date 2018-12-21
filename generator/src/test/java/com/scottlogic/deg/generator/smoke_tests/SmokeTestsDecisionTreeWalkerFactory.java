package com.scottlogic.deg.generator.smoke_tests;

import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.DecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.CartesianProductDecisionTreeWalker;

import java.nio.file.Path;

public class SmokeTestsDecisionTreeWalkerFactory implements DecisionTreeWalkerFactory {

    @Override
    public DecisionTreeWalker getDecisionTreeWalker(Path outputPath) {
        FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
        RowSpecMerger rowSpecMerger = new RowSpecMerger(fieldSpecMerger);
        ConstraintReducer constraintReducer = new ConstraintReducer(
            new FieldSpecFactory(),
            fieldSpecMerger);

        return new CartesianProductDecisionTreeWalker(
            constraintReducer,
            rowSpecMerger);
    }
}
