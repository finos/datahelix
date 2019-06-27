package com.scottlogic.deg.profile.v0_1;

import java.util.Collection;

public class ConstraintDTOBuilder {
    private ConstraintDTO constraintDTO;

    private ConstraintDTOBuilder(){
        this.constraintDTO = new ConstraintDTO();
    }

    public static ConstraintDTOBuilder instance() {
        return new ConstraintDTOBuilder();
    }

    public ConstraintDTOBuilder appendField (String fieldName){
        this.constraintDTO.field = fieldName;
        return this;
    }

    public ConstraintDTOBuilder appendIs (String constraintName){
        this.constraintDTO.is = constraintName;
        return this;
    }

    public ConstraintDTOBuilder appendValue (Object value){
        this.constraintDTO.value = value;
        return this;
    }

    public ConstraintDTOBuilder appendValues (Collection<Object> values){
        this.constraintDTO.values = values;
        return this;
    }

    public ConstraintDTOBuilder appendNot (ConstraintDTO constraint){
        this.constraintDTO.not = constraint;
        return this;
    }

    public ConstraintDTOBuilder appendAnyOf (Collection<ConstraintDTO> constraints){
        this.constraintDTO.anyOf = constraints;
        return this;
    }

    public ConstraintDTOBuilder appendAllOf (Collection<ConstraintDTO> constraints){
        this.constraintDTO.allOf = constraints;
        return this;
    }

    public ConstraintDTOBuilder appendIf (ConstraintDTO constraint){
        this.constraintDTO.if_ = constraint;
        return this;
    }

    public ConstraintDTOBuilder appendElse (ConstraintDTO constraint){
        this.constraintDTO.else_ = constraint;
        return this;
    }

    public ConstraintDTOBuilder appendThen (ConstraintDTO constraint){
        this.constraintDTO.then = constraint;
        return this;
    }

    public ConstraintDTO Build(){
        return this.constraintDTO;
    }
}
