package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.decisiontree.test_utils.EqualityComparer;

public class NullRestrictionsEqualityComparer implements EqualityComparer {

    @Override
    public int getHashCode(Object item) {
        return 0;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((NullRestrictions) item1, (NullRestrictions) item2);
    }

    private boolean equals (NullRestrictions item1, NullRestrictions item2) {
        if (item1 == null && item2 != null ||
            item1 != null && item2 == null) {
            return false;
        }

        if (item1 == null && item2 == null) {
            return true;
        }

        return item1.nullness.equals(item2.nullness);
    }

}
