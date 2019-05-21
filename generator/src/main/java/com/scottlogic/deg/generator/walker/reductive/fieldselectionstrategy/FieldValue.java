package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import com.scottlogic.deg.generator.restrictions.FormatRestrictions;

public class FieldValue {
    private final Field field;
    private final Object value;
    private final FieldSpecSource valueSource;
    private final String format;

    public FieldValue(Field field, Object value){
        this(field, value, FieldSpec.Empty);
    }
    public FieldValue(Field field, Object value, FieldSpec valueSource){
        this.value = value;
        this.field = field;
        this.valueSource = valueSource.getFieldSpecSource();
        this.format = valueSource.getFormatRestrictions() == null ? null : valueSource.getFormatRestrictions().formatString;
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

    public String getFormat() {
        return format;
    }

    public FieldSpecSource getFieldSpecSource() {
        return valueSource;
    }
}
