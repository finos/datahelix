package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.input.Field;

public class FieldRestrictionFactory {
    private final ConstraintTypeClassifier constraintTypeClassifier = new ConstraintTypeClassifier();
    private final NumericFieldRestrictionFactory numericFieldRestrictionFactory = new NumericFieldRestrictionFactory();

    public IFieldRestriction getForConstraint(Field field, IConstraint constraint) {
        final ConstraintType constraintType = constraintTypeClassifier.classify(constraint);
        switch(constraintType) {
            case Numeric:
                return numericFieldRestrictionFactory.getForConstraint(field, constraint);
            case Abstract:
//                TODO
            default:
                throw new IllegalStateException();
        }
    }
}
