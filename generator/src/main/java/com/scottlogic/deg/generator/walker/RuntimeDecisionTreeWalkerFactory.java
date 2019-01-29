package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeSimplifier;
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
    private final DataGeneratorMonitor monitor;
    private final FieldSpecValueGenerator valueGenerator;

    @Inject
    public RuntimeDecisionTreeWalkerFactory(GenerationConfig config, DataGeneratorMonitor monitor, FixFieldStrategy fixFieldStrategy, FieldSpecValueGenerator valueGenerator) {
        this.fixFieldStrategy = fixFieldStrategy;
        this.config = config;
        this.monitor = monitor;
        this.valueGenerator = valueGenerator;
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
                ReductiveDataGeneratorMonitor reductiveMonitor = this.monitor instanceof ReductiveDataGeneratorMonitor
                    ? (ReductiveDataGeneratorMonitor) this.monitor
                    : new NoopDataGeneratorMonitor();

                return new ReductiveDecisionTreeWalker(
                    visualiser,
                    new FixedFieldBuilder(
                        config,
                        constraintReducer,
                        fixFieldStrategy,
                        reductiveMonitor,
                        valueGenerator),
                    reductiveMonitor,
                    new ReductiveDecisionTreeReducer(
                        fieldSpecFactory,
                        fieldSpecMerger,
                        new DecisionTreeSimplifier()),
                    new ReductiveRowSpecGenerator(
                        constraintReducer,
                        fieldSpecMerger,
                        reductiveMonitor)
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
