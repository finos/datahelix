package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.test_utils.EqualityComparer;

public class ReductiveRowSpecEqualityComparer implements EqualityComparer {
    private EqualityComparer fieldToFieldSpecComparer = new FieldToFieldSpecComparer();

    @Override
    public int getHashCode(Object item) {
        return 0;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        if (item1 != null && item1.getClass() != ReductiveRowSpec.class ||
            item2 != null && item2.getClass() != ReductiveRowSpec.class) {
            return false;
        }

        return equals((ReductiveRowSpec) item1, (ReductiveRowSpec) item2);
    }

    private boolean equals(ReductiveRowSpec rowSpec1, ReductiveRowSpec rowSpec2) {
        if (objectsAreOppositeNullState(rowSpec1, rowSpec2)) {
            return false;
        }

        boolean result = fieldsAreEqual(rowSpec1.getFields(), rowSpec2.getFields());

        if (result) {
            result = fieldToFieldSpecComparer.equals(rowSpec1.getFieldToFieldSpec(), rowSpec2.getFieldToFieldSpec());
        }
        if (result) {
            result = rowSpec1.getLastFixedField().equals(rowSpec2.getLastFixedField());
        }

        return result;
    }

    private boolean fieldsAreEqual(ProfileFields profileFields1, ProfileFields profileFields2) {
        if (objectsAreOppositeNullState(profileFields1, profileFields2)) {
            return false;
        }

        if (profileFields1 == null && profileFields2 == null) {
            return true;
        }

        return profileFields1.equals(profileFields2);
    }

    private boolean objectsAreOppositeNullState(Object object1, Object object2) {
        return object1 != null && object2 == null ||
               object1 == null && object2 != null;

    }
}
