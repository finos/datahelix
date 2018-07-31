package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.input.Field;
import com.scottlogic.deg.restriction.FieldSpec;

public class FieldRestrictionFactory {
    private final ConstraintTypeClassifier constraintTypeClassifier = new ConstraintTypeClassifier();
    private final NumericFieldRestrictionFactory numericFieldRestrictionFactory = new NumericFieldRestrictionFactory();
    private final GenericFieldRestrictionFactory genericFieldRestrictionFactory = new GenericFieldRestrictionFactory();

    public FieldSpec getForConstraint(Field field, IConstraint constraint) {
        final var constraintType = constraintTypeClassifier.classify(constraint);
        switch(constraintType) {
            case Numeric:
                return numericFieldRestrictionFactory.getForConstraint(field, constraint);
            case Generic:
                return genericFieldRestrictionFactory.getForConstraint(field, constraint);
            default:
                throw new IllegalStateException();
        }
    }
}
