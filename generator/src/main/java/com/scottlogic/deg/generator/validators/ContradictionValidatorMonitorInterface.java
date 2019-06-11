package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;

import java.util.Collection;
import java.util.Map;

public interface ContradictionValidatorMonitorInterface {
    void contradictionInTree(Field field, Collection<AtomicConstraint> atomicConstraints);
    void treeIsWhollyContradictory();
}
