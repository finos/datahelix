package com.scottlogic.deg.schemas.v3;

import com.fasterxml.jackson.annotation.*;
import com.scottlogic.deg.schemas.common.InvalidSchemaException;

import java.util.Collection;

@JsonPropertyOrder({ "field", "is", "value", "if", "then", "else" })
public class ConstraintDTO {
    // the DTO is very permissive, because validation isn't its job.
    // validation rules should be expressed in JSON schemas and DTO -> Model converters

    /** the ID of the field this constraint constrains, if relevant */
    public String field;

    /** a constant value - eg, used in isEqualTo or isGreaterThan */
    public Object value;

    /** a set of values - eg, used in isInSet */
    public Collection<Object> values;

    /** a constraint to negate - this property should only appear alone */
    public ConstraintDTO not;

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

    private String is;

    @JsonIgnore
    private boolean hasIsConstraintBeenSet = false;

    @JsonSetter("is")
    public void setIs(String is) {
        this.is = is;
        this.hasIsConstraintBeenSet = true;
    }

    @JsonGetter("is")
    public String getIs() throws InvalidSchemaException {
        if(this.hasIsConstraintBeenSet && this.is == null) {
            throw new InvalidSchemaException("Cannot recognise is null");
        }
        return this.is;
    }
}
