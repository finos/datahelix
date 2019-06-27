package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.restrictions.*;

import java.util.Collections;

public class TypesRestrictionMergeOperation implements RestrictionMergeOperation {
    private static final TypeRestrictionsMerger typeRestrictionsMerger = new TypeRestrictionsMerger();

    @Override
    public FieldSpec applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        MergeResult<TypeRestrictions> mergeResult = typeRestrictionsMerger.merge(
            left.getTypeRestrictions(),
            right.getTypeRestrictions());

        if (!mergeResult.successful) {
            return FieldSpec.Empty.withWhitelist(Collections.emptySet());
        }

        TypeRestrictions restrictions = mergeResult.restrictions != null
            ? mergeResult.restrictions
            : DataTypeRestrictions.ALL_TYPES_PERMITTED;

        return merging.withTypeRestrictions(restrictions);
    }
}

