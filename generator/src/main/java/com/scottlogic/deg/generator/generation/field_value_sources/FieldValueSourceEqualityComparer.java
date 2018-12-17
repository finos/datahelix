package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.utils.EqualityComparer;

public class FieldValueSourceEqualityComparer implements EqualityComparer {
    @Override
    public int getHashCode(Object item) {
        return 0;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((FieldValueSource) item1, (FieldValueSource) item2);
    }

    private boolean equals(FieldValueSource source1, FieldValueSource source2) {
        if (objectsOppositeNullState(source1, source2)) {
            return false;
        }

        boolean areEqual = iterablesAreEqual(source1.generateAllValues(), source2.generateAllValues());
        if (areEqual) {
            areEqual = iterablesAreEqual(source1.generateInterestingValues(), source2.generateInterestingValues());
        }

        return areEqual;
    }

    private boolean iterablesAreEqual(Iterable<Object> iterable1, Iterable<Object> iterable2) {
        if (objectsOppositeNullState(iterable1, iterable2)) {
            return false;
        }

        return iterable1.equals(iterable2);
    }

    private boolean objectsOppositeNullState(Object object1, Object object2) {
        return object1 != null && object2 == null ||
               object1 == null && object2 != null;

    }
}
