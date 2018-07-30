package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.IConstraint;

import java.util.HashMap;
import java.util.Map;

public class NumericConstraintTypeClassifier {
    final Map<Class<? extends Number>, NumericConstraintType> typeMapping = new HashMap<>();
    {
        typeMapping.put(Integer.class, NumericConstraintType.Integer);
        typeMapping.put(Double.class, NumericConstraintType.Double);
    }

    public NumericConstraintType classify(IConstraint constraint) {
        if (!(constraint instanceof IHasNumericTypeToken)) {
            throw new IllegalStateException();
        }
        final IHasNumericTypeToken<?> hasNumericTypeToken = (IHasNumericTypeToken) constraint;
        if (!typeMapping.containsKey(hasNumericTypeToken.getTypeToken())) {
            throw new IllegalStateException();
        }
        return typeMapping.get(hasNumericTypeToken.getTypeToken());
    }
}
