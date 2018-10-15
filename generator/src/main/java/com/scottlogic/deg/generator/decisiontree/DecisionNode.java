package com.scottlogic.deg.generator.decisiontree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class DecisionNode {
    private final Collection<ConstraintNode> options;

    DecisionNode(Collection<ConstraintNode> options) {
        this.options = options;
    }

    public DecisionNode(ConstraintNode... options) {
        this.options = Arrays.asList(options);
    }

    public Collection<ConstraintNode> getOptions() {
        return new ArrayList<>(options);
    }
}
