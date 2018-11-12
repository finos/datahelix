package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

public class IsOfTypeConstraintDto implements ConstraintDto {
    public FieldDto field;
    public TypesDto requiredType;

    @Override
    public IConstraint map() {
        return new IsOfTypeConstraint(new Field(this.field.name), getTypesFromTypesDto());
    }

    private IsOfTypeConstraint.Types getTypesFromTypesDto() {
        switch (this.requiredType) {
            case Numeric:
                return IsOfTypeConstraint.Types.Numeric;
            case String:
                return IsOfTypeConstraint.Types.String;
            case Temporal:
                return IsOfTypeConstraint.Types.Temporal;
        }

        return null;
    }

    public enum TypesDto {
        Numeric,
        String,
        Temporal
    }
}