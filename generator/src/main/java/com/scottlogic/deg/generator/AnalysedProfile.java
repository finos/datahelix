package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.IConstraintTreeNode;

import java.util.Collection;

public class AnalysedProfile implements IAnalysedProfile {
    private Collection<Field> fields;
    private IConstraintTreeNode treeRoot;

    @Override
    public Collection<Field> getFields() {
        return fields;
    }

    public IConstraintTreeNode getConstraintTreeRoot() {
        return treeRoot;
    }
}
