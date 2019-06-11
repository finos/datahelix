package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.decisiontree.testutils.EqualityComparer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;

public class FieldSpecEqualityComparer implements EqualityComparer {
    private EqualityComparer setRestrictionsComparer = new SetRestrictionsEqualityComparer();
    private EqualityComparer stringRestrictionsComparer = new StringRestrictionsEqualityComparer();
    private EqualityComparer typeRestrictionsComparer = new TypeRestrictionsEqualityComparer();
    private EqualityComparer dateTimeRestrictionsComparer = new DateTimeRestrictionsEqualityComparer();

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

        if (fieldSpec1 == null && fieldSpec2 == null){
            return true;
        }

        return setRestrictionsComparer.equals(fieldSpec1.getSetRestrictions(), fieldSpec2.getSetRestrictions())
        && stringRestrictionsComparer.equals(fieldSpec1.getStringRestrictions(), fieldSpec2.getStringRestrictions())
        && typeRestrictionsComparer.equals(fieldSpec1.getTypeRestrictions(), fieldSpec2.getTypeRestrictions())
        && dateTimeRestrictionsComparer.equals(fieldSpec1.getDateTimeRestrictions(), fieldSpec2.getDateTimeRestrictions());
    }
}

