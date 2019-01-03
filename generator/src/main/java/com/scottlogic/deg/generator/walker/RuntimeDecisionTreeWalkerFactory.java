package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.walker.reductive.*;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.routes.ExhaustiveProducer;

import java.nio.file.Path;

public class RuntimeDecisionTreeWalkerFactory implements  DecisionTreeWalkerFactory {

    private final FixFieldStrategy fixFieldStrategy;
    private final GenerationConfig config;

    public RuntimeDecisionTreeWalkerFactory(GenerationConfig config, FixFieldStrategy fixFieldStrategy) {
        this.fixFieldStrategy = fixFieldStrategy;
        this.config = config;
    }

    @Override
    public DecisionTreeWalker getDecisionTreeWalker(Path outputPath) {
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();
        FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
        RowSpecMerger rowSpecMerger = new RowSpecMerger(fieldSpecMerger);
        ConstraintReducer constraintReducer = new ConstraintReducer(
            fieldSpecFactory,
            fieldSpecMerger);

        switch (config.getWalkerType()){
            case ROUTED:
                return new DecisionTreeRoutesTreeWalker(
                    constraintReducer,
                    rowSpecMerger,
                    new ExhaustiveProducer());
            case REDUCTIVE:
                IterationVisualiser visualiser = new NoOpIterationVisualiser();

                return new ReductiveDecisionTreeWalker(
                    visualiser,
                    new FixedFieldBuilder(
                        config,
                        constraintReducer,
                        fixFieldStrategy),
                    new ReductiveDecisionTreeReducer(
                        fieldSpecFactory,
                        fieldSpecMerger),
                    new ReductiveRowSpecGenerator(
                        constraintReducer,
                        fieldSpecMerger)
                );
            case CARTESIAN_PRODUCT:
                return new CartesianProductDecisionTreeWalker(
                    constraintReducer,
                    rowSpecMerger);
        }

        throw new UnsupportedOperationException(
            String.format("Walker type %s is not supported", config.getWalkerType()));
    }
}
