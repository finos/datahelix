package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.*;

class ConstraintCollection {
    private final Map<Field, ArrayList<IConstraint>> constraints = new HashMap<>();

    void add(Field field, IConstraint atomicConstraint) {
        if (!constraints.containsKey(field)) {
            constraints.put(field, new ArrayList<>());
        }

        ArrayList<IConstraint> currentConstraints = constraints.get(field);
        currentConstraints.add(atomicConstraint);
    }

    FieldAndConstraintMapping getFieldAndConstraintMapToFixNext(){
        return constraints.entrySet()
            .stream()
            .map(entry -> new FieldAndConstraintMapping(entry.getKey(), entry.getValue()))
            .max(Comparator.comparing(FieldAndConstraintMapping::getPriority))
            .filter(c -> !c.getConstraints().isEmpty())
            .orElse(null);
    }
}

