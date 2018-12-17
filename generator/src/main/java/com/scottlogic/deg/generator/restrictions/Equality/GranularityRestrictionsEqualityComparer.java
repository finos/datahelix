package com.scottlogic.deg.generator.restrictions.Equality;

import com.scottlogic.deg.generator.restrictions.GranularityRestrictions;
import com.scottlogic.deg.generator.utils.EqualityComparer;

public class GranularityRestrictionsEqualityComparer implements EqualityComparer {

    @Override
    public int getHashCode(Object item) {
        return 0;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((GranularityRestrictions) item1, (GranularityRestrictions) item2);
    }

    private boolean equals(GranularityRestrictions item1, GranularityRestrictions item2) {
        if (item1 == null && item2 != null ||
            item1 != null && item2 == null) {
            return false;
        }

        if (item1 == null && item2 == null){
            return true;
        }

        return item1.getNumericScale() == item2.getNumericScale();
    }
}
