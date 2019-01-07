package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.combination.*;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeSimplifier;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.walker.reductive.*;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.routes.ExhaustiveProducer;

public class RuntimeDecisionTreeWalkerFactory implements DecisionTreeWalkerFactory {

    private final FixFieldStrategy fixFieldStrategy;
    private final GenerationConfig config;
    private final DataGeneratorMonitor monitor;

    public RuntimeDecisionTreeWalkerFactory(GenerationConfig config, DataGeneratorMonitor monitor, FixFieldStrategy fixFieldStrategy) {
        this.fixFieldStrategy = fixFieldStrategy;
        this.config = config;
        this.monitor = monitor;
    }

    @Override
    public DecisionTreeWalker getDecisionTreeWalker(DecisionTree tree) {
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
                CombinationCreator combinationCreator = new CombinationCreator(
                    constraintReducer,
                    config,
                    new SingleCombinationStrategy());

                CombinationProducer combinationProducer = new DecisionTreeCombinationProducer(combinationCreator);

                if (config.getReductionTarget() == GenerationConfig.ReductionTarget.VIOLATE_RULE){
                    combinationProducer = new ViolationCombinationProducer(combinationCreator);
                }

                IterationVisualiser visualiser = new NoOpIterationVisualiser();
                ReductiveDataGeneratorMonitor reductiveMonitor = this.monitor instanceof ReductiveDataGeneratorMonitor
                    ? (ReductiveDataGeneratorMonitor) this.monitor
                    : new NoopDataGeneratorMonitor();

                ReductiveDecisionTreeWalker reductiveDecisionTreeWalker = new ReductiveDecisionTreeWalker(
                    visualiser,
                    new FixedFieldBuilder(
                        config,
                        constraintReducer,
                        fixFieldStrategy,
                        reductiveMonitor),
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
                return new CombinationBasedWalker(
                    combinationProducer,
                    reductiveDecisionTreeWalker,
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
