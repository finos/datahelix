package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.walker.reductive.*;

public class RuntimeDecisionTreeWalkerFactory implements  DecisionTreeWalkerFactory {

    private final GenerationConfig config;

    public RuntimeDecisionTreeWalkerFactory(GenerationConfig config) {
        this.config = config;
    }

    @Override
    public DecisionTreeWalker getDecisionTreeWalker() {
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
                IterationVisualiser visualiser = new ReductiveIterationVisualiser();
                FixFieldStrategy fixFieldStrategy = new InitialFixFieldStrategy();

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
