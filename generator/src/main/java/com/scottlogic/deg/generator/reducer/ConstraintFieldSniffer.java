package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.*;

public class ConstraintFieldSniffer {

    ConstraintAndFieldTuple generateTuple(IConstraint constraint) {
        final Field field = detectField(constraint);
        return new ConstraintAndFieldTuple(constraint, field);
    }

    Field detectField(IConstraint constraint) {
        if (constraint instanceof NotConstraint) {
            return detectField(((NotConstraint) constraint).negatedConstraint);
        } else if (constraint instanceof IsEqualToConstantConstraint) {
            return ((IsEqualToConstantConstraint) constraint).field;
        } else if (constraint instanceof IsGreaterThanConstantConstraint) {
            return ((IsGreaterThanConstantConstraint) constraint).field;
        } else if (constraint instanceof IsGreaterThanOrEqualToConstantConstraint) {
            return ((IsGreaterThanOrEqualToConstantConstraint) constraint).field;
        } else if (constraint instanceof IsLessThanConstantConstraint) {
            return ((IsLessThanConstantConstraint) constraint).field;
        } else if (constraint instanceof IsLessThanOrEqualToConstantConstraint) {
            return ((IsLessThanOrEqualToConstantConstraint) constraint).field;
        } else if (constraint instanceof IsAfterConstantDateTimeConstraint) {
            return ((IsAfterConstantDateTimeConstraint) constraint).field;
        } else if (constraint instanceof IsAfterOrEqualToConstantDateTimeConstraint) {
            return ((IsAfterOrEqualToConstantDateTimeConstraint) constraint).field;
        } else if (constraint instanceof IsBeforeConstantDateTimeConstraint) {
            return ((IsBeforeConstantDateTimeConstraint) constraint).field;
        } else if (constraint instanceof IsBeforeOrEqualToConstantDateTimeConstraint) {
            return ((IsBeforeOrEqualToConstantDateTimeConstraint) constraint).field;
        } else if (constraint instanceof IsGranularToConstraint) {
            return ((IsGranularToConstraint) constraint).field;
        } else if (constraint instanceof IsInSetConstraint) {
            return ((IsInSetConstraint) constraint).field;
        } else if (constraint instanceof IsNullConstraint) {
            return ((IsNullConstraint) constraint).field;
        } else if (constraint instanceof IsOfTypeConstraint) {
            return ((IsOfTypeConstraint) constraint).field;
        } else if (constraint instanceof MatchesRegexConstraint) {
            return ((MatchesRegexConstraint) constraint).field;
        } else if (constraint instanceof ContainsRegexConstraint) {
            return ((ContainsRegexConstraint) constraint).field;
        } else if (constraint instanceof FormatConstraint) {
            return ((FormatConstraint) constraint).field;
        } else if (constraint instanceof StringHasLengthConstraint) {
            return ((StringHasLengthConstraint) constraint).field;
        } else if (constraint instanceof IsStringLongerThanConstraint) {
            return ((IsStringLongerThanConstraint) constraint).field;
        } else if (constraint instanceof IsStringShorterThanConstraint) {
            return ((IsStringShorterThanConstraint) constraint).field;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
