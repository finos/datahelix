package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public interface AtomicConstraint extends LogicalConstraint {

    default Field getField() { return getFields().iterator().next(); }

    default AtomicConstraint not() {
        return new AtomicNotConstraint(this);}
}
