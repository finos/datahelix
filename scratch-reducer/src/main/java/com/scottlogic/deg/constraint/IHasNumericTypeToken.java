package com.scottlogic.deg.constraint;

public interface IHasNumericTypeToken<T extends Number> extends IHasTypeToken<T> {
    public Class<T> getNumericTypeToken();
}
