package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.constraint.IHasTypeToken;

import java.util.Map;

public class GenericConstraintTypeClassifier {
    private final Map<Class<?>, GenericConstraintType> typeMapping = Map.of(
            String.class, GenericConstraintType.String
    );

    public GenericConstraintType classify(IConstraint constraint) {
        if (!(constraint instanceof IHasTypeToken)) {
            throw new IllegalStateException();
        }
        final var hasTypeToken = (IHasTypeToken<?>) constraint;
        if (!typeMapping.containsKey(hasTypeToken.getTypeToken())) {
            throw new IllegalStateException();
        }
        return typeMapping.get(hasTypeToken.getTypeToken());
    }
}
