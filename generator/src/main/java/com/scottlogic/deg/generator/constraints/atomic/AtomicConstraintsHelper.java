package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AtomicConstraintsHelper {

    public static List<AtomicConstraint> getConstraintsForField(Collection<AtomicConstraint> atomicConstraints, Field field) {
        return atomicConstraints.stream()
            .filter(atomicConstraint -> atomicConstraint.getField().equals(field))
            .collect(Collectors.toList());
    }
}
