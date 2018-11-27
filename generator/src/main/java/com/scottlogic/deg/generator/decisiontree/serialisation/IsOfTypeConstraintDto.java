package com.scottlogic.deg.generator.decisiontree.serialisation;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

public class IsOfTypeConstraintDto implements ConstraintDto {
    public FieldDto field;
    public TypesDto requiredType;

    public IsOfTypeConstraint.Types getTypesFromTypesDto() {
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
            default:
                throw new UnsupportedOperationException("Unsupported type: " + requiredType);
        }
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