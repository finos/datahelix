package com.scottlogic.deg.schemas.v3;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Collection;

@JsonDeserialize(using = RuleDeserializer.class)
@JsonSerialize(using = RuleSerializer.class)
public class Rule {
    public String description;
    public Collection<Constraint> constraints;

    public Rule() {}
}
