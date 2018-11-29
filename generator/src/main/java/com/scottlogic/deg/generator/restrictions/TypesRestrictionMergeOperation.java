package com.scottlogic.deg.generator.restrictions;

import java.util.Optional;

public class TypesRestrictionMergeOperation implements RestrictionMergeOperation {
    private static final TypeRestrictionsMerger typeRestrictionsMerger = new TypeRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        MergeResult<TypeRestrictions> mergeResult = typeRestrictionsMerger.merge(
            left.getTypeRestrictions(),
            right.getTypeRestrictions());

        if (!mergeResult.successful) {
            return Optional.empty();
        }

        TypeRestrictions restrictions = mergeResult.restrictions != null
            ? mergeResult.restrictions
            : DataTypeRestrictions.all;

        return Optional.of(merged.withTypeRestrictions(restrictions));
    }
}

