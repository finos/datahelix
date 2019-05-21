package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.DataBagValueSource;

import java.util.Objects;

public class DataBagValue {
    final Object value;
    private final String format;

    public final DataBagValueSource source;

    public DataBagValue(Object value, String format, DataBagValueSource source){
        this.value = value;
        this.format = format;
        this.source = source;
    }

    public DataBagValue(Object value, DataBagValueSource source){
        this.value = value;
        this.source = source;
        this.format = null;
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

    public Object getUnformattedValue(){
        return value;
    }
}

