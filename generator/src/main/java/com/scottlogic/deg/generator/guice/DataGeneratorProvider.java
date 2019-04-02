package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.walker.RandomReductiveDecisionTreeWalker;
import com.scottlogic.deg.generator.walker.ReductiveDataGenerator;

public class DataGeneratorProvider implements Provider<DataGenerator> {

    private final WalkingDataGenerator walkingDataGenerator;
    private final ReductiveDataGenerator reductiveDataGenerator;
    private final RandomReductiveDecisionTreeWalker randomReductiveDecisionTreeWalker;
    private final GenerationConfigSource configSource;

    private final TreePartitioner treePartitioner;
    private final DecisionTreeOptimiser optimiser;
    private final ReductiveDataGeneratorMonitor monitor;

    @Inject
    public DataGeneratorProvider(WalkingDataGenerator walkingDataGenerator,
                                 ReductiveDataGenerator reductiveDataGenerator,
                                 RandomReductiveDecisionTreeWalker randomReductiveDecisionTreeWalker,
                                 GenerationConfigSource configSource,
                                 TreePartitioner treePartitioner,
                                 DecisionTreeOptimiser optimiser,
                                 ReductiveDataGeneratorMonitor monitor){
        this.walkingDataGenerator = walkingDataGenerator;
        this.reductiveDataGenerator = reductiveDataGenerator;
        this.randomReductiveDecisionTreeWalker = randomReductiveDecisionTreeWalker;
        this.configSource = configSource;
        this.treePartitioner = treePartitioner;
        this.optimiser = optimiser;
        this.monitor = monitor;
    }

    @Override
    public DataGenerator get() {
        boolean isReductive = configSource.getWalkerType() == GenerationConfig.TreeWalkerType.REDUCTIVE;
        boolean isRandom = configSource.getGenerationType() == GenerationConfig.DataGenerationType.RANDOM;

        DataGenerator underlying = isReductive
            ? reductiveDataGenerator
            : walkingDataGenerator;

        if (isRandom && isReductive){
            return randomReductiveDecisionTreeWalker;
        }

        if (configSource.shouldDoPartitioning() && !isRandom){
            //if we partition with random each partition will have random values, but will be static in relation to
            //other partitions, therefore it looks like certain fields do not vary randomly
            return new PartitioningDataGeneratorDecorator(underlying, treePartitioner, optimiser, monitor);
        }
        return underlying;
    }
}
