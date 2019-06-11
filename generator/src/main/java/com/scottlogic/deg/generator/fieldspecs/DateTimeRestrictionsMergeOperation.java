package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.Optional;

public class DateTimeRestrictionsMergeOperation implements RestrictionMergeOperation {
    private final DateTimeRestrictionsMerger merger;

    @Inject
    public DateTimeRestrictionsMergeOperation(DateTimeRestrictionsMerger merger) {
        this.merger = merger;
    }

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        MergeResult<DateTimeRestrictions> mergeResult = merger.merge(
            left.getDateTimeRestrictions(), right.getDateTimeRestrictions());

        if (!mergeResult.successful){
            //no datetimes can be created

            TypeRestrictions typeRestrictions = merging.getTypeRestrictions();
            if (typeRestrictions == null){
                typeRestrictions = DataTypeRestrictions.ALL_TYPES_PERMITTED;
            }

            return Optional.of(merging
                .withTypeRestrictions(
                    typeRestrictions.except(IsOfTypeConstraint.Types.DATETIME)));
        }

        if (mergeResult.restrictions == null) {
            return Optional.of(merging); //no change
        }

        return Optional.of(merging
            .withDateTimeRestrictions(
                mergeResult.restrictions));
    }
}
