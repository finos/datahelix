package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;

import java.util.Collections;

import static com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory.forMaxLength;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createDefaultDateTimeRestrictions;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createDefaultNumericRestrictions;

public class FieldSpecFactory {
    public static final NullOnlyFieldSpec NULL_ONLY_FIELD_SPEC = new NullOnlyFieldSpec();

    public static WhitelistFieldSpec fromList(DistributedList<Object> whitelist) {
        return new WhitelistFieldSpec(whitelist, true);
    }

    public static RestrictionsFieldSpec fromRestriction(TypedRestrictions restrictions) {
        return new RestrictionsFieldSpec(restrictions, true, Collections.emptySet());
    }

    public static RestrictionsFieldSpec fromType(FieldType type) {
        switch (type) {
            case NUMERIC:
                return new RestrictionsFieldSpec(createDefaultNumericRestrictions(), true, Collections.emptySet());
            case DATETIME:
                return new RestrictionsFieldSpec(createDefaultDateTimeRestrictions(), true, Collections.emptySet());
            case STRING:
                return new RestrictionsFieldSpec(forMaxLength(1000), true, Collections.emptySet());
            default:
                throw new IllegalArgumentException("Unable to create FieldSpec from type " + type.name());
        }
    }

    public static NullOnlyFieldSpec nullOnly() {
        return NULL_ONLY_FIELD_SPEC;
    }
}
