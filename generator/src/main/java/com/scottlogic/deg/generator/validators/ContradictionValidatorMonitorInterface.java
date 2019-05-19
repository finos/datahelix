package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;

import java.util.Collection;
import java.util.Map;

public interface ContradictionValidatorMonitorInterface {
    void contradictionInTree(Field field, Collection<AtomicConstraint> atomicConstraints);
    void treeIsWhollyContradictory();
}
