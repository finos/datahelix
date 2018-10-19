package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint.Types;

import java.util.HashSet;
import java.util.Set;

public class TypeRestrictionsMerger {
    public TypeRestrictions merge(TypeRestrictions left, TypeRestrictions right) {
        if (left == null && right == null)
            return null;
        if (left == null)
            return right;
        if (right == null)
            return left;

        final TypeRestrictions merged = left.intersect(right);

        if (merged == null) {
            throw new UnmergeableRestrictionException(
                    "Unable to merge type restrictions - two or more conflicting types found");
        }

        return merged;
    }
}
