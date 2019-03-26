package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.walker.reductive.*;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class ReductiveDecisionTreeWalkerTests {
    private TreeConstraintNode rootNode;
    private DecisionTree tree;
    private FixedFieldBuilder fixedFieldBuilder;
    private ReductiveDecisionTreeWalker walker;
    private FixFieldStrategy fixFieldStrategy;

    @BeforeEach
    public void beforeEach(){
        ProfileFields fields = new ProfileFields(Arrays.asList(new Field("field1"), new Field("field2")));
        rootNode = new TreeConstraintNode();
        tree = new DecisionTree(rootNode, fields, "");
        ReductiveTreePruner treeReducer = mock(ReductiveTreePruner.class);
        when(treeReducer.pruneConstraintNode(eq(rootNode), any(FixedField.class))).thenReturn(Merged.of(rootNode));

        fixedFieldBuilder = mock(FixedFieldBuilder.class);
        fixFieldStrategy = mock(FixFieldStrategy.class);

        walker = new ReductiveDecisionTreeWalker(
            new NoOpIterationVisualiser(),
            fixedFieldBuilder,
            new NoopDataGeneratorMonitor(),
            treeReducer,
            mock(ReductiveRowSpecGenerator.class)
        );
    }

    /**
     * If no field can be fixed initially, the walker should exit early, with an empty stream of RowSpecs
     */
    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenFirstFieldCannotBeFixed() {
        when(fixedFieldBuilder.findNextFixedField(any(ReductiveState.class), eq(rootNode), eq(fixFieldStrategy))).thenReturn(null);

        List<RowSpec> result = walker.walk(tree, fixFieldStrategy).collect(Collectors.toList());

        verify(fixedFieldBuilder).findNextFixedField(any(ReductiveState.class), eq(rootNode), eq(fixFieldStrategy));
        Assert.assertThat(result, empty());
    }

    /**
     * If a field can be fixed initially, but subsequently another one cannot be fixed then exit as early as possible
     * with an empty stream of RowSpecs
     */
    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenSecondFieldCannotBeFixed() {
        FixedField firstFixedField = fixedField("field1", 123);
        when(fixedFieldBuilder.findNextFixedField(any(ReductiveState.class), eq(rootNode), eq(fixFieldStrategy))).thenReturn(firstFixedField, (FixedField)null);

        List<RowSpec> result = walker.walk(tree, fixFieldStrategy).collect(Collectors.toList());

        verify(fixedFieldBuilder, times(2)).findNextFixedField(any(ReductiveState.class), eq(rootNode), eq(fixFieldStrategy));
        Assert.assertThat(result, empty());
    }

    private static FixedField fixedField(String fieldName, Object... values) {
        FixedField mockFixedField = mock(FixedField.class, fieldName);

        when(mockFixedField.getField()).thenReturn(new Field(fieldName));
        when(mockFixedField.getStream()).thenReturn(Stream.of(values));

        return mockFixedField;
    }
}