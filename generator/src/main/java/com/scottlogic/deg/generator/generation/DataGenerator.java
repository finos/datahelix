package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeCollection;
import com.scottlogic.deg.generator.generation.combination_strategies.FieldExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.ConcatenatingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.IDataBagSource;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagSource;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.utils.ProjectingIterable;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.List;
import java.util.stream.Collectors;

public class DataGenerator implements IDataGenerator {
    private final RowSpecMerger rowSpecMerger;
    private final ConstraintReducer constraintReducer;

    public DataGenerator(
            RowSpecMerger rowSpecMerger,
            ConstraintReducer constraintReducer
    ) {
        this.rowSpecMerger = rowSpecMerger;
        this.constraintReducer = constraintReducer;
    }

    @Override
    public Iterable<TestCaseDataRow> generateData(
        ProfileFields profileFields,
        ProfileDecisionTreeCollection analysedProfile) {

        DecisionTreeWalker walker = new DecisionTreeWalker(
                constraintReducer,
                rowSpecMerger);

        List<RowSpec> rowSpecs = walker
            .walk(analysedProfile, profileFields)
            .collect(Collectors.toList());

        IDataBagSource allDataBagSource =
            rowSpecs
                .stream()
                .map(RowSpecDataBagSource::create)
                .collect(
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        ConcatenatingDataBagSource::new));

        GenerationConfig generationConfig = new GenerationConfig(
            GenerationConfig.DataGenerationType.Interesting,
            new FieldExhaustiveCombinationStrategy());

        Iterable<TestCaseDataRow> dataRows = new ProjectingIterable<>(
            allDataBagSource.generate(generationConfig),
            dataBag -> new TestCaseDataRow(
                profileFields.stream()
                    .map(dataBag::getValueAndFormat)
                    .collect(Collectors.toList())));

        return dataRows;
    }
}
