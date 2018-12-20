package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.combination.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.walker.reductive.*;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.routes.ExhaustiveProducer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static com.scottlogic.deg.generator.generation.GenerationConfig.ReductionTarget.*;

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

                CombinationProducer combinationProducer;

                if (config.getReductionTarget() == VIOLATE_RULE){
                    combinationProducer = new ViolationCombinationProducer(tree, combinationCreator);
                }
                else if (config.getReductionTarget() == VALID_RULE){
                    combinationProducer = new DecisionTreeCombinationProducer(tree, combinationCreator);
                }
                else throw new NotImplementedException();

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
                        fieldSpecMerger),
                    new ReductiveRowSpecGenerator(
                        constraintReducer,
                        fieldSpecMerger,
                        reductiveMonitor)
                );
                return new CombinationBasedWalker(
                    combinationProducer,
                    reductiveDecisionTreeWalker);
            case CARTESIAN_PRODUCT:
                return new CartesianProductDecisionTreeWalker(
                    constraintReducer,
                    rowSpecMerger);
        }

        throw new UnsupportedOperationException(
            String.format("Walker type %s is not supported", config.getWalkerType()));
    }
}
