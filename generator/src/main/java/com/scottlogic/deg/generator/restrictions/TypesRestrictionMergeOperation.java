package com.scottlogic.deg.generator.restrictions;

public class TypesRestrictionMergeOperation implements RestrictionMergeOperation {
    private static final TypeRestrictionsMerger typeRestrictionsMerger = new TypeRestrictionsMerger();

    @Override
    public boolean applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        MergeResult<TypeRestrictions> mergeResult = typeRestrictionsMerger.merge(
            left.getTypeRestrictions(),
            right.getTypeRestrictions());

        if (!mergeResult.successful) {
            return false;
        }

        TypeRestrictions restrictions = mergeResult.restrictions != null
            ? mergeResult.restrictions
            : DataTypeRestrictions.all;

        merged.setTypeRestrictions(restrictions);
        return true;
    }
}

