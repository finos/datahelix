package com.scottlogic.deg.schemas.v3;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class ConstraintDTO {
    public String type;

    // the DTO is very permissive, because validation isn't its job.
    // validation rules should be expressed in JSON schemas and DTO -> Model converters

    /** the ID of the field this constraint constrains, if relevant */
    public String field; // id of this field

    /** a constant value - eg, used in isEqualTo or isGreaterThan */
    public Object value;

    /** a set of values - eg, used in isInSet */
    public Collection<Object> values;

    /** a set of subconstraints - used in or/and */
    public Collection<ConstraintDTO> constraints;

    /** used in condition - should always co-occur with 'then' */
    public ConstraintDTO condition;

    /** the constraint to apply if 'condition' is true */
    public ConstraintDTO then;

    /** the constraint to apply if 'condition' is false */
    @JsonProperty("else")
    public ConstraintDTO elseCondition;
}
