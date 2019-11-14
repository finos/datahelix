/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Fields;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.scottlogic.deg.common.profile.FieldBuilder.createField;
import static com.scottlogic.deg.generator.builders.TestConstraintNodeBuilder.constraintNode;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class DecisionTreeOptimiserTest {

    DecisionTreeOptimiser optimiser = new DecisionTreeOptimiser();
    Field A = createField("A");
    Field B = createField("B");
    Field C = createField("C");
    Field D = createField("D");
    Field E = createField("E");
    Field F = createField("F");

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

        ConstraintNode actual = optimiser.optimiseTree(new DecisionTree(original, new Fields(Collections.EMPTY_LIST)))
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

        ConstraintNode actual = optimiser.optimiseTree(new DecisionTree(original, new Fields(Collections.EMPTY_LIST)))
            .getRootNode();

        assertThat(actual, sameBeanAs(original));
    }
}