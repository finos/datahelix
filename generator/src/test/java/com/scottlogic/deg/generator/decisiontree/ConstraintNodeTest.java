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
import org.junit.jupiter.api.Test;

import static com.scottlogic.deg.generator.builders.TestConstraintNodeBuilder.constraintNode;
import static org.junit.jupiter.api.Assertions.*;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class ConstraintNodeTest {

    private static final Field A = createField("A");
    private static final Field B = createField("B");

    @Test
    public void equals_identicalConstraintNodesDifferentReferences_isTrue() {
        ConstraintNode constraintNode1 = constraintNode()
            .where(A).isInSet("a1", "a2")
            .where(B).isInSet("b1", "b2")
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .withDecision(
                        constraintNode()
                            .where(B).isInSet("b1"),
                        constraintNode()
                            .where(B).isNotInSet("b1")),
                constraintNode()
                    .where(A).isNotInSet("a1"))
            .build();

        ConstraintNode constraintNode2 = constraintNode()
            .where(A).isInSet("a1", "a2")
            .where(B).isInSet("b1", "b2")
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .withDecision(
                        constraintNode()
                            .where(B).isInSet("b1"),
                        constraintNode()
                            .where(B).isNotInSet("b1")),
                constraintNode()
                    .where(A).isNotInSet("a1"))
            .build();

        assertEquals(constraintNode1, constraintNode2);
    }

    @Test
    public void hashCode_identicalConstraintNodesDifferentReferences_areEqual() {
        ConstraintNode constraintNode1 = constraintNode()
            .where(A).isInSet("a1", "a2")
            .where(B).isInSet("b1", "b2")
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .withDecision(
                        constraintNode()
                            .where(B).isInSet("b1"),
                        constraintNode()
                            .where(B).isNotInSet("b1")),
                constraintNode()
                    .where(A).isNotInSet("a1"))
            .build();

        ConstraintNode constraintNode2 = constraintNode()
            .where(A).isInSet("a1", "a2")
            .where(B).isInSet("b1", "b2")
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .withDecision(
                        constraintNode()
                            .where(B).isInSet("b1"),
                        constraintNode()
                            .where(B).isNotInSet("b1")),
                constraintNode()
                    .where(A).isNotInSet("a1"))
            .build();

        assertEquals(constraintNode1.hashCode(), constraintNode2.hashCode());
    }

    @Test
    public void equals_differentConstraintNodesDifferentReferences_isFalse() {
        ConstraintNode constraintNode1 = constraintNode()
            .where(A).isInSet("a1", "a2")
            .where(B).isInSet("b1", "b2")
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .withDecision(
                        constraintNode()
                            .where(B).isInSet("b2"),
                        constraintNode()
                            .where(B).isNotInSet("b1")),
                constraintNode()
                    .where(A).isNotInSet("a1"))
            .build();

        ConstraintNode constraintNode2 = constraintNode()
            .where(A).isInSet("a1", "a2")
            .where(B).isInSet("b1", "b2")
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .withDecision(
                        constraintNode()
                            .where(B).isInSet("b1"), //diff
                        constraintNode()
                            .where(B).isNotInSet("b1")),
                constraintNode()
                    .where(A).isNotInSet("a1"))
            .build();

        assertNotEquals(constraintNode1, constraintNode2);
    }

    @Test
    public void hashCode_differentConstraintNodesDifferentReferences_areNotEqual() {
        ConstraintNode constraintNode1 = constraintNode()
            .where(A).isInSet("a1", "a2")
            .where(B).isInSet("b1", "b2")
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .withDecision(
                        constraintNode()
                            .where(B).isInSet("b2"), //diff
                        constraintNode()
                            .where(B).isNotInSet("b1")),
                constraintNode()
                    .where(A).isNotInSet("a1"))
            .build();

        ConstraintNode constraintNode2 = constraintNode()
            .where(A).isInSet("a1", "a2")
            .where(B).isInSet("b1", "b2")
            .withDecision(
                constraintNode()
                    .where(A).isInSet("a1")
                    .withDecision(
                        constraintNode()
                            .where(B).isInSet("b1"),
                        constraintNode()
                            .where(B).isNotInSet("b1")),
                constraintNode()
                    .where(A).isNotInSet("a1"))
            .build();

        assertNotEquals(constraintNode1.hashCode(), constraintNode2.hashCode());
    }
}