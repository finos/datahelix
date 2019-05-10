package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.scottlogic.deg.generator.builders.ConstraintNodeBuilder.constraintNode;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class MostProlificConstraintOptimiserTest {

    MostProlificConstraintOptimiser optimiser = new MostProlificConstraintOptimiser();
    Field A = new Field("A");
    Field B = new Field("B");
    Field C = new Field("C");
    Field D = new Field("D");
    Field E = new Field("E");
    Field F = new Field("F");

    @Test
    public void optimise_circularDependency(){

        ConstraintNode original = constraintNode()
            .withDecision(
                constraintNode()
                    .where(A).isNull()
                    .where(B).isNull(),
                constraintNode()
                    .where(A).isNotNull())
            .withDecision(
                constraintNode()
                    .where(B).isNull()
                    .where(A).isNull(),
                constraintNode()
                    .where(B).isNotNull())
            .build();

        ConstraintNode actual = optimiser.optimiseTree(new DecisionTree(original, new ProfileFields(Collections.EMPTY_LIST), ""))
            .getRootNode();

        assertThat(actual, sameBeanAs(original));
    }

    @Test
    public void optimise_oneCommonIf(){
        ConstraintNode original = constraintNode()
            .where(A).isInSet("a1", "a2")
            .where(B).isInSet("b1", "b2")
            .where(C).isInSet("c1", "c2")
            .where(D).isInSet("d1", "d2")
            .where(E).isInSet("e1", "e2")
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .withDecision(
                        constraintNode()
                            .where(B).isInSet("b1")
                            .where(D).isInSet("d1"),
                        constraintNode()
                            .where(B).isNotInSet("b1"))
                    .withDecision(
                        constraintNode()
                            .where(C).isInSet("c1")
                            .where(E).isInSet("e1"),
                        constraintNode()
                            .where(C).isNotInSet("c1")),
                constraintNode()
                    .where(A).isNotInSet("a1"))
            .build();

        ConstraintNode actual = optimiser.optimiseTree(new DecisionTree(original, new ProfileFields(Collections.EMPTY_LIST), ""))
            .getRootNode();

        assertThat(actual, sameBeanAs(original));
    }

    @Test
    public void optimise_oneCommonIfTwoFields() {
        ConstraintNode original = constraintNode()
            .where(A).isInSet("a1", "a2")
            .where(B).isInSet("b1", "b2")
            .where(C).isInSet("c1", "c2")
            .where(D).isInSet("d1", "d2")
            .where(E).isInSet("e1", "e2")
            .where(F).isInSet("f1", "f2")
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .where(B).isInSet("b1")
                    .where(C).isInSet("c1")
                    .where(E).isInSet("e1"),
                constraintNode()
                    .where(A).isNotInSet("a1"),
                constraintNode()
                    .where(B).isNotInSet("b1"),
                constraintNode()
                    .where(C).isNotInSet("c1"))
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .where(B).isInSet("b1")
                    .where(D).isInSet("d1")
                    .where(F).isInSet("f1"),
                constraintNode()
                    .where(A).isNotInSet("a1"),
                constraintNode()
                    .where(B).isNotInSet("b1"),
                constraintNode()
                    .where(D).isNotInSet("d1"))
            .build();

        ConstraintNode actual = optimiser.optimiseTree(new DecisionTree(original, new ProfileFields(Collections.EMPTY_LIST), ""))
            .getRootNode();


        ConstraintNode expected = constraintNode()
            .where(A).isInSet("a1", "a2")
            .where(B).isInSet("b1", "b2")
            .where(C).isInSet("c1", "c2")
            .where(D).isInSet("d1", "d2")
            .where(E).isInSet("e1", "e2")
            .where(F).isInSet("f1", "f2")
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .withDecision(
                        constraintNode()
                            .where(B).isInSet("b1")
                            .withDecision(
                                constraintNode()
                                    .where(C).isInSet("c1")
                                    .where(E).isInSet("e1"),
                                constraintNode()
                                    .where(C).isNotInSet("c1")
                            )
                            .withDecision(
                                constraintNode()
                                    .where(D).isInSet("d1")
                                    .where(F).isInSet("f1"),
                                constraintNode()
                                    .where(D).isNotInSet("d1")),
                        constraintNode()
                            .where(B).isNotInSet("b1")),
                constraintNode()
                    .where(A).isNotInSet("a1")
            ).build();

        assertThat(actual, sameBeanAs(expected).ignoring("nodeMarkings.OPTIMISED.string"));
    }
}