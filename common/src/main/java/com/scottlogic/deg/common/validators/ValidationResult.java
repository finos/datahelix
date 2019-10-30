package com.scottlogic.deg.common.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationResult
{
    public final boolean isValid;
    public final List<String> errors;

    private ValidationResult(boolean isValid, List<String> errors)
    {
        this.isValid = isValid;
        this.errors = errors;
    }

    public static ValidationResult success(){
        return new ValidationResult(true, new ArrayList<>());
    }

    public static ValidationResult failure(String... errors)
    {
        return new ValidationResult(false, Arrays.stream(errors).collect(Collectors.toList()));
    }

    public static ValidationResult failure(List<String> errors)
    {
        return new ValidationResult(false, errors);
    }

    public static ValidationResult combine(List<ValidationResult> validationResults)
    {
        boolean isValid = validationResults.stream().allMatch(validationResult -> validationResult.isValid);
        List<String> errors = validationResults.stream().flatMap(validationResult -> validationResult.errors.stream()).collect(Collectors.toList());
        return new ValidationResult(isValid, errors);
    }

    public static ValidationResult combine(ValidationResult... validationResults)
    {
        boolean isValid = Arrays.stream(validationResults).allMatch(validationResult -> validationResult.isValid);
        List<String> errors = Arrays.stream(validationResults).flatMap(validationResult -> validationResult.errors.stream()).collect(Collectors.toList());
        return new ValidationResult(isValid, errors);
    }
}


