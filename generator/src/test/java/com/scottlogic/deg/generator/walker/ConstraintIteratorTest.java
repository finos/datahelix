package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class ConstraintIteratorTest {

    @Test
    void noSubDecisions_hasNext() {
        ConstraintNode node = new ConstraintNode();
        ConstraintIterator iterator = new ConstraintIterator(node);

        assertThat(iterator.hasNext(), is(true));
    }

    @Test
    void noSubDecisions_next_then_hasNextIsFalse() {
        ConstraintNode node = new ConstraintNode();
        ConstraintIterator iterator = new ConstraintIterator(node);

        RowSpecRoute route = iterator.next();
        assertThat(route.decisionIndex, is(0));//defaults to 0 as not set
        assertThat(route.subRoutes,  is(emptyArray()));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void oneSubDecisions_oneHasOneOptions(){
        ConstraintNode node = IteratorTestHelper.constraintSingle();
        ConstraintIterator iterator = new ConstraintIterator(node);

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(1)));
        assertThat(route.subRoutes[0].decisionIndex, is(0));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void oneSubDecisions_oneHasTwoOptions(){
        ConstraintNode node = IteratorTestHelper.constraintDouble();
        ConstraintIterator iterator = new ConstraintIterator(node);

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(1)));
        assertThat(route.subRoutes[0].decisionIndex, is(0));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(1)));
        assertThat(route.subRoutes[0].decisionIndex, is(1));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void twoSubDecisions_oneHasTwoOptions(){
        ConstraintNode node = IteratorTestHelper.constraintSingleDouble();
        ConstraintIterator iterator = new ConstraintIterator(node);

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(2)));
        assertThat(route.subRoutes[0].decisionIndex, is(0));
        assertThat(route.subRoutes[1].decisionIndex, is(0));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(2)));
        assertThat(route.subRoutes[0].decisionIndex, is(0));
        assertThat(route.subRoutes[1].decisionIndex, is(1));

        assertThat(iterator.hasNext(), is(false));
    }





    ////////////////////////////////////////////


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
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(emptyArray()));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void twoOptions_hasNext() {
        DecisionNode node = IteratorTestHelper.doubleDecision();
        DecisionIterator iterator = DecisionIterator.build(Arrays.asList(node));

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute route = iterator.next().get(0);
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(emptyArray()));

        assertThat(iterator.hasNext(), is(true));
        route = iterator.next().get(0);
        assertThat(route.decisionIndex, is(1));
        assertThat(route.subRoutes,  is(emptyArray()));

        assertThat(iterator.hasNext(), is(false));
    }


}