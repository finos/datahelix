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

        final TypeRestrictions merged = new TypeRestrictions();
        merged.allowedTypes = getMergedTypes(left.allowedTypes, right.allowedTypes);

        return merged;
    }

    private Set<Types> getMergedTypes(Set<Types> left, Set<Types> right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (left == right) {
            return left;
        }

        Set<Types> intersection = new HashSet<>(left);
        intersection.retainAll(right);

        if (intersection.isEmpty()) {
            throw new UnmergeableRestrictionException(
                "Unable to merge type restrictions - two or more conflicting types found");
        }


        return intersection;
    }
}
