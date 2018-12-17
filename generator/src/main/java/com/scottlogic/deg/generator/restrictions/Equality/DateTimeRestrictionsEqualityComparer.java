package com.scottlogic.deg.generator.restrictions.Equality;

import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;
import com.scottlogic.deg.generator.utils.EqualityComparer;

public class DateTimeRestrictionsEqualityComparer implements EqualityComparer {

    @Override
    public int getHashCode(Object item) {
        return 0;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((DateTimeRestrictions) item1, (DateTimeRestrictions) item2);
    }

    private boolean equals(DateTimeRestrictions item1, DateTimeRestrictions item2) {
        if (item1 == null && item2 != null ||
            item1 != null && item2 == null) {
            return false;
        }

        if (item1 == null && item2 == null){
            return true;
        }

        return item1.max.equals(item2.max)
            && item1.min.equals(item2.min);
    }
}
