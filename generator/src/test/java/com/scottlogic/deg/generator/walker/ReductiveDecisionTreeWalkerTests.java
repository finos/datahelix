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
import com.scottlogic.deg.generator.generation.TestGenerationConfig;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;
import com.scottlogic.deg.generator.walker.reductive.*;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
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
    private ReductiveConstraintNode rootNode;
    private DecisionTree tree;
    private FixedFieldBuilder fixedFieldBuilder;
    private ReductiveRowSpecGenerator rowSpecGenerator;
    private TestGenerationConfig config;
    private ReductiveDecisionTreeWalker walker;

    @BeforeEach
    public void beforeEach(){
        ProfileFields fields = new ProfileFields(Arrays.asList(new Field("field1"), new Field("field2")));
        rootNode = new ReductiveConstraintNode(new TreeConstraintNode(), Collections.emptySet());
        tree = new DecisionTree(rootNode, fields, "");
        ReductiveDecisionTreeReducer treeReducer = mock(ReductiveDecisionTreeReducer.class);
        when(treeReducer.reduce(eq(rootNode), any(ReductiveState.class))).thenReturn(rootNode);

        fixedFieldBuilder = mock(FixedFieldBuilder.class);
        rowSpecGenerator = mock(ReductiveRowSpecGenerator.class);

        config = new TestGenerationConfig();

        walker = new ReductiveDecisionTreeWalker(
            new NoOpIterationVisualiser(),
            fixedFieldBuilder,
            new NoopDataGeneratorMonitor(),
            treeReducer,
            rowSpecGenerator
        );
    }

    /**
     * If no field can be fixed initially, the walker should exit early, with an empty stream of RowSpecs
     */
    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenFirstFieldCannotBeFixed() {
        config.dataGenerationType = GenerationConfig.DataGenerationType.RANDOM;
        when(fixedFieldBuilder.findNextFixedField(any(ReductiveState.class), eq(rootNode))).thenReturn(null);

        List<RowSpec> result = walker.walk(tree, config).collect(Collectors.toList());

        verify(fixedFieldBuilder).findNextFixedField(any(ReductiveState.class), eq(rootNode));
        Assert.assertThat(result, empty());
    }

    /**
     * If a field can be fixed initially, but subsequently another one cannot be fixed then exit as early as possible
     * with an empty stream of RowSpecs
     */
    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenSecondFieldCannotBeFixed() {
        config.dataGenerationType = GenerationConfig.DataGenerationType.RANDOM;
        FixedField firstFixedField = fixedField("field1", 123);
        when(fixedFieldBuilder.findNextFixedField(any(ReductiveState.class), eq(rootNode))).thenReturn(firstFixedField, null);

        List<RowSpec> result = walker.walk(tree, config).collect(Collectors.toList());

        verify(fixedFieldBuilder, times(2)).findNextFixedField(any(ReductiveState.class), eq(rootNode));
        Assert.assertThat(result, empty());
    }

    /**
     * If RANDOM mode is enabled there should be two rows of data where field1 & field2 have (synthetically) random
     * values for each row
     */
    @Test
    public void shouldProduceTwoRowsOfRandomData() {
        config.dataGenerationType = GenerationConfig.DataGenerationType.RANDOM;
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

        List<RowSpec> result = walker.walk(tree, config).limit(2).collect(Collectors.toList());

        verify(rowSpecGenerator, times(2))
            .createRowSpecsFromFixedValues(any(ReductiveState.class), any(ConstraintNode.class));
        Assert.assertThat(
            result.stream().map(RowSpec::toString).collect(Collectors.toList()),
            hasItems("first-row-1&4", "second-row-10&40"));
    }

    /**
     * If NOT in RANDOM mode each row should be produced based on the exhaustive combination of
     * field1[first-value] * field2[all-values]
     */
    @Test
    public void shouldProduceTwoRowsOfNormalData() {
        config.dataGenerationType = GenerationConfig.DataGenerationType.FULL_SEQUENTIAL;
        FixedField fixedField1 = fixedField("field1", 1, 2, 3);
        FixedField fixedField2 = fixedField("field2", 7, 8, 9);
        when(rowSpecGenerator.createRowSpecsFromFixedValues(
            argThat(new ReductiveStateMatcher("field1", 1, "field2")), any(ConstraintNode.class)))
            .thenReturn(
                Stream.of(rowSpec("first-row-1&7"), rowSpec("first-row-1&8"), rowSpec("first-row-1&9")));
        when(fixedFieldBuilder.findNextFixedField(any(ReductiveState.class), eq(rootNode)))
            .thenReturn(
                fixedField1,
                fixedField2);

        List<RowSpec> result = walker.walk(tree, config).limit(2).collect(Collectors.toList());

        verify(rowSpecGenerator, times(1))
            .createRowSpecsFromFixedValues(any(ReductiveState.class), any(ConstraintNode.class));
        Assert.assertThat(
            result.stream().map(RowSpec::toString).collect(Collectors.toList()),
            hasItems("first-row-1&7", "first-row-1&8"));
    }

    /**
     * If NOT in RANDOM mode, processing should stop once all data has been emitted
     */
    @Test
    public void shouldStopOnceAllDataProduced() {
        LinkedList<Stream<RowSpec>> rowSpecsToEmit = new LinkedList<>(
            Arrays.asList(Stream.of(rowSpec("first-row-1&7")),
            Stream.of(rowSpec("shouldn't get here")),
            Stream.of(rowSpec("shouldn't get here"))));

        config.dataGenerationType = GenerationConfig.DataGenerationType.FULL_SEQUENTIAL;
        when(rowSpecGenerator.createRowSpecsFromFixedValues(
            argThat(new ReductiveStateMatcher("field1", 1, "field2")), any(ConstraintNode.class)))
            .thenAnswer(__ -> rowSpecsToEmit.poll());
        when(fixedFieldBuilder.findNextFixedField(argThat(new ReductiveStateMatcher()), eq(rootNode)))
            .thenAnswer(__ -> fixedField("field1", 1));
        when(fixedFieldBuilder.findNextFixedField(argThat(new ReductiveStateMatcher("field1", 1)), eq(rootNode)))
            .thenAnswer(__ -> fixedField("field2", 7));

        List<RowSpec> result = walker.walk(tree, config).limit(3).collect(Collectors.toList());

        verify(rowSpecGenerator, times(1))
            .createRowSpecsFromFixedValues(any(ReductiveState.class), any(ConstraintNode.class));
        verify(fixedFieldBuilder, times(1))
            .findNextFixedField(argThat(new ReductiveStateMatcher()), eq(rootNode));
        Assert.assertThat(
            result.stream().map(RowSpec::toString).collect(Collectors.toList()),
            hasItems("first-row-1&7"));
    }

    /**
     * A matcher for the ReductiveState, check to see if the ReductiveState matches the given details
     * when a method is invoked with it as a parameter
     */
    private class ReductiveStateMatcher extends BaseMatcher<ReductiveState> {
        private final Field field;
        private final Object expectedValue;
        private final Field lastFixedField;
        private final Object exectedLastFieldValue;
        private final boolean ignoreLastFixedFieldValue;

        public ReductiveStateMatcher() {
            this(null, null, null, null, true);
        }

        ReductiveStateMatcher(String field, Object expectedValue) {
            this(field, expectedValue, null, null, true);
        }

        ReductiveStateMatcher(String field, Object expectedValue, String lastFixedField) {
            this(field, expectedValue, lastFixedField, null, true);
        }

        ReductiveStateMatcher(String field, Object expectedValue, String lastFixedField, Object exectedLastFieldValue) {
            this(field, expectedValue, lastFixedField, exectedLastFieldValue, false);
        }

        private ReductiveStateMatcher(String field, Object expectedValue, String lastFixedField, Object exectedLastFieldValue, boolean ignoreLastFixedFieldValue) {
            this.field = field == null ? null : new Field(field);
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
            if (field == null && lastFixedField == null){
                return state.getLastFixedField() == null
                    && state.getFixedFieldsExceptLast().isEmpty();
            }

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

            Object currentValue = field.getCurrentValue();
            if (currentValue == AtomicCurrentValue.NOT_ITERATED){
                currentValue = field.getStream().findFirst().orElseThrow(() -> new IllegalStateException("No values found in stream"));
            }

            return currentValue.equals(expectedValue);
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

        FixedField mockFixedField = mock(FixedField.class, fieldName);

        AtomicBoolean hasStartedReading = new AtomicBoolean();
        AtomicCurrentValue currentValue = new AtomicCurrentValue();

        when(mockFixedField.getField()).thenReturn(new Field(fieldName));
        when(mockFixedField.getFieldSpecForValues()).thenReturn(valuesFieldSpec);
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
        public static final Object NOT_ITERATED = "NOT_ITERATED";
        public Object value = NOT_ITERATED;

        public void setValue(Object value) {
            this.value = value;
        }
    }
}