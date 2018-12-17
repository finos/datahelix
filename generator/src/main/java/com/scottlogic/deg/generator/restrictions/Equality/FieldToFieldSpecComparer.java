package com.scottlogic.deg.generator.restrictions.Equality;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.utils.EqualityComparer;

import java.util.Map;

public class FieldToFieldSpecComparer implements EqualityComparer {
    private EqualityComparer fieldSpecComparer = new FieldSpecEqualityComparer();

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
        if (!(item1 instanceof Map) || !(item2 instanceof Map)) {
            return false;
        }

        return equals((Map<Field, FieldSpec>) item1, (Map<Field, FieldSpec>) item2);
    }

    private boolean equals(Map<Field, FieldSpec> fieldToFieldSpec1, Map<Field, FieldSpec> fieldToFieldSpec2) {
        boolean result = fieldToFieldSpec1.size() == fieldToFieldSpec2.size();

        if (result) {
            for (Map.Entry<Field, FieldSpec> pair : fieldToFieldSpec1.entrySet()) {
                result =
                    fieldToFieldSpec2.containsKey(pair.getKey()) &&
                    fieldSpecComparer.equals(pair.getValue(), fieldToFieldSpec2.get(pair.getKey()));

                if (!result) {
                    break;
                }
            }
        }

        return result;
    }
}
