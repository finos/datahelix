package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.walker.RestartingDataGeneratorDecorator;
import com.scottlogic.deg.generator.walker.ReductiveDataGenerator;

public class DataGeneratorProvider implements Provider<DataGenerator> {

    private final WalkingDataGenerator walkingDataGenerator;
    private final ReductiveDataGenerator reductiveDataGenerator;

    private final GenerationConfigSource configSource;

    private final TreePartitioner treePartitioner;
    private final DecisionTreeOptimiser optimiser;
    private final GenerationConfig generationConfig;

    @Inject
    public DataGeneratorProvider(WalkingDataGenerator walkingDataGenerator,
                                 ReductiveDataGenerator reductiveDataGenerator,
                                 GenerationConfigSource configSource,
                                 TreePartitioner treePartitioner,
                                 DecisionTreeOptimiser optimiser,
                                 GenerationConfig generationConfig){
        this.walkingDataGenerator = walkingDataGenerator;
        this.reductiveDataGenerator = reductiveDataGenerator;
        this.configSource = configSource;
        this.treePartitioner = treePartitioner;
        this.optimiser = optimiser;
        this.generationConfig = generationConfig;
    }

    @Override
    public DataGenerator get() {
        boolean isReductive = configSource.getWalkerType() == GenerationConfig.TreeWalkerType.REDUCTIVE;
        boolean isRandom = configSource.getGenerationType() == GenerationConfig.DataGenerationType.RANDOM;

        DataGenerator underlying = isReductive
            ? reductiveDataGenerator
            : walkingDataGenerator;

        if (configSource.shouldDoPartitioning()){
            underlying = decorateWithPartitioning(underlying);
        }

        if (isRandom && isReductive){
            //restarting should be the outermost step if used with partitioning.
            underlying = decorateWithRestarting(underlying);
        }

        return underlying;
    }

    private DataGenerator decorateWithPartitioning(DataGenerator underlying) {
        return new PartitioningDataGeneratorDecorator(underlying, treePartitioner, optimiser, generationConfig);
    }

    private DataGenerator decorateWithRestarting(DataGenerator underlying) {
        return new RestartingDataGeneratorDecorator(underlying);
    }
}
