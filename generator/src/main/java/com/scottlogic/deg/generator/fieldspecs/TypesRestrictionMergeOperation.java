package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.restrictions.DataTypeRestrictions;
import com.scottlogic.deg.generator.restrictions.MergeResult;
import com.scottlogic.deg.generator.restrictions.TypeRestrictions;
import com.scottlogic.deg.generator.restrictions.TypeRestrictionsMerger;

import java.util.Optional;

public class TypesRestrictionMergeOperation implements RestrictionMergeOperation {
    private static final TypeRestrictionsMerger typeRestrictionsMerger = new TypeRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        MergeResult<TypeRestrictions> mergeResult = typeRestrictionsMerger.merge(
            left.getTypeRestrictions(),
            right.getTypeRestrictions());

        if (!mergeResult.successful) {
            return Optional.empty();
        }

        TypeRestrictions restrictions = mergeResult.restrictions != null
            ? mergeResult.restrictions
            : DataTypeRestrictions.all;

        return Optional.of(merging.withTypeRestrictions(
            restrictions,
            FieldSpecSource.fromFieldSpecs(left, right)));
    }
}

