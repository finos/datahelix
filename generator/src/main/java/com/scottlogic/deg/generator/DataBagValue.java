package com.scottlogic.deg.generator;

import java.util.Objects;

public class DataBagValue {
    public final Field field;
    public final Object value;
    public final String format;

    public final DataBagValueSource source;

    public DataBagValue(Field field, Object value, String format, DataBagValueSource source){
        this.field = field;
        this.value = value;
        this.format = format;
        this.source = source;
    }

    //unsafe, only used in test
    public DataBagValue(Field field, Object value){
        this(field, value, null, DataBagValueSource.Empty);
    }

    //unsafe, usage should be replaced
    public DataBagValue(Object value, String format, DataBagValueSource source){
        this(null, value, format, source);
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

    public DataBagValueSource getSource() {
        return source;
    }

}

