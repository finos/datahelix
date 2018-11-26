package com.scottlogic.deg.generator.decisiontree.serialisation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

public class IsOfTypeConstraintDto implements ConstraintDto {
    public FieldDto field;
    public TypesDto requiredType;

    static public IsOfTypeConstraintDto toDto(IsOfTypeConstraint constraint) {
        IsOfTypeConstraintDto dto = new IsOfTypeConstraintDto();
        dto.field = new FieldDto(constraint.field.name);
        switch (constraint.requiredType) {
        case NUMERIC:
            dto.requiredType = TypesDto.numeric;
            break;
        case STRING:
            dto.requiredType = TypesDto.string;
            break;
        case TEMPORAL:
            dto.requiredType = TypesDto.temporal;
            break;
        default:
            throw new UnsupportedOperationException("Unsupported type: " + constraint.requiredType);
        }
        return dto;
    }

    @Override
    public IConstraint map() {
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