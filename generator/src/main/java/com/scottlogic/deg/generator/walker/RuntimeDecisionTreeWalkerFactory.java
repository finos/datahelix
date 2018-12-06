package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.walker.reductive.*;

public class RuntimeDecisionTreeWalkerFactory implements  DecisionTreeWalkerFactory {

    private final GenerationConfig config;
    private final DataGeneratorMonitor monitor;

    public RuntimeDecisionTreeWalkerFactory(GenerationConfig config, DataGeneratorMonitor monitor) {
        this.config = config;
        this.monitor = monitor;
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
                ReductiveDataGeneratorMonitor reductiveMonitor = this.monitor instanceof ReductiveDataGeneratorMonitor
                    ? (ReductiveDataGeneratorMonitor) this.monitor
                    : new NoopDataGeneratorMonitor();

                return new ReductiveDecisionTreeWalker(
                    visualiser,
                    new FieldCollectionFactory(
                        config,
                        constraintReducer,
                        fieldSpecMerger,
                        fieldSpecFactory,
                        fixFieldStrategy,
                        reductiveMonitor),
                    reductiveMonitor);
            case CARTESIAN_PRODUCT:
                return new CartesianProductDecisionTreeWalker(
                    constraintReducer,
                    rowSpecMerger);
        }

        throw new UnsupportedOperationException(
            String.format("Walker type %s is not supported", config.getWalkerType()));
    }
}
