package com.scottlogic.deg.generator.decisiontree.test_utils;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

public class IsOfTypeConstraintDto implements ConstraintDto {
    public FieldDto field;
    public TypesDto requiredType;

    @Override
    public IConstraint map() {
        return new IsOfTypeConstraint(new Field(field.name), getTypesFromTypesDto());
    }

    private IsOfTypeConstraint.Types getTypesFromTypesDto() {
        switch (requiredType) {
            case Numeric:
                return IsOfTypeConstraint.Types.NUMERIC;
            case String:
                return IsOfTypeConstraint.Types.STRING;
            case Temporal:
                return IsOfTypeConstraint.Types.TEMPORAL;
        }

        return null;
    }

    public enum TypesDto {
        Numeric,
        String,
        Temporal
    }
}