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
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Please use ConstraintNodeBuilder in main instead of this test builder
@Deprecated

public class ConstraintNodeBuilder {
    protected List<AtomicConstraint> constraints = new ArrayList<>();
    private List<DecisionNode> decisionNodes = new ArrayList<>();
    private Set<NodeMarking> markings = new HashSet<>();

    protected ConstraintNodeBuilder() {
    }

    public ConstraintNode build() {
        return applyNodeMarkings(markings, new ConstraintNode(constraints, decisionNodes));
    }

    public static ConstraintNodeBuilder constraintNode() {
        return new ConstraintNodeBuilder();
    }

    public AtomicConstraintBuilder where(Field field) {
        return new AtomicConstraintBuilder(this, field);
    }

    public ConstraintNodeBuilder withDecision(ConstraintNodeBuilder... constraintNodes) {
        List<ConstraintNode> nodes = new ArrayList<>();
        for (ConstraintNodeBuilder constraintNode : constraintNodes) {
            nodes.add(constraintNode.build());
        }
        decisionNodes.add(new DecisionNode(nodes));
        return this;
    }

    public ConstraintNodeBuilder markNode(NodeMarking marking) {
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
