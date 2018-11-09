package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
class DecisionIteratorTest {

    RowSpecRoute route;

    @Test
    void oneOption_hasNext() {
        DecisionNode node = IteratorTestHelper.singleDecision();
        DecisionIterator iterator = DecisionIterator.build(Arrays.asList(node));

        assertThat(iterator.hasNext(), is(true));
    }

    @Test
    void oneOption_next_then_hasNextIsFalse() {
        DecisionNode node = IteratorTestHelper.singleDecision();
        DecisionIterator iterator = DecisionIterator.build(Arrays.asList(node));

        RowSpecRoute route = iterator.next().get(0);
        assertThat(route.decisionOptionIndex, is(0));
        assertThat(route.subRoutes,  is(emptyArray()));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void twoOptions_hasNext() {
        DecisionNode node = IteratorTestHelper.doubleDecision();
        DecisionIterator iterator = DecisionIterator.build(Arrays.asList(node));

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute route = iterator.next().get(0);
        assertThat(route.decisionOptionIndex, is(0));
        assertThat(route.subRoutes,  is(emptyArray()));

        assertThat(iterator.hasNext(), is(true));
        route = iterator.next().get(0);
        assertThat(route.decisionOptionIndex, is(1));
        assertThat(route.subRoutes,  is(emptyArray()));

        assertThat(iterator.hasNext(), is(false));
    }
}