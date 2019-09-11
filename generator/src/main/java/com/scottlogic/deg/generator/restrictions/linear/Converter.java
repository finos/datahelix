package com.scottlogic.deg.generator.restrictions.linear;

public interface Converter<T> {
    public T convert(Object value);
    public boolean isCorrectType(Object value);
}
