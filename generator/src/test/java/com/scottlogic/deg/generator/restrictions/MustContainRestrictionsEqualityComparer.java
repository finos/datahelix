package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.decisiontree.testutils.EqualityComparer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;

import java.util.Collections;
import java.util.Set;

public class MustContainRestrictionsEqualityComparer implements EqualityComparer {
    private final EqualityComparer fieldSpecComparer;

    MustContainRestrictionsEqualityComparer(EqualityComparer fieldSpecComparer) {
        this.fieldSpecComparer = fieldSpecComparer;
    }

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


        return setsAreEqual(
            checkNullObject(item1.getRequiredObjects()),
            checkNullObject(item2.getRequiredObjects())
        );
    }

    private Set<FieldSpec> checkNullObject(Set<FieldSpec> set) {
        if (set == null) {
            return Collections.emptySet();
        }

        return set;
    }

    private boolean setsAreEqual(Set<FieldSpec> requiredObjects1, Set<FieldSpec> requiredObjects2) {
        if (requiredObjects1.size() != requiredObjects2.size()) {
            return false;
        }

        boolean areEqual = true;
        for (FieldSpec spec1 : requiredObjects1) {
            for (FieldSpec spec2 : requiredObjects2) {
                if (fieldSpecComparer.equals(spec1, spec2)) {
                    areEqual = true;
                    break;
                }

                areEqual = false;
            }
        }

        return areEqual;
    }
}
