package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public interface AtomicConstraint extends LogicalConstraint {

     Field getField();

    default AtomicConstraint not() {
        return new AtomicNotConstraint(this);}
}
