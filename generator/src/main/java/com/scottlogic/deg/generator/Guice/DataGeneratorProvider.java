package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.PartitionCombiner;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.*;

public class DataGeneratorProvider implements Provider<DataGenerator> {

    private final DecisionTreeDataGenerator baseGenerator;
    private final GenerationConfigSource config;
    private final TreePartitioner treePartitioner;
    private final PartitionCombiner partitionCombiner;

    @Inject
    public DataGeneratorProvider(DecisionTreeDataGenerator baseGenerator, TreePartitioner treePartitioner, PartitionCombiner partitionCombiner, GenerationConfigSource config){
        this.baseGenerator = baseGenerator;
        this.config = config;
        this.treePartitioner = treePartitioner;
        this.partitionCombiner = partitionCombiner;
    }

    @Override
    public DataGenerator get() {
        DataGenerator dataGenerator = baseGenerator;

        if (config.shouldDoPartitioning()) {
            dataGenerator = new PartitioningDataGeneratorDecorator(dataGenerator, treePartitioner, partitionCombiner);
        }

        return dataGenerator;
    }
}
