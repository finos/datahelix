package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.constraint.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.TreeDecisionNode;

import java.util.ArrayList;
import java.util.List;

public class ConstraintNodeBuilder {
    protected List<AtomicConstraint> constraints = new ArrayList<>();
    private List<DecisionNode> decisionNodes = new ArrayList<>();

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

    public ConstraintNodeBuilder withDecision(ConstraintNodeBuilder... constraintNodes) {
        List<ConstraintNode> nodes = new ArrayList<>();
        for (ConstraintNodeBuilder constraintNode : constraintNodes) {
            nodes.add(constraintNode.build());
        }
        decisionNodes.add(new TreeDecisionNode(nodes));
        return this;
    }
}
