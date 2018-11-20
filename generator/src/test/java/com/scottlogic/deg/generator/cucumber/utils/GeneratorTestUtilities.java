package com.scottlogic.deg.generator.cucumber.utils;

import com.scottlogic.deg.generator.*;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.cucumber.steps.DateValueStep;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.NoopTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.generation.combination_strategies.FieldExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneratorTestUtilities {

    /**
     * Runs the data generator and returns list of generated result data.
     *
     * @return Generated data
     */
    static List<List<Object>> getDEGGeneratedData(List<Field> profileFields, List<IConstraint> constraints, GenerationConfig.DataGenerationType generationStrategy) {
        return getGeneratedDataAsList(profileFields, constraints, generationStrategy)
            .stream()
            .map(genObj ->
                genObj.values
                    .stream()
                    .map(obj -> {
                        if (obj.value != null && obj.format != null) {
                            return String.format(obj.format, obj.value);
                        }
                        return obj.value;
                    })
                    .collect(Collectors.toList())
            ).collect(Collectors.toList());
    }

    private static List<GeneratedObject> getGeneratedDataAsList(List<Field> profileFields, List<IConstraint> constraints, GenerationConfig.DataGenerationType generationStrategy) {
        Profile profile = new Profile(
            new ProfileFields(profileFields),
            Collections.singleton(new Rule("TEST_RULE", constraints)));

        final DecisionTreeCollection analysedProfile = new DecisionTreeGenerator().analyse(profile);

        final IDataGenerator dataGenerator = new DataGenerator(
            new RowSpecMerger(
                new FieldSpecMerger()),
            new ConstraintReducer(
                new FieldSpecFactory(),
                new FieldSpecMerger()),
            new TreePartitioner(),
            new NoopDecisionTreeOptimiser());

        final GenerationConfig config = new GenerationConfig(generationStrategy, new FieldExhaustiveCombinationStrategy());
        final Stream<GeneratedObject> dataSet = dataGenerator.generateData(profile, analysedProfile.getMergedTree(), config);
        List<GeneratedObject> allActualRows = new ArrayList<>();
        dataSet.forEach(allActualRows::add);
        return allActualRows;
    }

    public static Object parseInput(String input) {
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        } else if (input.matches(DateValueStep.DATE_REGEX)) {
            return DateValueStep.dateObject(input);
        } else if (input.equals("null")) {
            return null;
        } else if (input.matches("(-)?([0-9]+\\.[0-9]+)")) {
            return new BigDecimal(input);
        } else if (input.matches("(-)?[0-9]+")) {
            return Integer.parseInt(input);
        }

        return input;
    }

    public static Object parseExpected(String input) {
        if (input.matches(DateValueStep.DATE_REGEX)) {
            return LocalDateTime.parse(input);
        }
        return parseInput(input);
    }
}
