package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;

import java.util.Objects;

/**
 * Stores details about a Generated Value
 * The Field, the Value, the Format, and the source
 */
public class DataBagValue {
    public final Field field;
    public final Object value;
    public final String format;

    public final FieldSpecSource source;

    public DataBagValue(Field field, Object value, String format, FieldSpecSource source){
        this.field = field;
        this.value = value;
        this.format = format;
        this.source = source;
    }

    public DataBagValue(Field field, Object value){
        this(field, value, null, FieldSpecSource.Empty);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataBagValue that = (DataBagValue) o;
        return Objects.equals(value, that.value) &&
            Objects.equals(format, that.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, format);
    }
    
    public Object getFormattedValue() {
        if (format == null || value == null){
            return value;
        }

        return String.format(format, value);
    }

    public Field getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    public String getFormat() {
        return format;
    }

    public FieldSpecSource getSource() {
        return source;
    }

}

