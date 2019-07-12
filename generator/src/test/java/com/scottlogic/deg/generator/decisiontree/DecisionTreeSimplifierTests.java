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
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.fieldspecs.whitelist.ElementFrequency;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyWhitelist;
import com.scottlogic.deg.generator.fieldspecs.whitelist.Whitelist;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DecisionTreeSimplifierTests {
    // TODO: Simplifier tests needs fleshing out

    private static Whitelist<Object> setOf(Object... objects) {
        return new FrequencyWhitelist<>(Stream.of(objects)
            .map(element -> new ElementFrequency<>(element, 1.0F))
            .collect(Collectors.toSet()));
    }

    @Test
    void simplify_decisionContainsSingleOptiontWithMatchingConstraintOnRootNode_doesNotSimplifyTree() {
        DecisionTree tree = new DecisionTree(
            new TreeConstraintNode(
                Arrays.asList(
                    new IsInSetConstraint(new Field("Field 1"), setOf(1, 2)),
                    new IsNullConstraint(new Field("Field 1")).negate()
                ),
                Collections.singletonList(
                    new TreeDecisionNode(
                        Collections.singletonList(
                            new TreeConstraintNode(
                                Collections.singletonList(
                                    new IsInSetConstraint(new Field("Field 1"), setOf(1, 2))
                                ),
                                Collections.emptyList()
                            )
                        )
                    )
                )
            ),
            new ProfileFields(
                new ArrayList<Field>() {{ add(new Field("Field 1")); }}
            )
        );
        DecisionTreeSimplifier simplifier = new DecisionTreeSimplifier();

        final DecisionTree result = simplifier.simplify(tree);

        Assert.assertEquals(result.rootNode.getAtomicConstraints(), tree.getRootNode().getAtomicConstraints());
        Assert.assertTrue(result.rootNode.getDecisions().isEmpty());
    }

    @Test
    void simplify_decisionContainsSingleOptionWithDifferingConstraintOnRootNode_simplifiesDecision() {
        DecisionTree tree = new DecisionTree(
            new TreeConstraintNode(
                Arrays.asList(
                    new IsInSetConstraint(new Field("Field 1"), setOf(1, 2)),
                    new IsNullConstraint(new Field("Field 1")).negate()
                ),
                Collections.singletonList(
                    new TreeDecisionNode(
                        Collections.singletonList(
                            new TreeConstraintNode(
                                Collections.singletonList(
                                    new IsInSetConstraint(new Field("Field 2"), setOf("A", "B"))
                                ),
                                Collections.emptyList()
                            )
                        )
                    )
                )
            ),
            new ProfileFields(
                new ArrayList<Field>() {{ add(new Field("Field 1")); }}
            )
        );
        DecisionTreeSimplifier simplifier = new DecisionTreeSimplifier();

        final DecisionTree result = simplifier.simplify(tree);

        final List<AtomicConstraint> expectedConstraints = Arrays.asList(
            new IsInSetConstraint(new Field("Field 1"), setOf(1, 2)),
            new IsNullConstraint(new Field("Field 1")).negate(),
            new IsInSetConstraint(new Field("Field 2"), setOf("A", "B"))
        );
        Assert.assertTrue(result.rootNode.getAtomicConstraints().containsAll(expectedConstraints));
        Assert.assertTrue(result.rootNode.getDecisions().isEmpty());
    }
}
