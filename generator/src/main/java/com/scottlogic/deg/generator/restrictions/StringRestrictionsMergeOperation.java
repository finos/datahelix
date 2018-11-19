package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

public class StringRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final StringRestrictionsMerger stringRestrictionsMerger = new StringRestrictionsMerger();

    @Override
    public boolean successful(FieldSpec left, FieldSpec right, FieldSpec merged) {
        try {
            StringRestrictions stringRestrictions = stringRestrictionsMerger.merge(
                left.getStringRestrictions(), right.getStringRestrictions());

            if (stringRestrictions != null) {
                ITypeRestrictions typeRestrictions = merged.getTypeRestrictions();
                if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.String)) {
                    merged.setTypeRestrictions(TypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.String));
                } else {
                    return false;
                }
            }

            merged.setStringRestrictions(stringRestrictions);
            return true;
        } catch (UnmergeableRestrictionException e) {
            return false;
        }
    }
}

