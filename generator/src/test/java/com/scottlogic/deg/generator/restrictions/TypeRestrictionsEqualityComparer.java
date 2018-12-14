package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.decisiontree.test_utils.EqualityComparer;

import java.util.Set;

public class TypeRestrictionsEqualityComparer implements EqualityComparer {

    @Override
    public int getHashCode(Object item) {
        return 0;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((TypeRestrictions) item1, (TypeRestrictions) item2);
    }

    private boolean equals (TypeRestrictions item1, TypeRestrictions item2) {
        if (item1 == null && item2 != null ||
            item1 != null && item2 == null) {
            return false;
        }

        if (item1 == null && item2 == null) {
            return true;
        }

        if (item1.getClass() != item2.getClass()) {
            return false;
        }

        return item1.getAllowedTypes().equals(item2.getAllowedTypes());

    }
}
