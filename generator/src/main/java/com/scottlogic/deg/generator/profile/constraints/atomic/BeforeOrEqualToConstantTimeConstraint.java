package com.scottlogic.deg.generator.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.HelixTime;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;
import com.scottlogic.deg.generator.utils.Defaults;

import java.time.LocalTime;

public class BeforeOrEqualToConstantTimeConstraint implements AtomicConstraint {
    public final Field field;
    public final HelixTime referenceValue;

    public BeforeOrEqualToConstantTimeConstraint(Field field, HelixTime referenceValue) {
        this.field = field;
        this.referenceValue = referenceValue;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        return new AfterConstantTimeConstraint(field, referenceValue);
    }

    @Override
    public FieldSpec toFieldSpec() {
        final Limit<LocalTime> max = new Limit<>(referenceValue.getValue(), true);
        final LinearRestrictions<LocalTime> timeRestriction =
            LinearRestrictionsFactory.createTimeRestrictions(Defaults.TIME_MIN_LIMIT, max);
        Object o = FieldSpecFactory.fromRestriction(timeRestriction);
        return FieldSpecFactory.fromRestriction(timeRestriction);
    }
}
