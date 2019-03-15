package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.TreeDecisionNode;

import java.util.HashSet;
import java.util.Set;

public class ConstraintNodeBuilder {
    protected Set<AtomicConstraint> constraints = new HashSet<>();
    private Set<DecisionNode> decisionNodes = new HashSet<>();

    protected ConstraintNodeBuilder() {
    }

    public ConstraintNode build() {
        return new TreeConstraintNode(constraints, decisionNodes);
    }

    public static ConstraintNodeBuilder constraintNode() {
        return new ConstraintNodeBuilder();
    }

    public AtomicConstraintBuilder where(Field field) {
        return new AtomicConstraintBuilder(this, field);
    }

    public ConstraintNodeBuilder withDecisions(ConstraintNodeBuilder... constraintNodes) {
        Set<ConstraintNode> nodes = new HashSet<>();
        for (ConstraintNodeBuilder constraintNode : constraintNodes) {
            nodes.add(constraintNode.build());
        }
        decisionNodes.add(new TreeDecisionNode(nodes));
        return this;
    }
}
