package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;
import com.scottlogic.deg.generator.walker.reductive.*;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class ReductiveDecisionTreeWalkerTests {

    /**
     * If no field can be fixed initially, the walker should exit early, with an empty stream of RowSpecs
     */
    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenFirstFieldCannotBeFixed() {
        ProfileFields fields = new ProfileFields(Arrays.asList(new Field("field1"), new Field("field2")));
        ReductiveConstraintNode rootNode = new ReductiveConstraintNode(new TreeConstraintNode(), Collections.emptySet());
        DecisionTree tree = new DecisionTree(rootNode, fields, "");
        FixedFieldBuilder fixedFieldBuilder = mock(FixedFieldBuilder.class);
        ReductiveDecisionTreeReducer treeReducer = mock(ReductiveDecisionTreeReducer.class);
        ReductiveRowSpecGenerator rowSpecGenerator = mock(ReductiveRowSpecGenerator.class);
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.RANDOM,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE));
        ReductiveDecisionTreeWalker walker = new ReductiveDecisionTreeWalker(
            new NoOpIterationVisualiser(),
            fixedFieldBuilder,
            new NoopDataGeneratorMonitor(),
            treeReducer,
            rowSpecGenerator,
            config
        );
        when(treeReducer.reduce(eq(rootNode), any(ReductiveState.class))).thenReturn(rootNode);
        when(fixedFieldBuilder.findNextFixedField(any(ReductiveState.class), eq(rootNode))).thenReturn(null);

        List<RowSpec> result = walker.walk(tree).collect(Collectors.toList());

        verify(fixedFieldBuilder).findNextFixedField(any(ReductiveState.class), eq(rootNode));
        Assert.assertThat(result, empty());
    }

    /**
     * If a field can be fixed initially, but subsequently another one cannot be fixed then exit as early as possible
     * with an empty stream of RowSpecs
     */
    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenSecondFieldCannotBeFixed() {
        ProfileFields fields = new ProfileFields(Arrays.asList(new Field("field1"), new Field("field2")));
        ReductiveConstraintNode rootNode = new ReductiveConstraintNode(new TreeConstraintNode(), Collections.emptySet());
        DecisionTree tree = new DecisionTree(rootNode, fields, "");
        FixedFieldBuilder fixedFieldBuilder = mock(FixedFieldBuilder.class);
        ReductiveDecisionTreeReducer treeReducer = mock(ReductiveDecisionTreeReducer.class);
        ReductiveRowSpecGenerator rowSpecGenerator = mock(ReductiveRowSpecGenerator.class);
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.RANDOM,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE));
        ReductiveDecisionTreeWalker walker = new ReductiveDecisionTreeWalker(
            new NoOpIterationVisualiser(),
            fixedFieldBuilder,
            new NoopDataGeneratorMonitor(),
            treeReducer,
            rowSpecGenerator,
            config
        );
        FixedField firstFixedField = fixedField("field1", 123);
        when(treeReducer.reduce(eq(rootNode), any(ReductiveState.class))).thenReturn(rootNode);
        when(fixedFieldBuilder.findNextFixedField(any(ReductiveState.class), eq(rootNode))).thenReturn(firstFixedField, null);

        List<RowSpec> result = walker.walk(tree).collect(Collectors.toList());

        verify(fixedFieldBuilder, times(2)).findNextFixedField(any(ReductiveState.class), eq(rootNode));
        Assert.assertThat(result, empty());
    }

    @Test
    public void shouldProduceTwoRowsOfRandomData() {
        ProfileFields fields = new ProfileFields(Arrays.asList(new Field("field1"), new Field("field2")));
        ReductiveConstraintNode rootNode = new ReductiveConstraintNode(new TreeConstraintNode(), Collections.emptySet());
        DecisionTree tree = new DecisionTree(rootNode, fields, "");
        FixedFieldBuilder fixedFieldBuilder = mock(FixedFieldBuilder.class);
        ReductiveDecisionTreeReducer treeReducer = mock(ReductiveDecisionTreeReducer.class);
        ReductiveRowSpecGenerator rowSpecGenerator = mock(ReductiveRowSpecGenerator.class);
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.RANDOM,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE));
        ReductiveDecisionTreeWalker walker = new ReductiveDecisionTreeWalker(
            new NoOpIterationVisualiser(),
            fixedFieldBuilder,
            new NoopDataGeneratorMonitor(),
            treeReducer,
            rowSpecGenerator,
            config
        );
        when(treeReducer.reduce(eq(rootNode), any(ReductiveState.class))).thenReturn(rootNode);
        FixedField fixedField1_1 = fixedField("field1", 1, 2, 3);
        FixedField fixedField2_1 = fixedField("field2", 4, 5, 6);
        FixedField fixedField1_2 = fixedField("field1", 10, 20, 30);
        FixedField fixedField2_2 = fixedField("field2", 40, 50, 60);
        when(fixedFieldBuilder.findNextFixedField(any(ReductiveState.class), eq(rootNode)))
            .thenReturn(
                fixedField1_1, fixedField2_1,
                fixedField1_2, fixedField2_2);
        when(rowSpecGenerator.createRowSpecsFromFixedValues(
            argThat(new ReductiveStateMatcher("field1", 1, "field2", 4)), any(ConstraintNode.class)))
            .thenReturn(
                Stream.of(rowSpec("first-row-1&4")));
        when(rowSpecGenerator.createRowSpecsFromFixedValues(
            argThat(new ReductiveStateMatcher("field1", 10, "field2", 40)), any(ConstraintNode.class)))
            .thenReturn(
                Stream.of(rowSpec("second-row-10&40")));

        List<RowSpec> result = walker.walk(tree).limit(2).collect(Collectors.toList());

        verify(rowSpecGenerator, times(2))
            .createRowSpecsFromFixedValues(any(ReductiveState.class), any(ConstraintNode.class));
        Assert.assertThat(
            result.stream().map(RowSpec::toString).collect(Collectors.toList()),
            hasItems("first-row-1&4", "second-row-10&40"));
    }

    @Test
    public void shouldProduceTwoRowsOfNormalData() {
        ProfileFields fields = new ProfileFields(Arrays.asList(new Field("field1"), new Field("field2")));
        ReductiveConstraintNode rootNode = new ReductiveConstraintNode(new TreeConstraintNode(), Collections.emptySet());
        DecisionTree tree = new DecisionTree(rootNode, fields, "");
        FixedFieldBuilder fixedFieldBuilder = mock(FixedFieldBuilder.class);
        ReductiveDecisionTreeReducer treeReducer = mock(ReductiveDecisionTreeReducer.class);
        ReductiveRowSpecGenerator rowSpecGenerator = mock(ReductiveRowSpecGenerator.class);
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.FULL_SEQUENTIAL,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE));
        ReductiveDecisionTreeWalker walker = new ReductiveDecisionTreeWalker(
            new NoOpIterationVisualiser(),
            fixedFieldBuilder,
            new NoopDataGeneratorMonitor(),
            treeReducer,
            rowSpecGenerator,
            config
        );
        FixedField fixedField1 = fixedField("field1", 1, 2, 3);
        FixedField fixedField2 = fixedField("field2", 7, 8, 9);
        when(treeReducer.reduce(eq(rootNode), any(ReductiveState.class))).thenReturn(rootNode);
        when(rowSpecGenerator.createRowSpecsFromFixedValues(
            argThat(new ReductiveStateMatcher("field1", 1, "field2")), any(ConstraintNode.class)))
            .thenReturn(
                Stream.of(rowSpec("first-row-1&7"), rowSpec("first-row-1&8"), rowSpec("first-row-1&9")));
        when(fixedFieldBuilder.findNextFixedField(any(ReductiveState.class), eq(rootNode)))
            .thenReturn(
                fixedField1,
                fixedField2);

        List<RowSpec> result = walker.walk(tree).limit(2).collect(Collectors.toList());

        verify(rowSpecGenerator, times(1))
            .createRowSpecsFromFixedValues(any(ReductiveState.class), any(ConstraintNode.class));
        Assert.assertThat(
            result.stream().map(RowSpec::toString).collect(Collectors.toList()),
            hasItems("first-row-1&7", "first-row-1&8"));
    }

    private class ReductiveStateMatcher extends BaseMatcher<ReductiveState> {

        private final Field field;
        private final Object expectedValue;
        private final Field lastFixedField;
        private final Object exectedLastFieldValue;
        private final boolean ignoreLastFixedFieldValue;

        ReductiveStateMatcher(String field, Object expectedValue, String lastFixedField) {
            this(field, expectedValue, lastFixedField, null, true);
        }

        ReductiveStateMatcher(String field, Object expectedValue, String lastFixedField, Object exectedLastFieldValue) {
            this(field, expectedValue, lastFixedField, exectedLastFieldValue, false);
        }

        private ReductiveStateMatcher(String field, Object expectedValue, String lastFixedField, Object exectedLastFieldValue, boolean ignoreLastFixedFieldValue) {
            this.field = new Field(field);
            this.expectedValue = expectedValue;
            this.lastFixedField = lastFixedField == null ? null : new Field(lastFixedField);
            this.exectedLastFieldValue = lastFixedField == null ? null : exectedLastFieldValue;
            this.ignoreLastFixedFieldValue = ignoreLastFixedFieldValue;
        }

        @Override
        public boolean matches(Object o) {
            if (o == null) {
                return false;
            }

            ReductiveState state = (ReductiveState) o;
            FixedField lastFixedField = state.getLastFixedField();

            if (!state.isFieldFixed(field)) {
                return false;
            }

            boolean previouslyFixedFieldMatches = hasValue(state.getFixedField(field), expectedValue);
            if (this.lastFixedField == null || !previouslyFixedFieldMatches) {
                return previouslyFixedFieldMatches;
            }

            boolean lastFixedFieldNameMatches = this.lastFixedField == null || lastFixedField.getField().equals(this.lastFixedField);

            return lastFixedFieldNameMatches
                && (ignoreLastFixedFieldValue || hasValue(lastFixedField, exectedLastFieldValue));
        }

        private boolean hasValue(FixedField field, Object expectedValue) {
            Set<Object> whitelist = field.getFieldSpecForValues().getSetRestrictions().getWhitelist();
            if (!whitelist.contains(expectedValue)) {
                return false;
            }

            if (field.hasCurrentValue()) {
                return field.getCurrentValue() == expectedValue;
            }

            Optional<Object> firstValue = field.getStream().findFirst();
            return expectedValue.equals(firstValue.orElse(null));
        }

        @Override
        public void describeTo(Description description) {

        }
    }

    private static FixedField fixedField(String fieldName, Object... values) {
        FieldSpec valuesFieldSpec = FieldSpec.Empty.withSetRestrictions(
            SetRestrictions.fromWhitelist(new HashSet<>(Arrays.asList(values))),
            FieldSpecSource.Empty
        );

        FixedField mockFixedField = mock(FixedField.class);

        AtomicBoolean hasStartedReading = new AtomicBoolean();
        AtomicCurrentValue currentValue = new AtomicCurrentValue();

        when(mockFixedField.getField()).thenReturn(new Field(fieldName));
        when(mockFixedField.getFieldSpecForValues()).thenReturn(valuesFieldSpec);
        when(mockFixedField.hasCurrentValue()).thenAnswer(__ -> hasStartedReading.get());
        when(mockFixedField.getCurrentValue()).thenAnswer(__ -> currentValue.value);

        when(mockFixedField.getStream()).thenReturn(Stream.of(values).peek(v -> {
            currentValue.setValue(v);
            hasStartedReading.set(true);
        }));

        return mockFixedField;
    }

    private static RowSpec rowSpec(String detail) {
        return mock(RowSpec.class, detail);
    }

    private static class AtomicCurrentValue {
        public Object value = "NOT_ITERATED";

        public void setValue(Object value) {
            this.value = value;
        }
    }
}