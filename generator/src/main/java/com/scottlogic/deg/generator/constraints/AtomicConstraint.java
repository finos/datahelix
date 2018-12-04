package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

public interface AtomicConstraint extends IConstraint {

    default Field getField() { return getFields().iterator().next(); }
}
