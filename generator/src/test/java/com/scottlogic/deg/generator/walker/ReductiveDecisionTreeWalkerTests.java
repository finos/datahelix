package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.walker.reductive.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class ReductiveDecisionTreeWalkerTests {
    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenFirstFieldCannotBeFixed(){
        ProfileFields fields = new ProfileFields(Arrays.asList(new Field("field1"), new Field("field2")));
        ReductiveConstraintNode rootNode = new ReductiveConstraintNode(new TreeConstraintNode(), Collections.emptySet());
        DecisionTree tree = new DecisionTree(rootNode, fields, "");
        FixedFieldBuilder fixedFieldBuilder = mock(FixedFieldBuilder.class);
        ReductiveDecisionTreeReducer treeReducer = mock(ReductiveDecisionTreeReducer.class);
        ReductiveRowSpecGenerator rowSpecGenerator = mock(ReductiveRowSpecGenerator.class);
        ReductiveDecisionTreeWalker walker = new ReductiveDecisionTreeWalker(
            new NoOpIterationVisualiser(),
            fixedFieldBuilder,
            new NoopDataGeneratorMonitor(),
            treeReducer,
            rowSpecGenerator
        );
        when(treeReducer.reduce(eq(rootNode), any(ReductiveState.class))).thenReturn(rootNode);
        when(fixedFieldBuilder.findNextFixedField(any(ReductiveState.class), eq(rootNode))).thenReturn(null);

        List<RowSpec> result = walker.walk(tree).collect(Collectors.toList());

        verify(fixedFieldBuilder).findNextFixedField(any(ReductiveState.class), eq(rootNode));
        Assert.assertThat(result, empty());
    }

    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenSecondFieldCannotBeFixed(){
        ProfileFields fields = new ProfileFields(Arrays.asList(new Field("field1"), new Field("field2")));
        ReductiveConstraintNode rootNode = new ReductiveConstraintNode(new TreeConstraintNode(), Collections.emptySet());
        DecisionTree tree = new DecisionTree(rootNode, fields, "");
        FixedFieldBuilder fixedFieldBuilder = mock(FixedFieldBuilder.class);
        ReductiveDecisionTreeReducer treeReducer = mock(ReductiveDecisionTreeReducer.class);
        ReductiveRowSpecGenerator rowSpecGenerator = mock(ReductiveRowSpecGenerator.class);
        ReductiveDecisionTreeWalker walker = new ReductiveDecisionTreeWalker(
            new NoOpIterationVisualiser(),
            fixedFieldBuilder,
            new NoopDataGeneratorMonitor(),
            treeReducer,
            rowSpecGenerator
        );
        FixedField firstFixedField = fixedField(123);
        when(treeReducer.reduce(eq(rootNode), any(ReductiveState.class))).thenReturn(rootNode);
        when(fixedFieldBuilder.findNextFixedField(any(ReductiveState.class), eq(rootNode))).thenReturn(firstFixedField, null);

        List<RowSpec> result = walker.walk(tree).collect(Collectors.toList());

        verify(fixedFieldBuilder, times(2)).findNextFixedField(any(ReductiveState.class), eq(rootNode));
        Assert.assertThat(result, empty());
    }

    private static FixedField fixedField(Object... values){
        FixedField mockFixedField = mock(FixedField.class);
        when(mockFixedField.getStream()).thenReturn(Stream.of(values));
        //when(mockFixedField.field).thenReturn(new Field(fieldName)); //mockito cannot mock member variables (fields).
        when(mockFixedField.getFieldSpecForValues()).thenReturn(FieldSpec.Empty);
        when(mockFixedField.getCurrentValue()).thenReturn(123);

        return mockFixedField;
    }
}