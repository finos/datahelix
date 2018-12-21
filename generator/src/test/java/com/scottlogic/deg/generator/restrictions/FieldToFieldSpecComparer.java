package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.decisiontree.test_utils.EqualityComparer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;

import java.util.Map;

public class FieldToFieldSpecComparer implements EqualityComparer {
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
                    pair.getValue().equals(fieldToFieldSpec2.get(pair.getKey()));

                if (!result) {
                    break;
                }
            }
        }

        return result;
    }
}
