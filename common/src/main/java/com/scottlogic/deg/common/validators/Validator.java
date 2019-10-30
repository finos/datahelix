package com.scottlogic.deg.common.validators;

public interface Validator<T>
{
    ValidationResult validate(T obj);
}
