package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;
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

class RandomReductiveDataGeneratorTests {
    private DecisionTree tree;
    private GeneratorRestarter walker;
    private ReductiveDataGenerator underlyingWalker;
    Profile profile = mock(Profile.class);

    @BeforeEach
    public void beforeEach(){
        tree = new DecisionTree(
            new TreeConstraintNode(),
            new ProfileFields(Arrays.asList(new Field("field1"), new Field("field2"))),
            "test-tree");

        underlyingWalker = mock(ReductiveDataGenerator.class);
        walker = new GeneratorRestarter();
    }

    /**
     * If RANDOM mode is enabled there should be two rows of data where field1 & field2 have (synthetically) random
     * values for each row
     */
    @Test
    public void shouldProduceTwoRowsOfRandomDataOneGeneratedObjectFromEachIteration() {
        when(underlyingWalker.generateData(profile, tree)).thenReturn(
            Stream.of(rowSpec("first-iteration-first-random-row"), rowSpec("first-iteration-second-random-row")),
            Stream.of(rowSpec("second-iteration-first-random-row"), rowSpec("second-iteration-second-random-row"))
        );

        List<GeneratedObject> result = walker.generateAndRestart(profile, tree,(p, t)->underlyingWalker.generateData(p, t)).collect(Collectors.toList());

        verify(underlyingWalker, times(2)).generateData(profile, tree);
        Assert.assertThat(
            result.stream().map(GeneratedObject::toString).collect(Collectors.toList()),
            hasItems("first-iteration-first-random-row", "second-iteration-first-random-row"));
    }

    @Test
    public void shouldProduceNoData() {
        when(underlyingWalker.generateData(profile, tree)).thenReturn(
            Stream.of(rowSpec("first-iteration-first-random-row"), rowSpec("first-iteration-second-random-row")),
            Stream.empty(),
            Stream.of(rowSpec("third-iteration-first-random-row"), rowSpec("third-iteration-second-random-row"))
        );

        List<GeneratedObject> result = walker.generateAndRestart(profile, tree,(p, t)->underlyingWalker.generateData(p, t)).collect(Collectors.toList());

        verify(underlyingWalker, times(3)).generateData(profile, tree);
        Assert.assertThat(
            result.stream().map(GeneratedObject::toString).collect(Collectors.toList()),
            hasItems("first-iteration-first-random-row", "third-iteration-first-random-row"));
    }

    @Test
    public void shouldAccommodateNoDataInSubsequentIteration() {
        when(underlyingWalker.generateData(profile, tree)).thenReturn(
            Stream.empty()
        );

        List<GeneratedObject> result = walker.generateAndRestart(profile, tree,(p, t)->underlyingWalker.generateData(p, t)).collect(Collectors.toList());

        verify(underlyingWalker, times(1)).generateData(profile, tree);
        Assert.assertThat(
            result.stream().iterator().hasNext(),
            is(false));
    }

    private static GeneratedObject rowSpec(String detail) {
        return mock(GeneratedObject.class, detail);
    }
}