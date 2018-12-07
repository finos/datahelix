package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.walker.reductive.*;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;

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
                throw new UnsupportedOperationException("RouteProducer isn't implemented yet");
/*
                return new DecisionTreeRoutesTreeWalker(
                    constraintReducer,
                    rowSpecMerger,
                    <the producer>);*/
            case REDUCTIVE:
                IterationVisualiser visualiser = new ReductiveIterationVisualiser(outputPath.getParent());

                return new ReductiveDecisionTreeWalker(
                    visualiser,
                    new FieldCollectionFactory(
                        config,
                        constraintReducer,
                        fieldSpecMerger,
                        fieldSpecFactory,
                        fixFieldStrategy
                    ));
            case CARTESIAN_PRODUCT:
                return new CartesianProductDecisionTreeWalker(
                    constraintReducer,
                    rowSpecMerger);
        }

        throw new UnsupportedOperationException(
            String.format("Walker type %s is not supported", config.getWalkerType()));
    }
}
