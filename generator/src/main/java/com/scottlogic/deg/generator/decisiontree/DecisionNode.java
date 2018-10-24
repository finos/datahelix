package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class DecisionNode {
    private final ArrayList<ConstraintNode> options;

    DecisionNode(Collection<ConstraintNode> options) {
        this.options = new ArrayList<>(options);
    }

    public DecisionNode(ConstraintNode... options) {
        this.options = new ArrayList<>(Arrays.asList(options));
    }

    public Collection<ConstraintNode> getOptions() {
        return new ArrayList<>(options);
    }

    public void addOption(ConstraintNode newConstraint) {
        options.add(newConstraint);
    }

    public void removeOption(ConstraintNode option) {
        options.remove(option);
    }

    public boolean optionWithAtomicConstraintExists(ConstraintNode constraint){
        return options
                .stream()
                .anyMatch(c -> c.atomicConstraintExists(constraint));
    }
}
