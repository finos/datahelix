package com.scottlogic.deg.generator.restrictions.Equality;

import com.scottlogic.deg.generator.restrictions.FormatRestrictions;
import com.scottlogic.deg.generator.utils.EqualityComparer;

public class FormatRestrictionsEqualityComparer implements EqualityComparer {

    @Override
    public int getHashCode(Object item) {
        return 0;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((FormatRestrictions) item1, (FormatRestrictions) item2);
    }

    private boolean equals (FormatRestrictions item1, FormatRestrictions item2) {
        if (item1 == null && item2 != null ||
            item1 != null && item2 == null) {
            return false;
        }

        if (item1 == null && item2 == null){
            return true;
        }

        return item1.formatString.equals(item2.formatString);
    }
}
