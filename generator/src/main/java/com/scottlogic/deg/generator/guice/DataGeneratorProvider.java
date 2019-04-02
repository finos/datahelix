package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.*;

public class DataGeneratorProvider implements Provider<DataGenerator> {

    private final WalkingDataGenerator walkingDataGenerator;
    private final PartitioningDataGenerator partitioningDataGenerator;
    private final GenerationConfigSource configSource;

    @Inject
    public DataGeneratorProvider(WalkingDataGenerator walkingDataGenerator,
                                 PartitioningDataGenerator partitioningDataGenerator,
                                 GenerationConfigSource configSource){
        this.walkingDataGenerator = walkingDataGenerator;
        this.partitioningDataGenerator = partitioningDataGenerator;
        this.configSource = configSource;
    }

    @Override
    public DataGenerator get() {
        boolean isRandom = configSource.getGenerationType() == GenerationConfig.DataGenerationType.RANDOM;
        if (configSource.shouldDoPartitioning() && !isRandom){
            //if we partition with random each partition will have random values, but will be static in relation to
            //other partitions, therefore it looks like certain fields do not vary randomly
            return partitioningDataGenerator;
        }
        return walkingDataGenerator;
    }
}
