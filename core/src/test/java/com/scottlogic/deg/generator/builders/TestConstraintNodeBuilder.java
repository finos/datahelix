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

package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.*;

import java.util.HashSet;
import java.util.Set;

public class TestConstraintNodeBuilder {
    protected Set<AtomicConstraint> constraints = new HashSet<>();
    private Set<DecisionNode> decisionNodes = new HashSet<>();
    private Set<NodeMarking> markings = new HashSet<>();

    protected TestConstraintNodeBuilder() {
    }

    public ConstraintNode build() {
        return applyNodeMarkings(markings, new ConstraintNodeBuilder().addAtomicConstraints(constraints).setDecisions(decisionNodes).build());
    }

    public static TestConstraintNodeBuilder constraintNode() {
        return new TestConstraintNodeBuilder();
    }

    public TestAtomicConstraintBuilder where(Field field) {
        return new TestAtomicConstraintBuilder(this, field);
    }

    public TestConstraintNodeBuilder withDecision(TestConstraintNodeBuilder... constraintNodes) {
        Set<ConstraintNode> nodes = new HashSet<>();
        for (TestConstraintNodeBuilder constraintNode : constraintNodes) {
            nodes.add(constraintNode.build());
        }
        decisionNodes.add(new DecisionNode(nodes));
        return this;
    }

    public TestConstraintNodeBuilder markNode(NodeMarking marking) {
        this.markings.add(marking);
        return this;
    }

    private ConstraintNode applyNodeMarkings(Set<NodeMarking> markings, ConstraintNode unmarkedNode) {
        ConstraintNode currentNode = unmarkedNode;
        for (NodeMarking marking : markings) {
            currentNode = currentNode.builder().markNode(marking).build();
        }
        return currentNode;
    }
}
