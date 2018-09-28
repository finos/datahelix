package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.FieldMapper;
import com.scottlogic.deg.generator.decisiontree.TreePartitioner;
import com.scottlogic.deg.generator.generation.databags.ConcatenatingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.IDataBagSource;
import com.scottlogic.deg.generator.generation.databags.MultiplexingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagSource;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.utils.HardLimitingIterable;
import com.scottlogic.deg.generator.utils.ProjectingIterable;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.*;
import java.util.stream.Collectors;

public class DataGenerator implements IDataGenerator {
    private final RowSpecMerger rowSpecMerger;
    private final ConstraintReducer constraintReducer;
    private final FieldMapper ruleFieldMapper = new FieldMapper();
    private final TreePartitioner fieldPartitioner = new TreePartitioner();

    public DataGenerator(
        RowSpecMerger rowSpecMerger,
        ConstraintReducer constraintReducer) {
        this.rowSpecMerger = rowSpecMerger;
        this.constraintReducer = constraintReducer;
    }

    @Override
    public TestCaseGenerationResult generateData(
        Profile profile,
        DecisionTree decisionTree,
        GenerationConfig generationConfig) {

        final Map<Object, Set<Field>> ruleToFieldMapping = ruleFieldMapper.mapRulesToFields(decisionTree);
        final List<DecisionTree> partitionedTrees = fieldPartitioner.splitTreeIntoPartitions(decisionTree.getRootNode(), ruleToFieldMapping)
            .collect(Collectors.toList());

        final DecisionTreeWalker walker = new DecisionTreeWalker(
                constraintReducer,
                rowSpecMerger);

        final List<List<RowSpec>> rowSpecsByPartition = partitionedTrees
            .stream()
            .map(tree -> walker.walk(tree).collect(Collectors.toList()))
            .collect(Collectors.toList());

        final List<IDataBagSource> allDataBagSources =
            rowSpecsByPartition
                .stream()
                .map(rowSpecs ->
                    rowSpecs
                        .stream()
                        .map(RowSpecDataBagSource::create)
                        .collect(
                            Collectors.collectingAndThen(
                                Collectors.toList(),
                                ConcatenatingDataBagSource::new)))
            .collect(Collectors.toList());

        Iterable<TestCaseDataRow> dataRows = new ProjectingIterable<>(
            new MultiplexingDataBagSource(allDataBagSources).generate(generationConfig),
            dataBag -> new TestCaseDataRow(
                profile.fields.stream()
                    .map(dataBag::getValueAndFormat)
                    .collect(Collectors.toList())));

        dataRows = new HardLimitingIterable<>(dataRows, generationConfig.getMaxRows());

        return new TestCaseGenerationResult(
            profile,
            Arrays.asList(
                new TestCaseDataSet(
                    null,
                    dataRows)));
    }
}
