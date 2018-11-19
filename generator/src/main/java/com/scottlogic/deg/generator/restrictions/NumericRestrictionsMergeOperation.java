package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

public class NumericRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final NumericRestrictionsMerger numericRestrictionsMerger = new NumericRestrictionsMerger();

    @Override
    public boolean successful(FieldSpec left, FieldSpec right, FieldSpec merged) {
        try {
            NumericRestrictions numberRestrictions = numericRestrictionsMerger.merge(
                left.getNumericRestrictions(), right.getNumericRestrictions());

            if (numberRestrictions != null) {
                ITypeRestrictions typeRestrictions = merged.getTypeRestrictions();
                if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Numeric)) {
                    merged.setTypeRestrictions(TypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.Numeric));
                } else {
                    throw new UnmergeableRestrictionException("Cannot merge numeric restriction");
                }
            }

            merged.setNumericRestrictions(numberRestrictions);
            return true;
        } catch (UnmergeableRestrictionException e) {
            return false;
        }
    }
}

