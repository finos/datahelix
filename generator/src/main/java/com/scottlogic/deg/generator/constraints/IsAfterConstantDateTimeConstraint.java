package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.time.LocalDateTime;

public class IsAfterConstantDateTimeConstraint implements IConstraint {
    public final Field field;
    public final LocalDateTime referenceValue;

    public IsAfterConstantDateTimeConstraint(Field field, LocalDateTime referenceValue) {
        this.field = field;
        this.referenceValue = referenceValue;
    }

    @Override
    public String toDotLabel(){
        return String.format("%s > %s", field.name, referenceValue);
    }
}
