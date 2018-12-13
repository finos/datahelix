package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.decisiontree.test_utils.DefaultEqualityComparer;
import com.scottlogic.deg.generator.decisiontree.test_utils.EqualityComparer;

public class FieldSpecEqualityComparer implements EqualityComparer {
    private EqualityComparer setRestrictionsComparer = new DefaultEqualityComparer();
    private EqualityComparer stringRestrictionsComparer = new DefaultEqualityComparer();
    private EqualityComparer nullRestrictionsComparer = new DefaultEqualityComparer();
    private EqualityComparer typeRestrictionsComparer = new DefaultEqualityComparer();
    private EqualityComparer dateTimeRestrictionsComparer = new DefaultEqualityComparer();
    private EqualityComparer formatRestrictionsComparer = new DefaultEqualityComparer();
    private EqualityComparer granularityRestrictionsComparer = new DefaultEqualityComparer();
    private EqualityComparer mustContainRestrictionsComparer = new DefaultEqualityComparer();
    private EqualityComparer fieldSpecSourceComparer = new DefaultEqualityComparer();

    @Override
    public int getHashCode(Object item) {
        return 0;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((FieldSpec) item1, (FieldSpec) item2);
    }

    private boolean equals(FieldSpec fieldSpec1, FieldSpec fieldSpec2) {
        if (fieldSpec1 == null && fieldSpec2 != null ||
            fieldSpec1 != null && fieldSpec2 == null) {
            return false;
        }

        //must not have any other restrictions on <fieldSpec>
        //<fieldSpec> must have a must contain restriction
        //must contain restriction must have 1 item in collection (field spec)
        //must contain restriction single item must have null restriction
        //must contain restriction single item must not have any other restrictions
        return false;
    }
}
