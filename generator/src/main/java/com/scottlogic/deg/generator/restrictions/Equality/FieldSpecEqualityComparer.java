package com.scottlogic.deg.generator.restrictions.Equality;

import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.utils.EqualityComparer;

public class FieldSpecEqualityComparer implements EqualityComparer {
    private EqualityComparer setRestrictionsComparer = new SetRestrictionsEqualityComparer();
    private EqualityComparer stringRestrictionsComparer = new StringRestrictionsEqualityComparer();
    private EqualityComparer nullRestrictionsComparer = new NullRestrictionsEqualityComparer();
    private EqualityComparer typeRestrictionsComparer = new TypeRestrictionsEqualityComparer();
    private EqualityComparer dateTimeRestrictionsComparer = new DateTimeRestrictionsEqualityComparer();
    private EqualityComparer formatRestrictionsComparer = new FormatRestrictionsEqualityComparer();
    private EqualityComparer granularityRestrictionsComparer = new GranularityRestrictionsEqualityComparer();
    private EqualityComparer mustContainRestrictionsComparer = new MustContainRestrictionsEqualityComparer(this);

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
        && nullRestrictionsComparer.equals(fieldSpec1.getNullRestrictions(), fieldSpec2.getNullRestrictions())
        && typeRestrictionsComparer.equals(fieldSpec1.getTypeRestrictions(), fieldSpec2.getTypeRestrictions())
        && dateTimeRestrictionsComparer.equals(fieldSpec1.getDateTimeRestrictions(), fieldSpec2.getDateTimeRestrictions())
        && formatRestrictionsComparer.equals(fieldSpec1.getFormatRestrictions(), fieldSpec2.getFormatRestrictions())
        && granularityRestrictionsComparer.equals(fieldSpec1.getGranularityRestrictions(), fieldSpec2.getGranularityRestrictions())
        && mustContainRestrictionsComparer.equals(fieldSpec1.getMustContainRestriction(), fieldSpec2.getMustContainRestriction());
    }
}

