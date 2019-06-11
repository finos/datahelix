package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.decisiontree.testutils.EqualityComparer;

import java.util.Collections;
import java.util.Set;

public class SetRestrictionsEqualityComparer implements EqualityComparer {

    @Override
    public int getHashCode(Object item) {
        return 0;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((SetRestrictions) item1, (SetRestrictions) item2);
    }

    private boolean equals(SetRestrictions item1, SetRestrictions item2){
        if (item1 == null && item2 != null ||
            item1 != null && item2 == null) {
            return false;
        }

        if (item1 == null && item2 == null){
            return true;
        }

        return checkNullObject(item1.getWhitelist()).equals(checkNullObject(item1.getWhitelist()));

        }

    private Set<Object> checkNullObject(Set<Object> set) {
        if (set == null) {
            return Collections.emptySet();
        }

        return set;
    }
}
