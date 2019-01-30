package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.*;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagSourceFactory;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.Nullness;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReductiveRowSpecGeneratorTests {
    @Test
    void getValuesFromLastFixedField_lastFixedFieldNull_throwsIllegalStateException() {
        ReductiveRowSpecGenerator rowSpecGenerator = getRowSpecGenerator();
        ReductiveState state = getReductiveState(
            Arrays.asList("First Field", "Second Field")
        );
        ConstraintNode rootNode = new TreeConstraintNode(Collections.emptySet(), Collections.emptySet());

        Assertions.assertThrows(
            IllegalStateException.class,
            () -> rowSpecGenerator.createRowSpecsFromFixedValues(state, rootNode));
    }

    @Test
    void getValuesFromLastFixedField_lastFixedFieldNotNull_returnsStreamOfAllValuesInLastFixedField() {
        List<Integer> expectedValues = Arrays.asList(1, 2, 3, 4, 5);
        ReductiveRowSpecGenerator rowSpecGenerator = getRowSpecGenerator();
        ConstraintNode rootNode = new TreeConstraintNode(Collections.emptySet(), Collections.emptySet());
        FieldSpecSource fieldSpecSource = FieldSpecSource.Empty;
        ReductiveState state = getReductiveState(
            Arrays.asList("First Field", "Second Field"),
            new FixedField(
                new Field("First Field"),
                Stream.empty(),
                FieldSpec.Empty
                    .withSetRestrictions(SetRestrictions.fromWhitelist(new HashSet<>(expectedValues)), fieldSpecSource)
                    .withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL), fieldSpecSource),
                mock(ReductiveDataGeneratorMonitor.class)));
        GenerationConfig config = new GenerationConfig(new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.INTERESTING,
            GenerationConfig.TreeWalkerType.REDUCTIVE,
            GenerationConfig.CombinationStrategyType.EXHAUSTIVE
        ));
        RowSpecDataBagSourceFactory dataBagSourceFactory = new RowSpecDataBagSourceFactory(
            new FieldSpecValueGenerator(
                config,
                new StandardFieldValueSourceEvaluator()));

        Stream<RowSpec> result = rowSpecGenerator.createRowSpecsFromFixedValues(state, rootNode);
        final Stream<List<Object>> fixed = result
            .map(o ->
                dataBagSourceFactory.createDataBagSource(o).generate(config)
                    .map(dataBag -> dataBag.getValue(new Field("First Field")))
                .collect(Collectors.toList()));
        final Stream<Object> actualStream = fixed.flatMap(Collection::stream);

        Assert.assertEquals(expectedValues, actualStream.collect(Collectors.toList()));
    }

    @Test
    void createRowSpecFromFixedValues_lastFixedFieldNull_throwsIllegalStateException() {
        ReductiveState reductiveState = getReductiveState(
            Arrays.asList("First Field", "Second Field")
        );
        ReductiveRowSpecGenerator rowSpecGenerator = getRowSpecGenerator();
        ConstraintNode rootNode = new TreeConstraintNode(Collections.emptySet(), Collections.emptySet());

        Assertions.assertThrows(
            IllegalStateException.class,
            () -> rowSpecGenerator.createRowSpecsFromFixedValues(reductiveState, rootNode));
    }

    @Test
    void createRowSpecFromFixedValues_reducerReturnsEmptyOptionalFieldSpec_returnsEmptyStream() {
        ConstraintReducer reducer = mock(ConstraintReducer.class);
        when(reducer.reduceConstraintsToFieldSpec(Matchers.<Collection<AtomicConstraint>>any()))
            .thenReturn(Optional.empty());

        HashMap<Field, FixedField> fixedFields = new HashMap<>();

        //noinspection ConstantConditions,ConfusingArgumentToVarargsMethod
        Stream<Object> fixedFieldValues = Stream.of((Object)null);

        FixedField fixedField = new FixedField(
            new Field("First Field"),
            fixedFieldValues,
            FieldSpec.Empty,
            mock(ReductiveDataGeneratorMonitor.class)
        );
        fixedFields.put(
            new Field("First Field"),
            fixedField
        );
        ReductiveRowSpecGenerator rowSpecGenerator = getRowSpecGenerator(reducer);
        ReductiveState reductiveState = getReductiveState(
            Arrays.asList("First Field", "Second Field"),
            fixedFields,
            new FixedField(
                new Field("Test"),
                Stream.of(Collections.emptyList()),
                null,
                mock(ReductiveDataGeneratorMonitor.class)
            )
        );

        //noinspection ResultOfMethodCallIgnored
        fixedField.getStream().collect(Collectors.toList());
        Stream<RowSpec> result = rowSpecGenerator.createRowSpecsFromFixedValues(reductiveState,
            new ReductiveConstraintNode(
                new TreeConstraintNode(Collections.emptyList(), Collections.emptyList()),
                null
            )
        );

        Assert.assertEquals(result.collect(Collectors.toList()), Collections.emptyList());
    }

    private ReductiveRowSpecGenerator getRowSpecGenerator(
        ConstraintReducer reducer) {
        FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();

        if (reducer == null){
            reducer = new ConstraintReducer(new FieldSpecFactory(), fieldSpecMerger);
        }
        return new ReductiveRowSpecGenerator(
            reducer,
            fieldSpecMerger,
            mock(ReductiveDataGeneratorMonitor.class)
        );
    }

    private ReductiveRowSpecGenerator getRowSpecGenerator() {
        return getRowSpecGenerator(
            null
        );
    }

    private ReductiveState getReductiveState(List<String> profileFields) {
        return new ReductiveState(
            new ProfileFields(profileFields
                .stream()
                .map(Field::new)
                .collect(Collectors.toList())));
    }

    private ReductiveState getReductiveState(List<String> profileFields, FixedField lastFixedField) {
        return getReductiveState(profileFields, new HashMap<>(), lastFixedField);
    }

    private ReductiveState getReductiveState(List<String> profileFields, HashMap<Field, FixedField> fixedFields, FixedField lastFixedField) {
        ReductiveState initialState = getReductiveState(profileFields);

        ReductiveState finalState = fixedFields.values().stream().reduce(
            initialState,
            ReductiveState::with,
            (a, b) -> null);

        if (lastFixedField == null){
            return finalState;
        }

        return finalState.with(lastFixedField);
    }
}
