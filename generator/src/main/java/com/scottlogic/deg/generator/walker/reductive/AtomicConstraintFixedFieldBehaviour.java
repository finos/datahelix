package com.scottlogic.deg.generator.walker.reductive;

public enum AtomicConstraintFixedFieldBehaviour{
    //The atomic constraint relates to a field that has been fixed, but doesn't contradict
    NON_CONTRADICTORY,
    //The atomic constraint relates to a field that has not been fixed
    FIELD_NOT_FIXED,
    //the atomic constraint contradicts a fixed field
    CONSTRAINT_CONTRADICTS
}
