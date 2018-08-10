package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

class TypeConstraintFieldSpecStrategyFactory {
    @FunctionalInterface
    public interface TypeConstraintFieldSpecStrategy extends Consumer<FieldSpec> {}

    private enum TypeConstraintFieldSpecStrategies implements TypeConstraintFieldSpecStrategy {
        numericStrategy(
                fieldSpec -> {
                    if (fieldSpec.getNumericRestrictions() == null) {
                        fieldSpec.setNumericRestrictions(new NumericRestrictions());
                    }
                }
        ),
        stringStrategy(
                fieldSpec -> {
                    if (fieldSpec.getStringRestrictions() == null) {
                        fieldSpec.setStringRestrictions(new StringRestrictions());
                    }
                }
        ),
        temporalStrategy(
                fieldSpec -> {
                    if (fieldSpec.getDateTimeRestrictions() == null) {
                        fieldSpec.setDateTimeRestrictions(new DateTimeRestrictions());
                    }
                }
        );

        TypeConstraintFieldSpecStrategies(TypeConstraintFieldSpecStrategy impl) {
            this.impl = impl;
        }

        private final TypeConstraintFieldSpecStrategy impl;

        @Override
        public void accept(FieldSpec fieldSpec) {
            this.impl.accept(fieldSpec);
        }
    }

    private static final Map<IsOfTypeConstraint.Types, TypeConstraintFieldSpecStrategy> strategies = new HashMap<>();
    static {
        strategies.put(IsOfTypeConstraint.Types.Numeric, TypeConstraintFieldSpecStrategies.numericStrategy);
        strategies.put(IsOfTypeConstraint.Types.String, TypeConstraintFieldSpecStrategies.stringStrategy);
        strategies.put(IsOfTypeConstraint.Types.Temporal, TypeConstraintFieldSpecStrategies.temporalStrategy);
    }

    public static TypeConstraintFieldSpecStrategy getStrategy(IsOfTypeConstraint constraint) {
        if (constraint.requiredType == null) {
            throw new IllegalStateException();
        }
        if (!strategies.containsKey(constraint.requiredType)) {
            throw new UnsupportedOperationException();
        }
        return strategies.get(constraint.requiredType);
    }
}