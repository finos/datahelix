package com.scottlogic.deg.profile.dtos.constraints;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.scottlogic.deg.profile.dtos.constraints.atomic.AtomicConstraintDTO;

@JsonDeserialize(as = GeneratorConstraintDto.class)
public class GeneratorConstraintDto extends AtomicConstraintDTO {
    public static final String NAME = "generator";

    @JsonProperty(NAME)
    public String generator;
}
