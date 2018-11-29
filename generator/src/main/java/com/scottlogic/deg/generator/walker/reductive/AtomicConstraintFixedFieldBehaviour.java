package com.scottlogic.deg.generator.walker.reductive;

public enum AtomicConstraintFixedFieldBehaviour{
    //The atomic constraint relates to a field that has been fixed, but doesn't contradict
    LEAVE,
    //The atomic constraint relates to a field that has not been fixed
    INCLUDE,
    //the atomic constraint contradicts a fixed field
    CONSTRAINT_INVALID
}
