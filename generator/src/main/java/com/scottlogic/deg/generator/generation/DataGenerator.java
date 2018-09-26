package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeProfile;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        DecisionTreeProfile analysedProfile,
        GenerationConfig generationConfig) {

        final Map<Object, List<Field>> ruleToFieldMapping = ruleFieldMapper.mapRulesToFields(analysedProfile);
        final Stream<ConstraintNode> partitionedTrees = fieldPartitioner.splitTreeIntoPartitions(analysedProfile.getRootNode(), ruleToFieldMapping);

        final DecisionTreeWalker walker = new DecisionTreeWalker(
                constraintReducer,
                rowSpecMerger);

        final Stream<Stream<RowSpec>> rowSpecsByPartition = partitionedTrees
            .map(rootNode -> walker.walk(rootNode, profile.fields));

        final List<IDataBagSource> allDataBagSource =
            rowSpecsByPartition
                .map(rowSpecs ->
                    rowSpecs
                        .map(RowSpecDataBagSource::create)
                        .collect(
                            Collectors.collectingAndThen(
                                Collectors.toList(),
                                ConcatenatingDataBagSource::new)))
            .collect(Collectors.toList());

        Iterable<TestCaseDataRow> dataRows = new ProjectingIterable<>(
            new MultiplexingDataBagSource(allDataBagSource).generate(generationConfig),
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
