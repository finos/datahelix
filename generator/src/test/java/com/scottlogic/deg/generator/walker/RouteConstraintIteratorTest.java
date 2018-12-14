package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.iterator.ConstraintIterator;
import com.scottlogic.deg.generator.walker.iterator.ConstraintIteratorFactory;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;

class RouteConstraintIteratorTest {

    // There should be verification on what the produced routes are, as well as how many routes get produced

    @Test
    void noSubDecisions_hasNext() {
        ConstraintNode node = IteratorTestHelper.endConstraint("");
        ConstraintIterator iterator = new ConstraintIteratorFactory().create(node);

        assertThat(iterator.hasNext(), is(true));
    }

    @Test
    void noSubDecisions_next_then_hasNextIsFalse() {
        ConstraintNode node = IteratorTestHelper.endConstraint("");
        ConstraintIterator iterator = new ConstraintIteratorFactory().create(node);

        RowSpecRoute route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(0)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void oneSubDecisions_oneHasOneOptions(){
        ConstraintNode node = IteratorTestHelper.constraintSingle();
        ConstraintIterator iterator = new ConstraintIteratorFactory().create(node);

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(1)));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void oneSubDecisions_oneHasTwoOptions(){
        ConstraintNode node = IteratorTestHelper.constraintDouble();
        ConstraintIterator iterator = new ConstraintIteratorFactory().create(node);

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(1)));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(1)));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void twoSubDecisions_oneHasTwoOptions(){
        ConstraintNode node = IteratorTestHelper.constraintSingleDouble();
        ConstraintIterator iterator = new ConstraintIteratorFactory().create(node);

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(2)));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(2)));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void threeSubDecisions_allHaveTwoOptions(){
        ConstraintNode node = IteratorTestHelper.constraintTripleDouble();
        ConstraintIterator iterator = new ConstraintIteratorFactory().create(node);

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(3)));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(3)));

        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(3)));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(3)));

        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(3)));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(3)));

        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(3)));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(3)));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void twoSubDecisions_bothHaveTwoOptions(){
        ConstraintNode node = IteratorTestHelper.constraintDoubleDouble("");
        ConstraintIterator iterator = new ConstraintIteratorFactory().create(node);

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(2)));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(2)));

        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(2)));


        assertThat(iterator.hasNext(), is(true));
        route = iterator.next();
        assertThat(route.subRoutes,  is(hasSize(2)));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void doubleLayeredConstraint(){
        ConstraintNode node = IteratorTestHelper.constraintDoubleLayered();
        ConstraintIterator iterator = new ConstraintIteratorFactory().create(node);

        assertThat(iterator.hasNext(), is(true));
        RowSpecRoute actual = iterator.next();

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();

        assertThat(iterator.hasNext(), is(true));
        actual = iterator.next();

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    void twentyFive_test() {
        ConstraintNode node = IteratorTestHelper.constraintBiggy();
        ConstraintIterator iterator = new ConstraintIteratorFactory().create(node);

        for (int i = 0; i<25; i++){
            assertThat(""+i,iterator.hasNext(), is(true));
            RowSpecRoute next = iterator.next();
            assertThat("",true);
        }
        assertThat(iterator.hasNext(), is(false));
    }
}