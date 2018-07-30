package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.IConstraint;

import java.util.HashMap;
import java.util.Map;

public class ConstraintTypeClassifier {
    final Map<Class<? extends IConstraint>, ConstraintType> typeMapping = new HashMap<>();
    {
        typeMapping.put(NumericLimitConstConstraint.class, ConstraintType.Numeric);
    }

    public ConstraintType classify(IConstraint constraint) {
        if (!typeMapping.containsKey(constraint.getClass())) {
            throw new IllegalStateException();
        }
        return typeMapping.get(constraint);
    }
}
