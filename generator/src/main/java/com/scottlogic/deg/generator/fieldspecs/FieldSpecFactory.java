package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;

import java.util.Collections;

import static com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory.forMaxLength;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createDefaultDateTimeRestrictions;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createDefaultNumericRestrictions;

public class FieldSpecFactory {
    private static final DistributedList<Object> NO_VALUES = DistributedList.empty();

    public static FieldSpec fromList(DistributedList<Object> whitelist) {
        return new FieldSpec(whitelist, null, true, Collections.emptySet());
    }

    public static FieldSpec fromRestriction(TypedRestrictions restrictions) {
        return new FieldSpec(null, restrictions, true, Collections.emptySet());
    }

    public static FieldSpec fromType(FieldType type) {
        switch (type) {
            case NUMERIC:
                return new FieldSpec(null, createDefaultNumericRestrictions(), true, Collections.emptySet());
            case DATETIME:
                return new FieldSpec(null, createDefaultDateTimeRestrictions(), true, Collections.emptySet());
            case STRING:
                return new FieldSpec(null, forMaxLength(1000), true, Collections.emptySet());
            default:
                throw new IllegalArgumentException("Unable to create FieldSpec from type " + type.name());
        }
    }

    public static FieldSpec nullOnly() {
        return new FieldSpec(NO_VALUES, null, true, Collections.emptySet());
    }
}
