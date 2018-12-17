package com.scottlogic.deg.generator.restrictions.Equality;

import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.utils.EqualityComparer;

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
        if (objectsAreOppositeNullState(item1, item2)) {
            return false;
        }

        if (objectsBothNull(item1, item2)) {
            return true;
        }

        return nullnessAreEqual(item1.nullness, item2.nullness);
    }

    private boolean nullnessAreEqual(NullRestrictions.Nullness nullness1, NullRestrictions.Nullness nullness2) {
        if (nullness1 == null && nullness2 != null ||
            nullness1 != null && nullness2 == null) {
            return false;
        }

        if (nullness1 == null && nullness2 == null) {
            return true;
        }

        return nullness1.equals(nullness2);
    }

    private boolean objectsAreOppositeNullState(Object object1, Object object2) {
        return object1 == null && object2 != null ||
               object1 != null && object2 == null;
    }

    private boolean objectsBothNull(Object object1, Object object2) {
        return object1 == null && object2 == null;
    }
}
