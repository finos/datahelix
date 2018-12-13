package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.decisiontree.test_utils.EqualityComparer;

public class ReductiveRowSpecEqualityComparer implements EqualityComparer {
    private EqualityComparer fieldToFieldSpecComparer = new FieldToFieldSpecComparer();

    @Override
    public int getHashCode(Object item) {
        return 0;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        if (item1 != null && item2 == null ||
            item1 == null && item2 != null) {
            return false;
        }
        if (item1.getClass() != ReductiveRowSpec.class ||
            item2.getClass() != ReductiveRowSpec.class) {
            return false;
        }

        return equals((ReductiveRowSpec) item1, (ReductiveRowSpec) item2);
    }

    private boolean equals(ReductiveRowSpec rowSpec1, ReductiveRowSpec rowSpec2) {
        boolean result = rowSpec1.getFields().equals(rowSpec2.getFields());

        if (result) {
            result = fieldToFieldSpecComparer.equals(rowSpec1.getFieldToFieldSpec(), rowSpec2.getFieldToFieldSpec());
        }
        if (result) {
            result = rowSpec1.getLastFixedField().equals(rowSpec2.getLastFixedField());
        }

        return result;
    }
}
