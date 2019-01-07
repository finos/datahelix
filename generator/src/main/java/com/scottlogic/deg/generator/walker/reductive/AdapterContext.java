package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;

import java.util.Collection;
import java.util.HashSet;

public class AdapterContext {
    private final HashSet<AtomicConstraint> unfixedAtomicConstraints = new HashSet<>();
    private final HashSet<AtomicConstraint> nonContradictoryAtomicConstraints = new HashSet<>();
    private final HashSet<AtomicConstraint> conflictingAtomicConstraints = new HashSet<>();

    private boolean valid = true;
    private final AdapterContext parent;

    AdapterContext() {
        this.parent = null;
    }

    private AdapterContext(AdapterContext parent) {
        this.parent = parent;
    }

    Collection<AtomicConstraint> getAllUnfixedAtomicConstraints() {
        return unfixedAtomicConstraints;
    }

    Collection<AtomicConstraint> getAllNonContradictoryAtomicConstraints() {
        return nonContradictoryAtomicConstraints;
    }

    Collection<AtomicConstraint> getAllConflictingAtomicConstraints() {
        return conflictingAtomicConstraints;
    }

    public boolean isValid() {
        return this.valid;
    }

    AdapterContext forOption(ConstraintNode node){
        return new AdapterContext(this);
    }

    void setIsInvalid() {
        this.valid = false;
    }

    void addUnfixedAtomicConstraint(AtomicConstraint atomicConstraint) {
        unfixedAtomicConstraints.add(atomicConstraint);
        if (this.parent != null)
            this.parent.addUnfixedAtomicConstraint(atomicConstraint);
    }

    void addNonContradictoryAtomicConstraint(AtomicConstraint atomicConstraint) {
        this.nonContradictoryAtomicConstraints.add(atomicConstraint);
        if (this.parent != null)
            this.parent.addNonContradictoryAtomicConstraint(atomicConstraint);
    }

    void addConflictingAtomicConstraint(AtomicConstraint atomicConstraint) {
        this.conflictingAtomicConstraints.add(atomicConstraint);
        if (this.parent != null)
            this.parent.addConflictingAtomicConstraint(atomicConstraint);
    }

    public void treeIsInvalid() {
        this.valid = false;
        if (this.parent != null)
            this.parent.treeIsInvalid();
    }
}
