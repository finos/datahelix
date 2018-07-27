package com.scottlogic.deg.generator.constraints;

import java.util.Collection;
import java.util.List;

public interface IConstraintTreeNode {

    Collection<IConstraint> getAtomicConstraints();

    List<IConstraintTreeNode> getChildNodes();

    void addAtomicConstraint(IConstraint constraint);

    void addChild(IConstraintTreeNode node);

    IConstraintTreeNode merge(IConstraintTreeNode other);
}
