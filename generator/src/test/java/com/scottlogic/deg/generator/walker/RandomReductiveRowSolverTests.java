package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.generation.rows.Row;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

class RandomReductiveRowSolverTests {
    private DecisionTree tree;
    private RestartingRowSolver walker;
    private ReductiveRowSolver underlyingWalker;
    Profile profile = mock(Profile.class);

    @BeforeEach
    public void beforeEach(){
        tree = new DecisionTree(
            new TreeConstraintNode(),
            new ProfileFields(Arrays.asList(new Field("field1"), new Field("field2"))),
            "test-tree");

        underlyingWalker = mock(ReductiveRowSolver.class);
        walker = new RestartingRowSolver(underlyingWalker);
    }

    /**
     * If RANDOM mode is enabled there should be two rows of data where field1 & field2 have (synthetically) random
     * values for each row
     */
    @Test
    public void shouldProduceTwoRowsOfRandomDataOneRowFromEachIteration() {
        when(underlyingWalker.generateRows(profile, tree)).thenReturn(
            Stream.of(rowSpec("first-iteration-first-random-row"), rowSpec("first-iteration-second-random-row")),
            Stream.of(rowSpec("second-iteration-first-random-row"), rowSpec("second-iteration-second-random-row"))
        );

        List<Row> result = walker.generateRows(profile, tree).limit(2).collect(Collectors.toList());

        verify(underlyingWalker, times(2)).generateRows(profile, tree);
        Assert.assertThat(
            result.stream().map(Row::toString).collect(Collectors.toList()),
            hasItems("first-iteration-first-random-row", "second-iteration-first-random-row"));
    }

    @Test
    public void shouldProduceNoData() {
        when(underlyingWalker.generateRows(profile, tree)).thenReturn(
            Stream.of(rowSpec("first-iteration-first-random-row"), rowSpec("first-iteration-second-random-row")),
            Stream.empty(),
            Stream.of(rowSpec("third-iteration-first-random-row"), rowSpec("third-iteration-second-random-row"))
        );

        List<Row> result = walker.generateRows(profile, tree).limit(2).collect(Collectors.toList());

        verify(underlyingWalker, times(3)).generateRows(profile, tree);
        Assert.assertThat(
            result.stream().map(Row::toString).collect(Collectors.toList()),
            hasItems("first-iteration-first-random-row", "third-iteration-first-random-row"));
    }

    @Test
    public void shouldAccommodateNoDataInSubsequentIteration() {
        when(underlyingWalker.generateRows(profile, tree)).thenReturn(
            Stream.empty()
        );

        List<Row> result = walker.generateRows(profile, tree).limit(2).collect(Collectors.toList());

        verify(underlyingWalker, times(1)).generateRows(profile, tree);
        Assert.assertThat(
            result.stream().iterator().hasNext(),
            is(false));
    }

    private static Row rowSpec(String detail) {
        return mock(Row.class, detail);
    }
}