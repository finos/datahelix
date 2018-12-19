package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.ConstraintToFieldMapper;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.NoopTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.TreePartitioner;

public class TreePartitioningProvider implements Provider<TreePartitioner> {
    private GenerateCommandLine commandLine;
    private ConstraintToFieldMapper fieldMapper;

    @Inject
    public TreePartitioningProvider(GenerateCommandLine commandLine, ConstraintToFieldMapper fieldMapper) {
        this.commandLine = commandLine;
        this.fieldMapper = fieldMapper;
    }

    @Override
    public TreePartitioner get() {
        if (this.commandLine.shouldDoPartitioning()){
            return new RelatedFieldTreePartitioner(fieldMapper);
        }
        return new NoopTreePartitioner();
    }
}
