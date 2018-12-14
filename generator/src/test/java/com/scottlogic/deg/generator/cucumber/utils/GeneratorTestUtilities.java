package com.scottlogic.deg.generator.cucumber.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.cucumber.steps.DateValueStep;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.walker.CartesianProductDecisionTreeWalker;
import com.scottlogic.deg.schemas.v3.RuleDTO;
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
            Collections.singleton(new Rule(rule("TEST_RULE"), constraints)));

        final DecisionTreeCollection analysedProfile = new ProfileDecisionTreeFactory().analyse(profile);

        final DataGenerator dataGenerator = new DecisionTreeDataGenerator(
            new CartesianProductDecisionTreeWalker(
                new ConstraintReducer(
                    new FieldSpecFactory(),
                    new FieldSpecMerger()),
                new RowSpecMerger(
                    new FieldSpecMerger())),
            new RelatedFieldTreePartitioner(),
            new NoopDecisionTreeOptimiser(),
            new NoopDataGeneratorMonitor());

        final GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                generationStrategy,
                walkerType,
                combinationStrategy));
        final Stream<GeneratedObject> dataSet = dataGenerator.generateData(profile, analysedProfile.getMergedTree(), config);

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

    private static RuleInformation rule(String description){
        RuleDTO rule = new RuleDTO();
        rule.rule = description;
        return new RuleInformation(rule);
    }
}
