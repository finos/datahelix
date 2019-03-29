package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.DataBagValueSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;

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

    public Object getValue() throws InvalidProfileException {
        if (format == null || value == null){
            return value;
        }

        try {
            return String.format(format, value);
        } catch (Exception e) {
            throw new InvalidProfileException(String.format("Unable to format value `%s` with format expression `%s`: %s", value, format, e.getMessage()));
        }
    }
}

