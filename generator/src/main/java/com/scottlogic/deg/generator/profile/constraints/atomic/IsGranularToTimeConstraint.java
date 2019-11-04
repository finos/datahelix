package com.scottlogic.deg.generator.profile.constraints.atomic;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.TimeGranularity;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;

public class IsGranularToTimeConstraint implements AtomicConstraint {
    public final TimeGranularity timeGranularity;
    public final Field field;

    public IsGranularToTimeConstraint(Field field, TimeGranularity timeGranularity) {
        if(field == null)
            throw new IllegalArgumentException("field must not be null");
        if(timeGranularity == null)
            throw new IllegalArgumentException("granularity must not be null");

        this.timeGranularity = timeGranularity;
        this.field = field;
    }

    @Override
    public Field getField() {
        return null;
    }

    @Override
    public AtomicConstraint negate() {
        throw new ValidationException("Time Granularity cannot be negated or used in if statements");
    }

    @Override
    public FieldSpec toFieldSpec() {
        return null;
    }
}
