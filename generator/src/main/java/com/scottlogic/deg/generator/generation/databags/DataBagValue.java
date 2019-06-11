package com.scottlogic.deg.generator.generation.databags;

import java.util.Objects;

public class DataBagValue {
    private final Object value;
    private final String format;

    public DataBagValue(Object value, String format){
        this.value = value;
        this.format = format;
    }

    public DataBagValue(Object value) {
        this(value, null);
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
        if (format == null || value == null) {
            return value;
        }

        return String.format(format, value);
    }

    public Object getUnformattedValue(){
        return value;
    }
}

