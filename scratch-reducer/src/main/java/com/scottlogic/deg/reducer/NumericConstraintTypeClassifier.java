package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.constraint.IHasNumericTypeToken;

import java.util.Map;

public class NumericConstraintTypeClassifier {
    private final Map<Class<? extends Number>, NumericConstraintType> typeMapping = Map.of(
            Integer.class, NumericConstraintType.Integer,
            Double.class, NumericConstraintType.Double
    );

    public NumericConstraintType classify(IConstraint constraint) {
        if (!(constraint instanceof IHasNumericTypeToken)) {
            throw new IllegalStateException();
        }
        final var hasNumericTypeToken = (IHasNumericTypeToken) constraint;
        if (!typeMapping.containsKey(hasNumericTypeToken.getTypeToken())) {
            throw new IllegalStateException();
        }
        return typeMapping.get(hasNumericTypeToken.getTypeToken());
    }
}
