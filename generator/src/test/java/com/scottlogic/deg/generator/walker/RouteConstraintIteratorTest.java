package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.factory.ConstraintIteratorFactory;
import com.scottlogic.deg.generator.walker.factory.ConstraintIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class RouteConstraintIteratorTest {

    @Test
    void noSubDecisions_hasNext() {
        ConstraintNode node = IteratorTestHelper.endConstraint();
        ConstraintIterator iterator = ConstraintIteratorFactory.create(node);

        assertThat(iterator.hasNext(), is(true));
    }

    @Test
    void noSubDecisions_next_then_hasNextIsFalse() {
        ConstraintNode node = IteratorTestHelper.endConstraint();
        ConstraintIterator iterator = ConstraintIteratorFactory.create(node);

        RowSpecRoute route = iterator.next();
        assertThat(route.decisionIndex, is(0));//defaults to 0 as not set
        assertThat(route.subRoutes,  is(emptyArray()));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void oneSubDecisions_oneHasOneOptions(){
        ConstraintNode node = IteratorTestHelper.constraintSingle();
        ConstraintIterator iterator = ConstraintIteratorFactory.create(node);

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
        ConstraintIterator iterator = ConstraintIteratorFactory.create(node);

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
        ConstraintIterator iterator = ConstraintIteratorFactory.create(node);

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

    @Test
    void threeSubDecisions_allHaveTwoOptions(){
        ConstraintNode node = IteratorTestHelper.constraintTripleDouble();
        ConstraintIterator iterator = ConstraintIteratorFactory.create(node);

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(3)));
        assertThat(route.subRoutes[0].decisionIndex, is(0));
        assertThat(route.subRoutes[1].decisionIndex, is(0));
        assertThat(route.subRoutes[2].decisionIndex, is(0));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(3)));
        assertThat(route.subRoutes[0].decisionIndex, is(0));
        assertThat(route.subRoutes[1].decisionIndex, is(0));
        assertThat(route.subRoutes[2].decisionIndex, is(1));

        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(3)));
        assertThat(route.subRoutes[0].decisionIndex, is(0));
        assertThat(route.subRoutes[1].decisionIndex, is(1));
        assertThat(route.subRoutes[2].decisionIndex, is(0));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(3)));
        assertThat(route.subRoutes[0].decisionIndex, is(0));
        assertThat(route.subRoutes[1].decisionIndex, is(1));
        assertThat(route.subRoutes[2].decisionIndex, is(1));

        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(3)));
        assertThat(route.subRoutes[0].decisionIndex, is(1));
        assertThat(route.subRoutes[1].decisionIndex, is(0));
        assertThat(route.subRoutes[2].decisionIndex, is(0));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(3)));
        assertThat(route.subRoutes[0].decisionIndex, is(1));
        assertThat(route.subRoutes[1].decisionIndex, is(0));
        assertThat(route.subRoutes[2].decisionIndex, is(1));

        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(3)));
        assertThat(route.subRoutes[0].decisionIndex, is(1));
        assertThat(route.subRoutes[1].decisionIndex, is(1));
        assertThat(route.subRoutes[2].decisionIndex, is(0));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(3)));
        assertThat(route.subRoutes[0].decisionIndex, is(1));
        assertThat(route.subRoutes[1].decisionIndex, is(1));
        assertThat(route.subRoutes[2].decisionIndex, is(1));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void twoSubDecisions_bothHaveTwoOptions(){
        ConstraintNode node = IteratorTestHelper.constraintDoubleDouble();
        ConstraintIterator iterator = ConstraintIteratorFactory.create(node);

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

        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(2)));
        assertThat(route.subRoutes[0].decisionIndex, is(1));
        assertThat(route.subRoutes[1].decisionIndex, is(0));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.decisionIndex, is(0));
        assertThat(route.subRoutes,  is(arrayWithSize(2)));
        assertThat(route.subRoutes[0].decisionIndex, is(1));
        assertThat(route.subRoutes[1].decisionIndex, is(1));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void doubleLayeredConstraint(){
        ConstraintNode node = IteratorTestHelper.constraintDoubleLayered();
        ConstraintIterator iterator = ConstraintIteratorFactory.create(node);

        RowSpecRoute leftDecision = new RowSpecRoute();
        leftDecision.subRoutes = new RowSpecRoute[]{};

        RowSpecRoute lowerLeftDecision = new RowSpecRoute();
        lowerLeftDecision.subRoutes = new RowSpecRoute[]{};

        RowSpecRoute lowerRightDecision = new RowSpecRoute();
        lowerRightDecision.subRoutes = new RowSpecRoute[]{};

        RowSpecRoute rightOuterDecision = new RowSpecRoute();
        rightOuterDecision.decisionIndex = 0;
        rightOuterDecision.subRoutes = new RowSpecRoute[]{lowerLeftDecision, lowerRightDecision};

        RowSpecRoute expected = new RowSpecRoute();
        expected.decisionIndex = 0;
        expected.subRoutes = new RowSpecRoute[]{leftDecision, rightOuterDecision};

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute actual = iterator.next();
        leftDecision.decisionIndex = 0;
        lowerLeftDecision.decisionIndex = 0;
        lowerRightDecision.decisionIndex = 0;
        assertThat(actual, is(sameBeanAs(expected)));

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();
        leftDecision.decisionIndex = 0;
        lowerLeftDecision.decisionIndex = 0;
        lowerRightDecision.decisionIndex = 1;
        assertThat(actual, is(sameBeanAs(expected)));

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();
        leftDecision.decisionIndex = 0;
        lowerLeftDecision.decisionIndex = 1;
        lowerRightDecision.decisionIndex = 0;
        assertThat(actual, is(sameBeanAs(expected)));

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();
        leftDecision.decisionIndex = 0;
        lowerLeftDecision.decisionIndex = 1;
        lowerRightDecision.decisionIndex = 1;
        assertThat(actual, is(sameBeanAs(expected)));

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();
        leftDecision.decisionIndex = 1;
        lowerLeftDecision.decisionIndex = 0;
        lowerRightDecision.decisionIndex = 0;
        assertThat(actual, is(sameBeanAs(expected)));

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();
        leftDecision.decisionIndex = 1;
        lowerLeftDecision.decisionIndex = 0;
        lowerRightDecision.decisionIndex = 1;
        assertThat(actual, is(sameBeanAs(expected)));

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();
        leftDecision.decisionIndex = 1;
        lowerLeftDecision.decisionIndex = 1;
        lowerRightDecision.decisionIndex = 0;
        assertThat(actual, is(sameBeanAs(expected)));

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();
        leftDecision.decisionIndex = 1;
        lowerLeftDecision.decisionIndex = 1;
        lowerRightDecision.decisionIndex = 1;
        assertThat(actual, is(sameBeanAs(expected)));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void twentyFive_test() {
        ConstraintNode node = IteratorTestHelper.constraintBiggy();
        ConstraintIterator iterator = ConstraintIteratorFactory.create(node);

        for (int i = 0; i<25; i++){
            assertThat(""+i,iterator.hasNext(), is(true));
            RowSpecRoute next = iterator.next();
            assertThat("",true);
        }
        assertThat(iterator.hasNext(), is(false));
    }
}