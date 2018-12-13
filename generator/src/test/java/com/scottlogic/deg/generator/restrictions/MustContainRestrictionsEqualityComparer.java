package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.decisiontree.test_utils.EqualityComparer;

import java.util.Collections;
import java.util.Set;

public class MustContainRestrictionsEqualityComparer implements EqualityComparer {

    @Override
    public int getHashCode(Object item) {
        return 0;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((MustContainRestriction) item1, (MustContainRestriction) item2);
    }

    private  boolean equals (MustContainRestriction item1, MustContainRestriction item2) {
        if (item1 == null && item2 != null ||
            item1 != null && item2 == null) {
            return false;
        }

        if (item1 == null && item2 == null){
            return true;
        }

        return checkNullObject(item1.getRequiredObjects()).equals(checkNullObject(item2.getRequiredObjects()));
    }

    private Set<FieldSpec> checkNullObject(Set<FieldSpec> set) {
        if (set == null) {
            return Collections.emptySet();
        }

        return set;
    }
}
