package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.OrConstraint;

import java.util.*;

/**
 * Defines a builder for a class that can contain constraints.
 */
public abstract class ConstraintChainBuilder<T> extends BaseConstraintBuilder<T> {
    protected List<Constraint> constraints = new ArrayList<>();
    private Constraint constraintToAdd;

    ConstraintChainBuilder(ConstraintChainBuilder<T> builder) {
        constraints.addAll(builder.constraints);
        constraintToAdd = builder.constraintToAdd;
    }

    ConstraintChainBuilder() {}

    abstract ConstraintChainBuilder<T> copy();

    public T build() {
        saveConstraint();
        return buildInner();
    }

    public ConstraintChainBuilder<T> withLessThanConstraint(Field field, int referenceValue) {
        return saveAndAddConstraint(new IsLessThanConstantConstraint(field, referenceValue, null));
    }

    public ConstraintChainBuilder<T> withGreaterThanConstraint(Field field, int referenceValue) {
        return saveAndAddConstraint(new IsGreaterThanConstantConstraint(field, referenceValue, null));
    }

    public ConstraintChainBuilder<T> withEqualToConstraint(Field barField, Object referenceValue) {
        return saveAndAddConstraint(new IsInSetConstraint(barField, Collections.singleton(referenceValue), null));
    }

    public ConstraintChainBuilder<T> withOrConstraint(ConstraintChainBuilder<OrConstraint> orBuilder) {
        return saveAndAddConstraint(orBuilder.build());
    }

    public ConstraintChainBuilder<T> withAndConstraint(ConstraintChainBuilder<AndConstraint> andBuilder) {
        return saveAndAddConstraint(andBuilder.build());
    }

    public ConstraintChainBuilder<T> withIfConstraint(BaseConstraintBuilder<ConditionalConstraint> builder) {
        return saveAndAddConstraint(builder.buildInner());
    }

    public ConstraintChainBuilder<T> withInSetConstraint(Field field, Object[] legalArray) {
        Set<Object> legalSet = new HashSet<>(Arrays.asList(legalArray));
        return saveAndAddConstraint(new IsInSetConstraint(field, legalSet, null ));
    }

    public ConstraintChainBuilder<T> withOfLengthConstraint(Field fooField, int length) {
        return saveAndAddConstraint(new StringHasLengthConstraint(fooField, length, null));
    }

    public ConstraintChainBuilder<T> withOfTypeConstraint(Field fooField, IsOfTypeConstraint.Types requiredType) {
        return saveAndAddConstraint(new IsOfTypeConstraint(fooField, requiredType, null));
    }

    public ConstraintChainBuilder<T> negate() {
        if (constraintToAdd == null) {
            throw new RuntimeException("Unable to call negate method on builder as no constraint found to negate.");
        }
        constraintToAdd = constraintToAdd.negate();
        return this.copy();
    }

    public ConstraintChainBuilder<T> violate() {
        if (constraintToAdd == null) {
            throw new RuntimeException("Unable to call violate method on builder as no constraint found to violate. ");
        } else if (!(constraintToAdd instanceof AtomicConstraint)) {
            throw new RuntimeException("Can only mark atomic constraints as violated.");
        }

        constraintToAdd = new ViolatedAtomicConstraint(((AtomicConstraint) constraintToAdd).negate());
        return this.copy();
    }

    private void saveConstraint() {
        if (constraintToAdd != null) {
            constraints.add(constraintToAdd);
            constraintToAdd = null;
        }
    }

    private ConstraintChainBuilder<T> saveAndAddConstraint(Constraint constraint) {
        saveConstraint();
        constraintToAdd = constraint;
        return this.copy();
    }

    public ConstraintChainBuilder<T> appendBuilder(ConstraintChainBuilder<? extends Constraint> builder) {
        saveConstraint();
        constraints.addAll(builder.constraints);
        constraintToAdd = builder.constraintToAdd;
        return this.copy();
    }
}
