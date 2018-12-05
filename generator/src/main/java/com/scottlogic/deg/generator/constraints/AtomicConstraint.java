package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public interface AtomicConstraint extends Constraint {

    Field getField();

    String toDotLabel();

    default AtomicConstraint negate() {
        return new AtomicNotConstraint(this);}
}
