package com.scottlogic.deg.schemas.v3;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collection;

@JsonPropertyOrder({ "type", "field", "value", "if", "then", "else" })
public class ConstraintDTO {
    public String type;

    // the DTO is very permissive, because validation isn't its job.
    // validation rules should be expressed in JSON schemas and DTO -> Model converters

    /** the ID of the field this constraint constrains, if relevant */
    public String field;

    /** a constant value - eg, used in isEqualTo or isGreaterThan */
    public Object value;

    /** a set of values - eg, used in isInSet */
    public Collection<Object> values;

    /** a set of subconstraints - used in or/and */
    public Collection<ConstraintDTO> anyOf;

    /** a set of subconstraints - used in or/and */
    public Collection<ConstraintDTO> allOf;

    /** used in condition - should always co-occur with 'then' */
    @JsonProperty("if")
    public ConstraintDTO if_;

    /** the constraint to apply if 'if_' is true */
    public ConstraintDTO then;

    /** the constraint to apply if 'if_' is false */
    @JsonProperty("else")
    public ConstraintDTO else_;
}
