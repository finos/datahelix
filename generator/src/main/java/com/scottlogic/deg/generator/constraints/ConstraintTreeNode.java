package com.scottlogic.deg.generator.constraints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConstraintTreeNode implements IConstraintTreeNode {
    public final Collection<IConstraint> atomicConstraints;
    public final List<IConstraintTreeNode> childNodes;

    public ConstraintTreeNode(Collection<IConstraint> atomicConstraints, List<IConstraintTreeNode> childNodes) {
        this.atomicConstraints = atomicConstraints;
        this.childNodes = childNodes;
    }

    public ConstraintTreeNode(Collection<IConstraint> atomicConstraints) {
        this(atomicConstraints, new ArrayList<>());
    }

    public ConstraintTreeNode(IConstraint singleLeaf) {
        this.childNodes = new ArrayList<>();
        this.atomicConstraints = new ArrayList<>();
        this.atomicConstraints.add(singleLeaf);
    }

    public ConstraintTreeNode(IConstraintTreeNode childA, IConstraintTreeNode childB) {
        childNodes = new ArrayList<>();
        childNodes.add(childA);
        childNodes.add(childB);
        atomicConstraints = new ArrayList<>();
    }

    public ConstraintTreeNode() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public Collection<IConstraint> getAtomicConstraints() {
        return new ArrayList<>(atomicConstraints);
    }

    @Override
    public List<IConstraintTreeNode> getChildNodes() {
        return new ArrayList<>(childNodes);
    }

    @Override
    public void addAtomicConstraint(IConstraint constraint) {
        atomicConstraints.add(constraint);
    }

    @Override
    public void addChild(IConstraintTreeNode node) {
        childNodes.add(node);
    }

    @Override
    public IConstraintTreeNode merge(IConstraintTreeNode other) {
        atomicConstraints.addAll(other.getAtomicConstraints());
        childNodes.addAll(other.getChildNodes());
        return this;
    }
}
