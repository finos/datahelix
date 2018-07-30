package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.AmongConstraint;
import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.constraint.NumericLimitConstConstraint;
import com.scottlogic.deg.constraint.TypeConstraint;

import java.util.Map;

public class ConstraintTypeClassifier {
    private final Map<Class<? extends IConstraint>, ConstraintType> typeMapping = Map.of(
            NumericLimitConstConstraint.class, ConstraintType.Numeric,
            AmongConstraint.class, ConstraintType.Generic,
            TypeConstraint.class, ConstraintType.Generic
    );

    public ConstraintType classify(IConstraint constraint) {
        if (!typeMapping.containsKey(constraint.getClass())) {
            return ConstraintType.Generic;
        }
        return typeMapping.get(constraint);
    }
}
