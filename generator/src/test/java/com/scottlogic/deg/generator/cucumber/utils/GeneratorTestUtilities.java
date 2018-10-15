package com.scottlogic.deg.generator.cucumber.utils;

import com.scottlogic.deg.generator.*;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.cucumber.steps.DateValueStep;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.generation.combination_strategies.FieldExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GeneratorTestUtilities {

    /**
     * Runs the data generator and returns list of generated result data.
     * @return Generated data
     */
    static List <List<String>> getDEGGeneratedData(List<Field> profileFields, List<IConstraint> constraints, GenerationConfig.DataGenerationType generationStrategy) {
        return getGeneratedDataAsList(profileFields, constraints, generationStrategy)
            .stream()
            .map(genObj ->
                genObj.values
                    .stream()
                    .map(GeneratorTestUtilities::getDataBagAsString)
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
                new FieldSpecMerger()));

        final GenerationConfig config = new GenerationConfig(generationStrategy, new FieldExhaustiveCombinationStrategy());
        final Iterable<GeneratedObject> dataSet = dataGenerator.generateData(profile, analysedProfile.getMergedTree(), config);
        List<GeneratedObject> allActualRows = new ArrayList<>();
        dataSet.iterator().forEachRemaining(allActualRows::add);
        return allActualRows;
    }

    private static String getDataBagAsString(DataBagValue x){
        if (x.value == null)
            return "null";

        if (x.format == null)
            return x.value.toString();

        return String.format(x.format, x.value);
    }

    public static Object parseInput(String input) {
        Object parsedValue;
        if (input.startsWith("\"") && input.endsWith("\"")) {
            parsedValue = input.substring(1, input.length() - 1);
        } else if (input.matches(DateValueStep.DATE_REGEX)){
            parsedValue = input;
        } else if (input.contains(".")){
            parsedValue = Double.parseDouble(input);
        } else {
            parsedValue = Integer.parseInt(input);
        }
        return parsedValue;
    }

}
