package com.scottlogic.deg.generator.restrictions;

public class TypesRestrictionMergeOperation implements RestrictionMergeOperation {
    private static final TypeRestrictionsMerger typeRestrictionsMerger = new TypeRestrictionsMerger();

    @Override
    public boolean successful(FieldSpec left, FieldSpec right, FieldSpec merged) {
        try {
            ITypeRestrictions typeRestrictions = typeRestrictionsMerger.merge(
                left.getTypeRestrictions(),
                right.getTypeRestrictions());

            if (typeRestrictions == null) {
                typeRestrictions = TypeRestrictions.all;
            }

            merged.setTypeRestrictions(typeRestrictions);
            return true;
        } catch (UnmergeableRestrictionException e) {
            return false;
        }
    }
}

