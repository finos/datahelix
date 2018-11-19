package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.Optional;

public class DateTimeRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final DateTimeRestrictionsMerger dateTimeRestrictionsMerger = new DateTimeRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        DateTimeRestrictions dateTimeRestrictions = dateTimeRestrictionsMerger.merge(
            left.getDateTimeRestrictions(), right.getDateTimeRestrictions());

        if (dateTimeRestrictions == null) {
            return Optional.of(merged.setDateTimeRestrictions(null));
        }

        TypeRestrictions typeRestrictions = merged.getTypeRestrictions();
        if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Temporal)) {
            return Optional.empty();
        }

        return Optional.of(merged.setDateTimeRestrictions(dateTimeRestrictions).setTypeRestrictions(
            DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.Temporal)));
    }
}

