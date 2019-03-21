package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.walker.reductive.*;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class ReductiveDecisionTreeWalkerTests {
    private TreeConstraintNode rootNode;
    private DecisionTree tree;
    private ReductiveFieldSpecBuilder reductiveFieldSpecBuilder;
    private ReductiveDecisionTreeWalker walker;
    private FixFieldStrategy fixFieldStrategy;
    private FieldSpecValueGenerator fieldSpecValueGenerator;

    @BeforeEach
    public void beforeEach(){
        ProfileFields fields = new ProfileFields(Arrays.asList(new Field("field1"), new Field("field2")));
        rootNode = new TreeConstraintNode();
        tree = new DecisionTree(rootNode, fields, "");
        ReductiveTreePruner treePruner = mock(ReductiveTreePruner.class);
        when(treePruner.pruneConstraintNode(eq(rootNode), any())).thenReturn(Merged.of(rootNode)); //TODO paul why is this being mocked

        reductiveFieldSpecBuilder = mock(ReductiveFieldSpecBuilder.class);
        fieldSpecValueGenerator = mock(FieldSpecValueGenerator.class);
        fixFieldStrategy = mock(FixFieldStrategy.class);


        walker = new ReductiveDecisionTreeWalker(
            new NoOpIterationVisualiser(),
            reductiveFieldSpecBuilder,
            new NoopDataGeneratorMonitor(),
            treePruner,
            mock(ReductiveRowSpecGenerator.class),
            fieldSpecValueGenerator
        );
    }

    /**
     * If no field can be fixed initially, the walker should exit early, with an empty stream of RowSpecs
     */
    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenFirstFieldCannotBeFixed() {
        when(reductiveFieldSpecBuilder.getFieldSpecWithMustContains(eq(rootNode), any())).thenReturn(Optional.empty());

        List<RowSpec> result = walker.walk(tree, fixFieldStrategy).collect(Collectors.toList());

        verify(reductiveFieldSpecBuilder).getFieldSpecWithMustContains(eq(rootNode), any());
        Assert.assertThat(result, empty());
    }

    /**
     * If a field can be fixed initially, but subsequently another one cannot be fixed then exit as early as possible
     * with an empty stream of RowSpecs
     */
    @Test
    public void shouldReturnEmptyCollectionOfRowsWhenSecondFieldCannotBeFixed() {
        FieldSpec firstFieldSpec = mock(FieldSpec.class);
        DataBag dataBag = mock(DataBag.class);
        when(fieldSpecValueGenerator.generate(any(), firstFieldSpec)).thenReturn(Stream.of(dataBag));
        when(dataBag.getValue(any())).thenReturn("yes");

        when(reductiveFieldSpecBuilder.getFieldSpecWithMustContains(eq(rootNode), any())).thenReturn(Optional.of(firstFieldSpec), Optional.empty());

        List<RowSpec> result = walker.walk(tree, fixFieldStrategy).collect(Collectors.toList());

        verify(reductiveFieldSpecBuilder, times(2)).getFieldSpecWithMustContains(eq(rootNode), any());
        Assert.assertThat(result, empty());
    }
}