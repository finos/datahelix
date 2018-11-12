package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.ITreePartitioner;
import com.scottlogic.deg.generator.generation.databags.ConcatenatingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.IDataBagSource;
import com.scottlogic.deg.generator.generation.databags.MultiplexingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagSource;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataGenerator implements IDataGenerator {
    private final RowSpecMerger rowSpecMerger;
    private final ConstraintReducer constraintReducer;
    private final ITreePartitioner treePartitioner;
    private final IDecisionTreeOptimiser treeOptimiser;

    public DataGenerator(
            RowSpecMerger rowSpecMerger,
            ConstraintReducer constraintReducer,
            ITreePartitioner treePartitioner,
            IDecisionTreeOptimiser optimiser) {
        this.rowSpecMerger = rowSpecMerger;
        this.constraintReducer = constraintReducer;
        this.treePartitioner = treePartitioner;
        this.treeOptimiser = optimiser;
    }

    @Override
    public Stream<GeneratedObject> generateData(
        Profile profile,
        DecisionTree decisionTree,
        GenerationConfig generationConfig) {

        final List<DecisionTree> partitionedTrees =
            treePartitioner
                .splitTreeIntoPartitions(decisionTree)
                    .map(this.treeOptimiser::optimiseTree)
                .collect(Collectors.toList());

        final DecisionTreeWalker walker = new DecisionTreeWalker(
                constraintReducer,
                rowSpecMerger);

        final Stream<Stream<RowSpec>> rowSpecsByPartition = partitionedTrees
            .stream()
            .map(walker::walk);

        final Stream<IDataBagSource> allDataBagSources =
            rowSpecsByPartition
                .map(rowSpecs ->
                    new ConcatenatingDataBagSource(
                        rowSpecs
                            .map(RowSpecDataBagSource::create)));

        Stream<GeneratedObject> dataRows = new MultiplexingDataBagSource(allDataBagSources)
            .generate(generationConfig)
            .map(dataBag -> new GeneratedObject(
                profile.fields.stream()
                    .map(dataBag::getValueAndFormat)
                    .collect(Collectors.toList())));

        dataRows = dataRows
            .limit(generationConfig.getMaxRows());

        return dataRows;

    }
}
