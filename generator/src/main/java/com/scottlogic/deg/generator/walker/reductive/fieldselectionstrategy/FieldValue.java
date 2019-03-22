package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.restrictions.FormatRestrictions;

public class FieldValue {
    private final Field field;
    private final Object value;
    private final FormatRestrictions formatRestrictions;

    public FieldValue(Field field, Object value){
        this(field, value, null);
    }
    public FieldValue(Field field, Object value, FormatRestrictions formatRestrictions){
        this.value = value;
        this.field = field;
        this.formatRestrictions = formatRestrictions;
    }

    public Object getValue() {
        return value;
    }

    public Field getField() {
        return field;
    }

    @Override
    public String toString() {
        if (value == null) return "null";
        return value.toString();
    }

    public FormatRestrictions getFormatRestrictions() {
        return formatRestrictions;
    }
}
