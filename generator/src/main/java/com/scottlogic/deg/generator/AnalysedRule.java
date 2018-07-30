package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.IConstraintTreeNode;

public class AnalysedRule {
    public final String description;
    public final IConstraintTreeNode constraintRoot;

    public AnalysedRule(String description, IConstraintTreeNode constraintRoot) {
        this.description = description;
        this.constraintRoot = constraintRoot;
    }
}
