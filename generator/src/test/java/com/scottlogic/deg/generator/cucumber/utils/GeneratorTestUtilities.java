package com.scottlogic.deg.generator.cucumber.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.cucumber.steps.DateValueStep;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.DecisionTreeDataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.HierarchicalDependencyFixFieldStrategy;
import org.junit.Assert;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneratorTestUtilities {
    private static final ObjectMapper mapper = createMapper();

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        return mapper;
    }

    /**
     * Runs the data generator and returns list of generated result data.
     *
     * @return Generated data
     */
    static List<List<Object>> getDEGGeneratedData(
        List<Field> profileFields,
        List<Constraint> constraints,
        GenerationConfig.DataGenerationType generationStrategy,
        GenerationConfig.TreeWalkerType walkerType,
        GenerationConfig.CombinationStrategyType combinationStrategy) {
        return getGeneratedDataAsList(profileFields, constraints, generationStrategy, walkerType, combinationStrategy)
            .stream()
            .map(genObj ->{

                if (genObj == null){
                    throw new IllegalStateException("GeneratedObject is null");
                }

                return genObj.values
                    .stream()
                    .map(obj -> {
                        if (obj.value != null && obj.format != null) {
                            return String.format(obj.format, obj.value);
                        }
                        return obj.value;
                    })
                    .collect(Collectors.toList());
            }).collect(Collectors.toList());
    }

    private static List<GeneratedObject> getGeneratedDataAsList(
        List<Field> profileFields,
        List<Constraint> constraints,
        GenerationConfig.DataGenerationType generationStrategy,
        GenerationConfig.TreeWalkerType walkerType,
        GenerationConfig.CombinationStrategyType combinationStrategy) {
        Profile profile = new Profile(
            new ProfileFields(profileFields),
            Collections.singleton(new Rule("TEST_RULE", constraints)));

        final DecisionTreeCollection analysedProfile = new ProfileDecisionTreeFactory().analyse(profile);
        final GenerationConfig config = new GenerationConfig(generationStrategy, walkerType, combinationStrategy);
        FixFieldStrategy fixFieldStrategy = new HierarchicalDependencyFixFieldStrategy(profile, new FieldDependencyAnalyser());

        final DataGenerator dataGenerator = new DecisionTreeDataGenerator(
            config,
            new RelatedFieldTreePartitioner(),
            new NoopDecisionTreeOptimiser(),
            new NoopDataGeneratorMonitor(),
            new ProfileDecisionTreeFactory(),
            fixFieldStrategy);


        final Stream<GeneratedObject> dataSet = dataGenerator.generateData(profile, config);

        return dataSet.collect(Collectors.toList());
    }

    public static Object parseInput(String input) throws JsonParseException {
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        } else if (input.matches(DateValueStep.DATE_REGEX)) {
            return DateValueStep.dateObject(input);
        } else if (input.equals("null")) {
            return null;
        } else if (input.matches("-?(\\d+(\\.\\d+)?)")) {
            return parseNumber(input);
        }

        return input;
    }

    public static Object parseNumber(String input) throws JsonParseException {
        try {
            return mapper.readerFor(Number.class).readValue(input);
        }
        catch (JsonParseException e){
            throw e;
        }
        catch (IOException e) {
            Assert.fail("Unexpected IO exception " + e.toString());
            return "<unexpected IO exception>";
        }
    }

    public static Object parseExpected(String input) throws JsonParseException {
        if (input.matches(DateValueStep.DATE_REGEX)) {
            return LocalDateTime.parse(input);
        }
        return parseInput(input);
    }
}
