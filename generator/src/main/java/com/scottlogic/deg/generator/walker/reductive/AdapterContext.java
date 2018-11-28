package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;

import java.util.Collection;
import java.util.HashSet;

public class AdapterContext {
    private final HashSet<IConstraint> remainingAtomicConstraints = new HashSet<>();
    private final HashSet<IConstraint> removedAtomicConstraints = new HashSet<>();
    private final HashSet<IConstraint> conflictingAtomicConstraints = new HashSet<>();

    private boolean valid = true;
    private final AdapterContext parent;

    AdapterContext() {
        this.parent = null;
    }

    private AdapterContext(AdapterContext parent) {
        this.parent = parent;
    }

    Collection<IConstraint> getAllRemainingAtomicConstraints() {
        return remainingAtomicConstraints;
    }

    Collection<IConstraint> getAllRemovedAtomicConstraints() {
        return removedAtomicConstraints;
    }

    Collection<IConstraint> getAllConflictingAtomicConstraints() {
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

    void addRemainingAtomicConstraint(IConstraint atomicConstraint) {
        remainingAtomicConstraints.add(atomicConstraint);
        if (this.parent != null)
            this.parent.addRemainingAtomicConstraint(atomicConstraint);
    }

    void addRemovedAtomicConstraint(IConstraint atomicConstraint) {
        this.removedAtomicConstraints.add(atomicConstraint);
        if (this.parent != null)
            this.parent.addRemovedAtomicConstraint(atomicConstraint);
    }

    void addConflictingAtomicConstraint(IConstraint atomicConstraint) {
        this.conflictingAtomicConstraints.add(atomicConstraint);
        if (this.parent != null)
            this.parent.addConflictingAtomicConstraint(atomicConstraint);
    }
}
