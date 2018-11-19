package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

public class NumericRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final NumericRestrictionsMerger numericRestrictionsMerger = new NumericRestrictionsMerger();

    @Override
    public boolean applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        MergeResult<NumericRestrictions> mergeResult = numericRestrictionsMerger.merge(
            left.getNumericRestrictions(), right.getNumericRestrictions());

        if (!mergeResult.successful) {
            return false;
        }

        NumericRestrictions numberRestrictions = mergeResult.restrictions;
        if (numberRestrictions != null) {
            TypeRestrictions typeRestrictions = merged.getTypeRestrictions();
            if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.Numeric)) {
                merged.setTypeRestrictions(DataTypeRestrictions.createFromWhiteList(IsOfTypeConstraint.Types.Numeric));
            } else {
                return false;
            }
        }

        merged.setNumericRestrictions(numberRestrictions);
        return true;
    }
}

