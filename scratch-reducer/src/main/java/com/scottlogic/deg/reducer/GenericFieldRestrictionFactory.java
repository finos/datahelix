package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.input.Field;
import com.scottlogic.deg.restriction.FieldSpec;
import com.scottlogic.deg.restriction.StringFieldRestriction;

public class GenericFieldRestrictionFactory {
    private final GenericConstraintTypeClassifier genericConstraintTypeClassifier = new GenericConstraintTypeClassifier();
    private final NumericFieldRestrictionFactory numericFieldRestrictionFactory = new NumericFieldRestrictionFactory();

    public FieldSpec getForConstraint(Field field, IConstraint constraint) {
        final var constraintType = genericConstraintTypeClassifier.classify(constraint);
        switch(constraintType) {
            case String:
                return new StringFieldRestriction(field);
            case Numeric:
                return numericFieldRestrictionFactory.getForConstraint(field, constraint);
            default:
                throw new IllegalStateException();
        }
    }
}
