package com.scottlogic.deg.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.*;

public class ConstraintFieldSniffer {

    public ConstraintAndFieldTuple generateTuple(IConstraint constraint) {
        final Field field = detectField(constraint);
        return new ConstraintAndFieldTuple(constraint, field);
    }

    public Field detectField(IConstraint constraint) {
        if (constraint instanceof NotConstraint) {
            return detectField(((NotConstraint) constraint).negatedConstraint);
        } else if (constraint instanceof IsEqualToConstantConstraint) {
            return ((IsEqualToConstantConstraint) constraint).field;
        } else if (constraint instanceof IsGreaterThanConstantConstraint) {
            return ((IsGreaterThanConstantConstraint) constraint).field;
        } else if (constraint instanceof IsInSetConstraint) {
            return ((IsInSetConstraint) constraint).field;
        } else if (constraint instanceof IsNullConstraint) {
            return ((IsNullConstraint) constraint).field;
        } else if (constraint instanceof IsOfTypeConstraint) {
            return ((IsOfTypeConstraint) constraint).field;
        } else if (constraint instanceof MatchesRegexConstraint) {
            return ((MatchesRegexConstraint) constraint).field;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
