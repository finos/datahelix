package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsGreaterThanConstantConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsLessThanConstantConstraint;
import com.scottlogic.deg.generator.constraints.atomic.ViolatedAtomicConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.OrConstraint;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Defines a builder for a class that can contain constraints.
 */
public abstract class ConstraintChainBuilder<T> {
    protected Collection<Constraint> constraints = new ArrayList<>();
    private Constraint constraintToAdd;

    public T build() {
        saveConstraint();
        return buildInner();
    }

    abstract T buildInner();

    public ConstraintChainBuilder<T> withConstraint(Constraint constraint) {
        return saveAndAddConstraint(constraint);
    }

    public ConstraintChainBuilder<T> withConstraints(Collection<Constraint> constraints) {
        saveConstraint();
        this.constraints.addAll(constraints);
        return this;
    }

    public ConstraintChainBuilder<T> withLessThanConstraint(Field fooField, int referenceValue) {
        return saveAndAddConstraint(new IsLessThanConstantConstraint(fooField, referenceValue, null));
    }

    public ConstraintChainBuilder<T> withGreaterThanConstraint(Field fooField, int referenceValue) {
        return saveAndAddConstraint(new IsGreaterThanConstantConstraint(fooField, referenceValue, null));
    }

    public ConstraintChainBuilder<T> withOrConstraint(ConstraintChainBuilder<OrConstraint> orBuilder) {
        return saveAndAddConstraint(orBuilder.build());
    }

    public ConstraintChainBuilder<T> withAndConstraint(ConstraintChainBuilder<AndConstraint> andBuilder) {
        return saveAndAddConstraint(andBuilder.build());
    }

    public ConstraintChainBuilder<T> negate() {
        if (constraintToAdd == null) {
            throw new RuntimeException("Unable to call negate method on builder as no constraint found to negate.");
        }
        constraintToAdd = constraintToAdd.negate();
        return this;
    }

    public ConstraintChainBuilder<T> violate() {
        if (constraintToAdd == null) {
            throw new RuntimeException("Unable to call violate method on builder as no constraint found to violate. ");
        }
        else if (!(constraintToAdd instanceof AtomicConstraint)) {
            throw new RuntimeException("Can only mark atomic constraints as violated.");
        }

        constraintToAdd = new ViolatedAtomicConstraint(((AtomicConstraint) constraintToAdd).negate());
        return this;
    }

    void saveConstraint() {
        if (constraintToAdd != null) {
            constraints.add(constraintToAdd);
            constraintToAdd = null;
        }
    }

    private ConstraintChainBuilder<T> saveAndAddConstraint(Constraint constraint) {
        saveConstraint();
        constraintToAdd = constraint;
        return this;
    }
}
