package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.NoopTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

public class TreePartitioningProvider implements Provider<TreePartitioner> {
    private final GenerationConfigSource configSource;

    @Inject
    public TreePartitioningProvider(GenerationConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    public TreePartitioner get() {
        boolean isRandom = configSource.getGenerationType() == GenerationConfig.DataGenerationType.RANDOM;
        if (configSource.shouldDoPartitioning() && !isRandom){
            //if we partition with random each partition will have random values, but will be static in relation to
            //other partitions, therefore it looks like certain fields do not vary randomly
            return new RelatedFieldTreePartitioner();
        }
        return new NoopTreePartitioner();
    }
}
