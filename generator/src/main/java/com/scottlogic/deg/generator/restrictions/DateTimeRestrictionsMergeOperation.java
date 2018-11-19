package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

public class DateTimeRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final DateTimeRestrictionsMerger dateTimeRestrictionsMerger = new DateTimeRestrictionsMerger();

    @Override
    public boolean successful(FieldSpec left, FieldSpec right, FieldSpec merged) {
        try {
            DateTimeRestrictions dateTimeRestrictions = dateTimeRestrictionsMerger.merge(
                left.getDateTimeRestrictions(), right.getDateTimeRestrictions());

            if (dateTimeRestrictions != null) {
                ITypeRestrictions typeRestrictions = merged.getTypeRestrictions();
                if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Temporal)) {
                    merged.setTypeRestrictions(TypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.Temporal));
                } else {
                    throw new UnmergeableRestrictionException("Cannot merge date restriction");
                }
            }

            merged.setDateTimeRestrictions(dateTimeRestrictions);
            return true;
        } catch (UnmergeableRestrictionException e) {
            return false;
        }
    }
}

