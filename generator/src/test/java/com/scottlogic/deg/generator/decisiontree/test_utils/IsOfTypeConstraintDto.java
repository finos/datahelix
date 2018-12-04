package com.scottlogic.deg.generator.decisiontree.test_utils;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

public class IsOfTypeConstraintDto implements ConstraintDto {
    public FieldDto field;
    public TypesDto requiredType;

    @Override
    public AtomicConstraint map() {
        return new IsOfTypeConstraint(new Field(field.name), getTypesFromTypesDto());
    }

    private IsOfTypeConstraint.Types getTypesFromTypesDto() {
        switch (requiredType) {
            case Numeric:
            case numeric:
                return IsOfTypeConstraint.Types.NUMERIC;
            case String:
            case string:
                return IsOfTypeConstraint.Types.STRING;
            case Temporal:
            case temporal:
                return IsOfTypeConstraint.Types.TEMPORAL;
        }

        return null;
    }

    public enum TypesDto {
        //TODO: Retire these editions, leave the lower case editions
        Numeric,
        String,
        Temporal,

        //These should remain to match the profile type names (i.e. lowercase 'temporal', etc.)
        numeric,
        string,
        temporal,
    }
}