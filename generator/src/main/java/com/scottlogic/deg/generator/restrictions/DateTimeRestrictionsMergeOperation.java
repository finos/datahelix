package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

public class DateTimeRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final DateTimeRestrictionsMerger dateTimeRestrictionsMerger = new DateTimeRestrictionsMerger();

    @Override
    public boolean applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        DateTimeRestrictions dateTimeRestrictions = dateTimeRestrictionsMerger.merge(
            left.getDateTimeRestrictions(), right.getDateTimeRestrictions());

        if (dateTimeRestrictions != null) {
            TypeRestrictions typeRestrictions = merged.getTypeRestrictions();
            if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Temporal)) {
                merged.setTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.Temporal));
            } else {
                return false;
            }
        }

        merged.setDateTimeRestrictions(dateTimeRestrictions);
        return true;
    }
}

