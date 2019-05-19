package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;

import java.util.Collection;

public class ContradictionValidationMonitor  implements ContradictionValidatorMonitorInterface {

    public void contradictionInTree(Field field, Collection<AtomicConstraint> atomicConstraints) {
        System.out.println(String.format("A contradiction was detected in the tree for field: %s", field.name));
        System.out.println("Constraints that were being checked when the contradiction was detected :");
        atomicConstraints
            .stream()
            .map(entry -> String.format("Constraint: %s", entry.toString()))
            .forEach(System.out::println);
    }

    public void treeIsWhollyContradictory() {
        System.out.println("The provided profile is wholly contradictory. no fields can successfully be fixed.");
    }
}
