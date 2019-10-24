package com.scottlogic.deg.profile.dtos.constraints;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.common.ConstraintTypeJsonProperty;

@JsonDeserialize(as = GeneratorConstraintDto.class)
public class GeneratorConstraintDto extends AtomicConstraintDTO {

    @JsonProperty(ConstraintTypeJsonProperty.GENERATOR)
    public String generator;

    public GeneratorConstraintDto() {
        super(ConstraintType.GENERATOR);
    }
}
