package com.scottlogic.deg.generator.decisiontree.serialisation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;

public class IsOfTypeConstraintDto implements ConstraintDto {
    public FieldDto field;
    @JsonProperty("requiredType")
    public String requiredTypeString;

    public String rule;

    public IsOfTypeConstraint.Types getTypesFromTypesDto() {
        return IsOfTypeConstraint.Types.valueOf(requiredTypeString.toUpperCase());
    }
}