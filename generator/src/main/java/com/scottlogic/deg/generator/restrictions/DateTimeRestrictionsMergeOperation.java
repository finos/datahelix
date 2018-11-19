package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

public class DateTimeRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final DateTimeRestrictionsMerger dateTimeRestrictionsMerger = new DateTimeRestrictionsMerger();

    @Override
    public boolean applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        MergeResult<DateTimeRestrictions> mergeResult = dateTimeRestrictionsMerger.merge(
            left.getDateTimeRestrictions(), right.getDateTimeRestrictions());

        if (!mergeResult.successful) {
            return false;
        }

        DateTimeRestrictions dateTimeRestrictions = mergeResult.restrictions;

        if (dateTimeRestrictions != null) {
            ITypeRestrictions typeRestrictions = merged.getTypeRestrictions();
            if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Temporal)) {
                merged.setTypeRestrictions(TypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.Temporal));
            } else {
                return false;
            }
        }

        merged.setDateTimeRestrictions(dateTimeRestrictions);
        return true;
    }
}

